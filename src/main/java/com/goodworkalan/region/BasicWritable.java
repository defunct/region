package com.goodworkalan.region;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;

/**
 * A basic writeable implementation that writes its content to disk and marks
 * the entire region as clean in the cleanable used to track dirty bytes.
 * 
 * @author Alan Gutierrez
 */
public class BasicWritable extends AbstractRegion implements Writable {
    /** The cleanable interface used to track which bytes are dirty. */
    protected final Cleanable cleanable;

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
     * @param cleanable
     *            The cleanable interface used to record dirtyed bytes in the
     *            byte buffer.
     */
    public BasicWritable(long position, ByteBuffer byteBuffer, Lock lock, Cleanable cleanable) {
        super(position, byteBuffer, lock);
        this.cleanable = cleanable;
    }

    /**
     * Return the drityable interface used to record dirtyed bytes in the byte
     * buffer.
     * 
     * @return The dirtyable recorder for the byte buffer.
     */
    @Override
    protected Dirtyable getDirtyable() {
        return cleanable;
    }

    /**
     * Write the entire to the given file channel using the given disk at the
     * position of this region offset by the given offset. Marks the entire
     * cleanable used to track dirty bytes as clean.
     * 
     * @param fileChannel
     *            The file channel to write to.
     * @param offset
     *            An offset to add to the dirty region map file position.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public void write(FileChannel fileChannel, int offset) throws IOException {
        ByteBuffer bytes = getByteBuffer();
        bytes.clear();
        fileChannel.write(bytes, offset + getPosition());
        cleanable.clean();
    }
}
