/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons HandlesStreamOutput.java 2012-7-6 10:23:53 l.xue.nong$$
 */
package cn.com.rebirth.commons.io.stream;

import gnu.trove.impl.Constants;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.util.Arrays;

import cn.com.rebirth.commons.io.stream.StreamOutput;


/**
 * The Class HandlesStreamOutput.
 *
 * @author l.xue.nong
 */
public class HandlesStreamOutput extends AdapterStreamOutput {

	
	/** The Constant DEFAULT_IDENTITY_THRESHOLD. */
	private static final int DEFAULT_IDENTITY_THRESHOLD = 50;

	
	
	/** The identity threshold. */
	private final int identityThreshold;

	
	/** The handles. */
	private final TObjectIntHashMap<String> handles = new TObjectIntHashMap<String>(Constants.DEFAULT_CAPACITY,
			Constants.DEFAULT_LOAD_FACTOR, -1);

	
	/** The identity handles. */
	private final HandleTable identityHandles = new HandleTable(10, (float) 3.00);

	
	/**
	 * Instantiates a new handles stream output.
	 *
	 * @param out the out
	 */
	public HandlesStreamOutput(StreamOutput out) {
		this(out, DEFAULT_IDENTITY_THRESHOLD);
	}

	
	/**
	 * Instantiates a new handles stream output.
	 *
	 * @param out the out
	 * @param identityThreshold the identity threshold
	 */
	public HandlesStreamOutput(StreamOutput out, int identityThreshold) {
		super(out);
		this.identityThreshold = identityThreshold;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.AdapterStreamOutput#writeUTF(java.lang.String)
	 */
	@Override
	public void writeUTF(String s) throws IOException {
		if (s.length() < identityThreshold) {
			int handle = handles.get(s);
			if (handle == -1) {
				handle = handles.size();
				handles.put(s, handle);
				out.writeByte((byte) 0);
				out.writeVInt(handle);
				out.writeUTF(s);
			} else {
				out.writeByte((byte) 1);
				out.writeVInt(handle);
			}
		} else {
			int handle = identityHandles.lookup(s);
			if (handle == -1) {
				handle = identityHandles.assign(s);
				out.writeByte((byte) 2);
				out.writeVInt(handle);
				out.writeUTF(s);
			} else {
				out.writeByte((byte) 3);
				out.writeVInt(handle);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.AdapterStreamOutput#reset()
	 */
	@Override
	public void reset() throws IOException {
		handles.clear();
		identityHandles.clear();
		out.reset();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.AdapterStreamOutput#reset(cn.com.rebirth.search.commons.io.stream.StreamOutput)
	 */
	public void reset(StreamOutput out) throws IOException {
		super.reset(out);
		reset();
	}

	
	/**
	 * The Class HandleTable.
	 *
	 * @author l.xue.nong
	 */
	private static class HandleTable {

		
		
		/** The size. */
		private int size;
		
		
		/** The threshold. */
		private int threshold;
		
		
		/** The load factor. */
		private final float loadFactor;
		
		
		/** The spine. */
		private int[] spine;
		
		
		/** The next. */
		private int[] next;
		
		
		/** The objs. */
		private Object[] objs;

		
		/**
		 * Instantiates a new handle table.
		 *
		 * @param initialCapacity the initial capacity
		 * @param loadFactor the load factor
		 */
		HandleTable(int initialCapacity, float loadFactor) {
			this.loadFactor = loadFactor;
			spine = new int[initialCapacity];
			next = new int[initialCapacity];
			objs = new Object[initialCapacity];
			threshold = (int) (initialCapacity * loadFactor);
			clear();
		}

		
		/**
		 * Assign.
		 *
		 * @param obj the obj
		 * @return the int
		 */
		int assign(Object obj) {
			if (size >= next.length) {
				growEntries();
			}
			if (size >= threshold) {
				growSpine();
			}
			insert(obj, size);
			return size++;
		}

		
		/**
		 * Lookup.
		 *
		 * @param obj the obj
		 * @return the int
		 */
		int lookup(Object obj) {
			if (size == 0) {
				return -1;
			}
			int index = hash(obj) % spine.length;
			for (int i = spine[index]; i >= 0; i = next[i]) {
				if (objs[i] == obj) {
					return i;
				}
			}
			return -1;
		}

		
		/**
		 * Clear.
		 */
		void clear() {
			Arrays.fill(spine, -1);
			Arrays.fill(objs, 0, size, null);
			size = 0;
		}

		
		/**
		 * Insert.
		 *
		 * @param obj the obj
		 * @param handle the handle
		 */
		private void insert(Object obj, int handle) {
			int index = hash(obj) % spine.length;
			objs[handle] = obj;
			next[handle] = spine[index];
			spine[index] = handle;
		}

		
		/**
		 * Grow spine.
		 */
		private void growSpine() {
			spine = new int[(spine.length << 1) + 1];
			threshold = (int) (spine.length * loadFactor);
			Arrays.fill(spine, -1);
			for (int i = 0; i < size; i++) {
				insert(objs[i], i);
			}
		}

		
		/**
		 * Grow entries.
		 */
		private void growEntries() {
			int newLength = (next.length << 1) + 1;
			int[] newNext = new int[newLength];
			System.arraycopy(next, 0, newNext, 0, size);
			next = newNext;

			Object[] newObjs = new Object[newLength];
			System.arraycopy(objs, 0, newObjs, 0, size);
			objs = newObjs;
		}

		
		/**
		 * Hash.
		 *
		 * @param obj the obj
		 * @return the int
		 */
		private int hash(Object obj) {
			return System.identityHashCode(obj) & 0x7FFFFFFF;
		}
	}
}
