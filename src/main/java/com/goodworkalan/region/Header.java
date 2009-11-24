package com.goodworkalan.region;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * An array of regions in a file representing header fields. The header is
 * written by writing to
 * 
 * @author Alan Gutierrez
 * 
 * @param <K>
 *            The header key type.
 */
public class Header<K> extends BasicWritable {
    /**
     * The map of keys to a offset/length pairs. The lists are two element lists
     * containing the field offset and field length.
     */
    private final Map<K, List<Integer>> offsets;

    /**
     * Create a header at the given file position that maps the given map of
     * keys to offsets. The given byte buffer is used to buffer the header
     * fields before they are written do disk. The given lock is used to lock
     * the byte buffer for reading and writer.
     * 
     * @param position
     *            The position of the region on disk.
     * @param offset
     *            The map of keys to a offset/length pairs. The lists are two
     *            element lists containing the field offset and field length.
     * @param byteBuffer
     *            The byte buffer of header content.
     * @param lock
     *            A lock used to lock the header for reading and writing.
     * @param cleanable
     *            The cleanable interface used to record dirtyed bytes in the
     *            byte buffer.
     */
    Header(long position, Map<K, List<Integer>> offsets, ByteBuffer byteBuffer, Lock lock) {
        super(position, byteBuffer, lock, new DirtyByteMap(byteBuffer.capacity()));
        this.offsets = offsets;
    }

    /**
     * Get the header field region associated with the given key. The field
     * region contain a byte buffer slice of the header focused on the header
     * field region.
     * <p>
     * The byte buffer slice will start at the position of the header filed in
     * the header byte buffer. The capacity of the byte buffer slice will be the
     * header field length.
     * <p>
     * The field region will contain a lock, shared by all field regions
     * returned by the header, that locks the entire header for reading and
     * writing.
     * 
     * @param key
     *            The header field key
     * @return A region for the header field.
     */
    public Region get(K key) {
        List<Integer> offset = offsets.get(key);
        Dirtyable subDirtyable = new SubCleanable(cleanable, offset.get(0), offset.get(1));
        getLock().lock();
        try {
            ByteBuffer byteBuffer = getByteBuffer();

            int position = byteBuffer.position();
            int limit = byteBuffer.limit();

            byteBuffer.position(offset.get(0));
            byteBuffer.limit(offset.get(0) + offset.get(1));

            ByteBuffer subByteBuffer = byteBuffer.slice();

            byteBuffer.position(position);
            byteBuffer.limit(limit);

            return new BasicRegion(position + offset.get(0), subByteBuffer, getLock(), subDirtyable);
        } finally {
            getLock().unlock();
        }
    }
}
