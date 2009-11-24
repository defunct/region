package com.goodworkalan.region;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;

/**
 * Tracks a lockable and dirtyable region of a file channel. A region maps a
 * byte buffer to a lock to guard writing to the byte buffer and a position in
 * the file. A region implements the {@link Dirtyable} interface to that it can
 * track which bytes in the byte buffer need to be written to disk.
 * <p>
 * A <code>Region</code> does not actually contain a file channel itself.
 * 
 * @author Alan Gutierrez
 */
public interface Region extends Dirtyable
{
    /**
     * Get the lock used to guard the underlying byte buffer.
     * 
     * @return The lock used to guard the region.
     */
    public Lock getLock();

    /**
     * Get the file position of the region on disk.
     * 
     * @return The file position of the region on disk.
     */
    public long getPosition();
    
    /**
     * Get the byte content of the region. Subclasses
     * will define this method to return a byte buffer that corresponds to the
     * given file position, accounting for any offsets.
     * 
     * @return The the byte content of the region.
     */
    public ByteBuffer getByteBuffer();
}
