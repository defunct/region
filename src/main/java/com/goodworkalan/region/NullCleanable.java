package com.goodworkalan.region;

/**
 * A null implementation of {@link Cleanable} that does not bother to record
 * which bytes are clean or dirty.
 * 
 * @author Alan Gutierrez
 */
public class NullCleanable implements Cleanable {
    /** The length of the buffer. */
    private final int length;

    /**
     * Create a null implementation of cleanable for a buffer of the given
     * length.
     * 
     * @param length
     *            The length of the buffer.
     */
    public NullCleanable(int length) {
        this.length = length;
    }

    /**
     * Get the length of the buffer.
     * 
     * @return The length of the buffer.
     */
    public int getLength() {
        return length;
    }

    /**
     * Does nothing.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void dirty(int offset, int length) {
    }

    /** Does nothing. */
    public void dirty() {
    }

    /**
     * Does nothing.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void clean(int offset, int length) {
    }

    /** Does nothing. */
    public void clean() {
    }
}
