package com.goodworkalan.region;

/**
 * An extension the dirtyable interface that goes the other way and marks bytes
 * as clean.
 * 
 * @author Alan Gutierrez
 */
public interface Cleanable extends Dirtyable {
    /**
     * Mark as dirty the bytes in the byte buffer starting at the given offset
     * and extending for the given length.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void clean(int offset, int length);

    /**
     * Mark the entire byte buffer as clean.
     */
    public void clean();
}
