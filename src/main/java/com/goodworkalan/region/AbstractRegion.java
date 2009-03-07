package com.goodworkalan.region;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

/**
 * An abstract region implementation used as the basis for the
 * {@link BasicRegion} and {@link BasicWritable} implementations.
 * 
 * @author Alan Gutierrez
 */
public abstract class AbstractRegion implements Region
{
    /** The position of the region on disk. */
    private final long position;
    
    /** The byte buffer of region content. */
    private final ByteBuffer byteBuffer;
    
    /** A lock used to lock the region for reading and writing. */
    private final Lock lock;

    /**
     * Create a region at the given file position, with the given byte buffer of
     * region content, guarded by the given lock.
     * 
     * @param position
     *            The position of the region on disk.
     * @param byteBuffer
     *            The byte buffer of region content.
     * @param lock
     *            A lock used to lock the region for reading and writing.
     */
    public AbstractRegion(long position, ByteBuffer byteBuffer, Lock lock)
    {
        this.position = position;
        this.byteBuffer = byteBuffer;
        this.lock = lock;
    }

    /**
     * Return the drityable interface used to record dirtyed bytes in the byte
     * buffer.
     * 
     * @return The dirtyable recorder for the byte buffer.
     */
    protected abstract Dirtyable getDirtyable();

    /**
     * Get the lock used to guard the underlying byte buffer.
     * 
     * @return The lock used to guard the region.
     */
    public Lock getLock()
    {
        return lock;
    }

    /**
     * Get the file position of the region on disk.
     * 
     * @return The file position of the region on disk.
     */
    public long getPosition()
    {
        return position;
    }

    /**
     * Get the byte content of the region.
     * 
     * @return The byte content of the region.
     */
    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    /**
     * Get the length of the buffer.
     * 
     * @return The length of the buffer.
     */
    public int getLength()
    {
        return getDirtyable().getLength();
    }
    
    /**
     * Mark as dirty the bytes in the byte buffer starting at the given offset
     * and extending for the given length.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void dirty(int offset, int length)
    {
        getDirtyable().dirty(offset, length);
    }
    
    /**
     * Mark the entire buffer as dirty.
     */
    public void dirty()
    {
        getDirtyable().dirty();
    }
}
