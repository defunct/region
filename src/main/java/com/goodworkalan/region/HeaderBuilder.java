package com.goodworkalan.region;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A builder for a header that defines the header by mapping a header field key
 * to a header field length. The header files will be arrayed on file one after
 * another in the order in which they are specified.
 * 
 * @author Alan Gutierrez
 * 
 * @param <K>
 *            The header key type.
 */
public class HeaderBuilder<K> {
    /** The offset of the next field. */
    private int offset;

    /**
     * The map of keys to a offset/length pairs. The lists are two element lists
     * containing the field offset and field length.
     */
    private final Map<K, List<Integer>> offsets;

    /** Create a new header builder. */
    public HeaderBuilder() {
        this.offsets = new HashMap<K, List<Integer>>();
    }

    /**
     * Create a field of the given length at the current offset that will be
     * retrieved with the given key.
     * 
     * @param key
     *            The field key.
     * @param length
     *            The field length.
     */
    public void addField(K key, int length) {
        List<Integer> mapping = new ArrayList<Integer>(2);
        mapping.add(offset);
        mapping.add(length);
        offsets.put(key, mapping);
        offset += length;
    }

    /**
     * Create a new file header from this file header definition that will write
     * itself to the given file position.
     * 
     * @param position
     *            The file position.
     * @return A new file header.
     */
    public Header<K> newHeader(long position) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(offset);
        return new Header<K>(position, offsets, byteBuffer, new ReentrantLock());
    }
}
