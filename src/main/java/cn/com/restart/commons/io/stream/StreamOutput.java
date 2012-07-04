/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons StreamOutput.java 2012-3-29 15:15:09 l.xue.nong$$
 */


package cn.com.restart.commons.io.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.restart.commons.BytesHolder;
import cn.com.restart.commons.Nullable;



/**
 * The Class StreamOutput.
 *
 * @author l.xue.nong
 */
public abstract class StreamOutput extends OutputStream {

	
	/**
	 * Write byte.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void writeByte(byte b) throws IOException;

	
	/**
	 * Write bytes.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBytes(byte[] b) throws IOException {
		writeBytes(b, 0, b.length);
	}

	
	/**
	 * Write bytes.
	 *
	 * @param b the b
	 * @param length the length
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBytes(byte[] b, int length) throws IOException {
		writeBytes(b, 0, length);
	}

	
	/**
	 * Write bytes.
	 *
	 * @param b the b
	 * @param offset the offset
	 * @param length the length
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void writeBytes(byte[] b, int offset, int length) throws IOException;

	
	/**
	 * Write bytes holder.
	 *
	 * @param bytes the bytes
	 * @param offset the offset
	 * @param length the length
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBytesHolder(byte[] bytes, int offset, int length) throws IOException {
		writeVInt(length);
		writeBytes(bytes, offset, length);
	}

	
	/**
	 * Write bytes holder.
	 *
	 * @param bytes the bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBytesHolder(@Nullable BytesHolder bytes) throws IOException {
		if (bytes == null) {
			writeVInt(0);
		} else {
			writeVInt(bytes.length());
			writeBytes(bytes.bytes(), bytes.offset(), bytes.length());
		}
	}

	
	/**
	 * Write short.
	 *
	 * @param v the v
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final void writeShort(short v) throws IOException {
		writeByte((byte) (v >> 8));
		writeByte((byte) v);
	}

	
	/**
	 * Write int.
	 *
	 * @param i the i
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeInt(int i) throws IOException {
		writeByte((byte) (i >> 24));
		writeByte((byte) (i >> 16));
		writeByte((byte) (i >> 8));
		writeByte((byte) i);
	}

	
	/**
	 * Write v int.
	 *
	 * @param i the i
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeVInt(int i) throws IOException {
		while ((i & ~0x7F) != 0) {
			writeByte((byte) ((i & 0x7f) | 0x80));
			i >>>= 7;
		}
		writeByte((byte) i);
	}

	
	/**
	 * Write long.
	 *
	 * @param i the i
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeLong(long i) throws IOException {
		writeInt((int) (i >> 32));
		writeInt((int) i);
	}

	
	/**
	 * Write v long.
	 *
	 * @param i the i
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeVLong(long i) throws IOException {
		while ((i & ~0x7F) != 0) {
			writeByte((byte) ((i & 0x7f) | 0x80));
			i >>>= 7;
		}
		writeByte((byte) i);
	}

	
	/**
	 * Write optional utf.
	 *
	 * @param str the str
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeOptionalUTF(@Nullable String str) throws IOException {
		if (str == null) {
			writeBoolean(false);
		} else {
			writeBoolean(true);
			writeUTF(str);
		}
	}

	
	/**
	 * Write utf.
	 *
	 * @param str the str
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeUTF(String str) throws IOException {
		int charCount = str.length();
		writeVInt(charCount);
		int c;
		for (int i = 0; i < charCount; i++) {
			c = str.charAt(i);
			if (c <= 0x007F) {
				writeByte((byte) c);
			} else if (c > 0x07FF) {
				writeByte((byte) (0xE0 | c >> 12 & 0x0F));
				writeByte((byte) (0x80 | c >> 6 & 0x3F));
				writeByte((byte) (0x80 | c >> 0 & 0x3F));
			} else {
				writeByte((byte) (0xC0 | c >> 6 & 0x1F));
				writeByte((byte) (0x80 | c >> 0 & 0x3F));
			}
		}
	}

	
	/**
	 * Write float.
	 *
	 * @param v the v
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	
	/**
	 * Write double.
	 *
	 * @param v the v
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	
	/** The ZERO. */
	private static byte ZERO = 0;

	
	/** The ONE. */
	private static byte ONE = 1;

	
	/**
	 * Write boolean.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeBoolean(boolean b) throws IOException {
		writeByte(b ? ONE : ZERO);
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	public abstract void flush() throws IOException;

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	public abstract void close() throws IOException;

	
	/**
	 * Reset.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void reset() throws IOException;

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		writeByte((byte) b);
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		writeBytes(b, off, len);
	}

	
	/**
	 * Write map.
	 *
	 * @param map the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeMap(@Nullable Map<String, Object> map) throws IOException {
		writeGenericValue(map);
	}

	
	/**
	 * Write generic value.
	 *
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public void writeGenericValue(@Nullable Object value) throws IOException {
		if (value == null) {
			writeByte((byte) -1);
			return;
		}
		Class<?> type = value.getClass();
		if (type == String.class) {
			writeByte((byte) 0);
			writeUTF((String) value);
		} else if (type == Integer.class) {
			writeByte((byte) 1);
			writeInt((Integer) value);
		} else if (type == Long.class) {
			writeByte((byte) 2);
			writeLong((Long) value);
		} else if (type == Float.class) {
			writeByte((byte) 3);
			writeFloat((Float) value);
		} else if (type == Double.class) {
			writeByte((byte) 4);
			writeDouble((Double) value);
		} else if (type == Boolean.class) {
			writeByte((byte) 5);
			writeBoolean((Boolean) value);
		} else if (type == byte[].class) {
			writeByte((byte) 6);
			writeVInt(((byte[]) value).length);
			writeBytes(((byte[]) value));
		} else if (value instanceof List) {
			writeByte((byte) 7);
			List<?> list = (List<?>) value;
			writeVInt(list.size());
			for (Object o : list) {
				writeGenericValue(o);
			}
		} else if (value instanceof Object[]) {
			writeByte((byte) 8);
			Object[] list = (Object[]) value;
			writeVInt(list.length);
			for (Object o : list) {
				writeGenericValue(o);
			}
		} else if (value instanceof Map) {
			if (value instanceof LinkedHashMap) {
				writeByte((byte) 9);
			} else {
				writeByte((byte) 10);
			}
			Map<String, Object> map = (Map<String, Object>) value;
			writeVInt(map.size());
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				writeUTF(entry.getKey());
				writeGenericValue(entry.getValue());
			}
		} else if (type == Byte.class) {
			writeByte((byte) 11);
			writeByte((Byte) value);
		} else {
			throw new IOException("Can't write type [" + type + "]");
		}
	}
}
