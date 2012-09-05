/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons CachedStreamOutput.java 2012-7-6 10:23:41 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class CachedStreamOutput.
 *
 * @author l.xue.nong
 */
public class CachedStreamOutput {

	/**
	 * New entry.
	 *
	 * @return the entry
	 */
	private static Entry newEntry() {
		BytesStreamOutput bytes = new BytesStreamOutput();
		HandlesStreamOutput handles = new HandlesStreamOutput(bytes);
		return new Entry(bytes, handles);
	}

	/**
	 * The Class Entry.
	 *
	 * @author l.xue.nong
	 */
	public static class Entry {

		/** The bytes. */
		private final BytesStreamOutput bytes;

		/** The handles. */
		private final HandlesStreamOutput handles;

		/** The lzf. */
		private LZFStreamOutput lzf;

		/**
		 * Instantiates a new entry.
		 *
		 * @param bytes the bytes
		 * @param handles the handles
		 */
		Entry(BytesStreamOutput bytes, HandlesStreamOutput handles) {
			this.bytes = bytes;
			this.handles = handles;
		}

		/**
		 * Lzf.
		 *
		 * @return the lZF stream output
		 */
		private LZFStreamOutput lzf() {
			if (lzf == null) {
				lzf = new LZFStreamOutput(bytes, true);
			}
			return lzf;
		}

		/**
		 * Bytes.
		 *
		 * @return the bytes stream output
		 */
		public BytesStreamOutput bytes() {
			return bytes;
		}

		/**
		 * Cached bytes.
		 *
		 * @return the bytes stream output
		 */
		public BytesStreamOutput cachedBytes() {
			bytes.reset();
			return bytes;
		}

		/**
		 * Cached lzf bytes.
		 *
		 * @return the lZF stream output
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public LZFStreamOutput cachedLZFBytes() throws IOException {
			LZFStreamOutput lzf = lzf();
			lzf.reset();
			return lzf;
		}

		/**
		 * Cached handles lzf bytes.
		 *
		 * @return the handles stream output
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public HandlesStreamOutput cachedHandlesLzfBytes() throws IOException {
			LZFStreamOutput lzf = lzf();
			handles.reset(lzf);
			return handles;
		}

		/**
		 * Cached handles bytes.
		 *
		 * @return the handles stream output
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public HandlesStreamOutput cachedHandlesBytes() throws IOException {
			handles.reset(bytes);
			return handles;
		}
	}

	/**
	 * The Class SoftWrapper.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	static class SoftWrapper<T> {

		/** The ref. */
		private SoftReference<T> ref;

		/**
		 * Instantiates a new soft wrapper.
		 */
		public SoftWrapper() {
		}

		/**
		 * Sets the.
		 *
		 * @param ref the ref
		 */
		public void set(T ref) {
			this.ref = new SoftReference<T>(ref);
		}

		/**
		 * Gets the.
		 *
		 * @return the t
		 */
		public T get() {
			return ref == null ? null : ref.get();
		}

		/**
		 * Clear.
		 */
		public void clear() {
			ref = null;
		}
	}

	/** The Constant cache. */
	private static final SoftWrapper<Queue<Entry>> cache = new SoftWrapper<Queue<Entry>>();

	/** The Constant counter. */
	private static final AtomicInteger counter = new AtomicInteger();

	/** The bytes limit. */
	public static int BYTES_LIMIT = 1 * 1024 * 1024;

	/** The count limit. */
	public static int COUNT_LIMIT = 100;

	/**
	 * Clear.
	 */
	public static void clear() {
		cache.clear();
	}

	/**
	 * Pop entry.
	 *
	 * @return the entry
	 */
	public static Entry popEntry() {
		Queue<Entry> ref = cache.get();
		if (ref == null) {
			return newEntry();
		}
		Entry entry = ref.poll();
		if (entry == null) {
			return newEntry();
		}
		counter.decrementAndGet();
		return entry;
	}

	/**
	 * Push entry.
	 *
	 * @param entry the entry
	 */
	public static void pushEntry(Entry entry) {
		if (entry.bytes().underlyingBytes().length > BYTES_LIMIT) {
			return;
		}
		Queue<Entry> ref = cache.get();
		if (ref == null) {
			ref = new LinkedBlockingQueue<Entry>();
			counter.set(0);
			cache.set(ref);
		}
		if (counter.incrementAndGet() > COUNT_LIMIT) {
			counter.decrementAndGet();
		} else {
			ref.add(entry);
		}
	}
}
