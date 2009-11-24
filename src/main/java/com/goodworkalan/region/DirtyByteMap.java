package com.goodworkalan.region;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Dirty regions of a page as map of offsets to counts of dirty bytes. This is a
 * base class for raw pages and headers that want to write only the regions of a
 * page that have changed.
 * 
 * @author Alan Gutierrez
 */
public class DirtyByteMap implements Cleanable {
    /** The map of dirty regions offsets to count of dirty bytes. */
    final SortedMap<Integer, Integer> dirtied;

    /** The length of the buffer. */
    private final int length;

    /**
     * Construct a dirty region map that will track the dirty regions of the
     * byte content at the given file position.
     * 
     * @param length
     *            The length of the region to track.
     */
    public DirtyByteMap(int length) {
        this.length = length;
        this.dirtied = new TreeMap<Integer, Integer>();
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
     * Mark the entire buffer as dirty.
     */
    public void dirty() {
        dirty(0, getLength());
    }

    /**
     * Mark as dirty the given length of bytes at the given offset.
     * <p>
     * If the specified region is overlaps or a another dirty region, the
     * regions are combined to create a single dirty region.
     * 
     * @param offset
     *            The offset of the dirty region.
     * @param length
     *            The length of the dirty region.
     */
    public void dirty(int offset, int length) {
        int start = offset;
        int end = offset + length;
        if (start < 0) {
            throw new IllegalStateException();
        }

        if (end > getLength()) {
            throw new IllegalStateException();
        }

        INVALIDATE: for (;;) {
            Iterator<Map.Entry<Integer, Integer>> entries = dirtied.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<Integer, Integer> entry = entries.next();
                if (start < entry.getKey() && end >= entry.getKey()) {
                    entries.remove();
                    end = end > entry.getValue() ? end : entry.getValue();
                    continue INVALIDATE;
                } else if (entry.getKey() <= start && start <= entry.getValue()) {
                    entries.remove();
                    start = entry.getKey();
                    end = end > entry.getValue() ? end : entry.getValue();
                    continue INVALIDATE;
                } else if (entry.getValue() < start) {
                    break;
                }
            }
            break;
        }
        dirtied.put(start, end);
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
    public void clean(int offset, int length) {
        int start = offset;
        int end = offset + length;
        if (start < 0) {
            throw new IllegalStateException();
        }

        if (end > getLength()) {
            throw new IllegalStateException();
        }

        INVALIDATE: for (;;) {
            Iterator<Map.Entry<Integer, Integer>> entries = dirtied.entrySet()
                    .iterator();
            while (entries.hasNext()) {
                Map.Entry<Integer, Integer> entry = entries.next();
                if (start <= entry.getKey() && end > entry.getKey()) {
                    entries.remove();
                    if (end < entry.getValue()) {
                        dirtied.put(end, entry.getValue());
                    }
                    continue INVALIDATE;
                } else if (entry.getKey() < start && start < entry.getValue()) {
                    entries.remove();
                    dirtied.put(entry.getKey(), start);
                    if (end < entry.getValue()) {
                        dirtied.put(end, entry.getValue());
                    }
                    continue INVALIDATE;
                } else if (entry.getValue() < start) {
                    break;
                }
            }
        }
    }

    /**
     * Mark the entire buffer as clean.
     */
    public void clean() {
        dirtied.clear();
    }

    /**
     * Write the bytes marked as dirty in this dirty byte map to the given byte
     * buffer to the given file channel at the given file position.
     * 
     * @param byteBuffer
     *            The byte buffer.
     * @param fileChannel
     *            The file channel.
     * @param position
     *            The file position.
     * @throws IOException
     *             If an I/O error occurs while writing the byte buffer.
     */
    public void write(ByteBuffer byteBuffer, FileChannel fileChannel,
            long position) throws IOException {
        for (Map.Entry<Integer, Integer> entry : dirtied.entrySet()) {
            byteBuffer.limit(entry.getValue());
            byteBuffer.position(entry.getKey());

            fileChannel.write(byteBuffer, position + entry.getKey());
        }

        byteBuffer.limit(byteBuffer.capacity());

        clean();
    }
}