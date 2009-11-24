package com.goodworkalan.region;

/**
 * A cleanable that reports on a buffer slice of a region buffer.
 * 
 * @author Alan Gutierrez
 */
public class SubCleanable implements Cleanable {
    /** The offset into the parent cleanable. */
    private final int offset;

    /** The length of the the buffer slice. */
    private final int length;

    /** The parent. */
    private final Cleanable cleanable;

    /**
     * Create a cleanable that reports on a slice of the given cleanable defined
     * by the given offset and the given length.
     * 
     * @param dirtyable
     *            The parent cleanable.
     * @param offset
     *            The offset into the parent cleanable.
     * @param length
     *            The length of the the buffer slice.
     */
    public SubCleanable(Cleanable dirtyable, int offset, int length) {
        this.offset = offset;
        this.length = length;
        this.cleanable = dirtyable;
    }

    /**
     * Get the length of the buffer slice.
     * 
     * @return The length of the buffer slice.
     */
    public int getLength() {
        return length;
    }

    /**
     * Mark the entire buffer slice as dirty.
     */
    public void dirty() {
        dirty(0, length);
    }

    /**
     * Mark as dirty the bytes in the byte buffer slice starting at the given
     * offset and extending for the given length.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void dirty(int offset, int length) {
        if (length > getLength()) {
            throw new IndexOutOfBoundsException();
        }
        cleanable.dirty(this.offset + offset, length);
    }

    /**
     * Mark as dirty the bytes in the byte buffer slice starting at the given
     * offset and extending for the given length.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void clean(int offset, int length) {
        if (length > getLength()) {
            throw new IndexOutOfBoundsException();
        }
        cleanable.clean(this.offset + offset, length);
    }

    /**
     * Mark the entire byte buffer slice as clean.
     */
    public void clean() {
        clean(0, length);
    }
}
