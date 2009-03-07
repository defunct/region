package com.goodworkalan.region;

/**
 * Used to mark regions of a buffer as dirty and in need of writing to disk.
 * The dirtyable interface parallels a byte buffer.
 *  
 * @author Alan Gutierrez
 */
public interface Dirtyable
{
    /**
     * Get the length of the buffer.
     * 
     * @return The length of the buffer.
     */
    public int getLength();

    /**
     * Mark as dirty the bytes in the byte buffer starting at the given offset
     * and extending for the given length.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void dirty(int offset, int length);

    /**
     * Mark the entire buffer as dirty.
     */
    public void dirty();
}
