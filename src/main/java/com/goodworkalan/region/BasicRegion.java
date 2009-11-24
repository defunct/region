package com.goodworkalan.region;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

/**
 * A basic region implementation that does not write its own content.
 * <p>
 * When a region does not need to track which bytes are dirty, a
 * {@link NullCleanable} can be used to implement the <code>Dirtyable</code>
 * interface.
 * <p>
 * The lock can be ignored in the case of a region that is never accessed
 * concurrently. When a region is never accessed concurrently, locking the
 * uncontended lock ought not to create a significant performance penalty.
 * 
 * @author Alan Gutierrez
 */
public class BasicRegion extends AbstractRegion {
    /** Used to track which bytes in the byte buffer are dirty. */
    public Dirtyable dirtyable;

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
     * @param dirtyable
     *            The drityable interface used to record dirtyed bytes in the
     *            byte buffer.
     */
    public BasicRegion(long position, ByteBuffer byteBuffer, Lock lock, Dirtyable dirtyable) {
        super(position, byteBuffer, lock);
        this.dirtyable = dirtyable;
    }

    /**
     * Return the drityable interface used to record dirtyed bytes in the byte
     * buffer.
     * 
     * @return The dirtyable recorder for the byte buffer.
     */
    @Override
    protected Dirtyable getDirtyable() {
        return dirtyable;
    }
}
