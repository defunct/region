package com.goodworkalan.region;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * A region that writes itself to an underlying file channel, possibly taking
 * into consideration which bytes in the byte buffer are dirty and which are
 * clean.
 * <p>
 * This interface is intended for regions that write only the dirty portions of
 * their byte buffer.
 * 
 * @author Alan Gutierrez
 */
public interface Writable extends Region {
    /**
     * Write the dirty regions to the given file channel using the given disk at
     * the position of this region offset by the given offset.
     * 
     * @param fileChannel
     *            The file channel to write to.
     * @param offset
     *            An offset to add to the dirty region map file position.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public void write(FileChannel fileChannel, int offset) throws IOException;
}