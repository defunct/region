package com.goodworkalan.region;

/**
 * A cleanable that reports on a buffer slice of a region buffer. 
 *
 * @author Alan Gutierrez
 */
public class SubCleanable implements Cleanable
{
    /** The offset into the parent cleanable. */
    private final int offset;
    
    /** The length of the the buffer slice. */
    private final int length;
    
    /** The parent. */
    private final Cleanable cleanable;

    // TODO Document.
    public SubCleanable(Cleanable dirtyable, int offset, int length)
    {
        this.offset = offset;
        this.length = length;
        this.cleanable = dirtyable;
    }
    
    // TODO Document.
    public int getLength()
    {
        return length;
    }

    // TODO Document.
    public void dirty()
    {
        dirty(0, length);
    }

    // TODO Document.
    public void dirty(int offset, int length)
    {
        if (length > getLength())
        {
            throw new IndexOutOfBoundsException();
        }
        cleanable.dirty(this.offset + offset, length);
    }
    
    // TODO Document.
    public void clean(int offset, int length)
    {
        if (length > getLength())
        {
            throw new IndexOutOfBoundsException();
        }
        cleanable.clean(this.offset + offset, length);
    }
    
    // TODO Document.
    public void clean()
    {
        clean(0, length);
    }
}
