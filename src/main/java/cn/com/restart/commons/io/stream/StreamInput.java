/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons StreamInput.java 2012-3-29 15:15:08 l.xue.nong$$
 */


package cn.com.restart.commons.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.restart.commons.BytesHolder;
import cn.com.restart.commons.Nullable;


/**
 * The Class StreamInput.
 *
 * @author l.xue.nong
 */
public abstract class StreamInput extends InputStream {

	
	/**
	 * Read byte.
	 *
	 * @return the byte
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract byte readByte() throws IOException;

	
	/**
	 * Read bytes.
	 *
	 * @param b the b
	 * @param offset the offset
	 * @param len the len
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void readBytes(byte[] b, int offset, int len) throws IOException;

	
	/**
	 * Read bytes holder.
	 *
	 * @return the bytes holder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BytesHolder readBytesHolder() throws IOException {
		int size = readVInt();
		if (size == 0) {
			return BytesHolder.EMPTY;
		}
		byte[] bytes = new byte[size];
		readBytes(bytes, 0, size);
		return new BytesHolder(bytes, 0, size);
	}

	
	/**
	 * Read bytes reference.
	 *
	 * @return the bytes holder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BytesHolder readBytesReference() throws IOException {
		return readBytesHolder();
	}

	
	/**
	 * Read fully.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readFully(byte[] b) throws IOException {
		readBytes(b, 0, b.length);
	}

	
	/**
	 * Read short.
	 *
	 * @return the short
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public short readShort() throws IOException {
		return (short) (((readByte() & 0xFF) << 8) | (readByte() & 0xFF));
	}

	
	/**
	 * Read int.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int readInt() throws IOException {
		return ((readByte() & 0xFF) << 24) | ((readByte() & 0xFF) << 16) | ((readByte() & 0xFF) << 8)
				| (readByte() & 0xFF);
	}

	
	/**
	 * Read v int.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int readVInt() throws IOException {
		byte b = readByte();
		int i = b & 0x7F;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7F) << 7;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7F) << 14;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7F) << 21;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		assert (b & 0x80) == 0;
		return i | ((b & 0x7F) << 28);
	}

	
	/**
	 * Read long.
	 *
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public long readLong() throws IOException {
		return (((long) readInt()) << 32) | (readInt() & 0xFFFFFFFFL);
	}

	
	/**
	 * Read v long.
	 *
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public long readVLong() throws IOException {
		byte b = readByte();
		long i = b & 0x7FL;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 7;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 14;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 21;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 28;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 35;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 42;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		i |= (b & 0x7FL) << 49;
		if ((b & 0x80) == 0)
			return i;
		b = readByte();
		assert (b & 0x80) == 0;
		return i | ((b & 0x7FL) << 56);
	}

	
	/**
	 * Read optional utf.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Nullable
	public String readOptionalUTF() throws IOException {
		if (readBoolean()) {
			return readUTF();
		}
		return null;
	}

	
	/**
	 * Read utf.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readUTF() throws IOException {
		int charCount = readVInt();
		char[] chars = CachedStreamInput.getCharArray(charCount);
		int c, charIndex = 0;
		while (charIndex < charCount) {
			c = readByte() & 0xff;
			switch (c >> 4) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				chars[charIndex++] = (char) c;
				break;
			case 12:
			case 13:
				chars[charIndex++] = (char) ((c & 0x1F) << 6 | readByte() & 0x3F);
				break;
			case 14:
				chars[charIndex++] = (char) ((c & 0x0F) << 12 | (readByte() & 0x3F) << 6 | (readByte() & 0x3F) << 0);
				break;
			}
		}
		return new String(chars, 0, charCount);
	}

	
	/**
	 * Read float.
	 *
	 * @return the float
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	
	/**
	 * Read double.
	 *
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	
	/**
	 * Read boolean.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#reset()
	 */
	public abstract void reset() throws IOException;

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#close()
	 */
	public abstract void close() throws IOException;

	
	/**
	 * Read map.
	 *
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public Map<String, Object> readMap() throws IOException {
		return (Map<String, Object>) readGenericValue();
	}

	
	/**
	 * Read generic value.
	 *
	 * @return the object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Nullable
	public Object readGenericValue() throws IOException {
		byte type = readByte();
		if (type == -1) {
			return null;
		} else if (type == 0) {
			return readUTF();
		} else if (type == 1) {
			return readInt();
		} else if (type == 2) {
			return readLong();
		} else if (type == 3) {
			return readFloat();
		} else if (type == 4) {
			return readDouble();
		} else if (type == 5) {
			return readBoolean();
		} else if (type == 6) {
			int bytesSize = readVInt();
			byte[] value = new byte[bytesSize];
			readFully(value);
			return value;
		} else if (type == 7) {
			int size = readVInt();
			List<Object> list = new ArrayList<Object>(size);
			for (int i = 0; i < size; i++) {
				list.add(readGenericValue());
			}
			return list;
		} else if (type == 8) {
			int size = readVInt();
			Object[] list = new Object[size];
			for (int i = 0; i < size; i++) {
				list[i] = readGenericValue();
			}
			return list;
		} else if (type == 9 || type == 10) {
			int size = readVInt();
			Map<String, Object> map;
			if (type == 9) {
				map = new LinkedHashMap<String, Object>(size);
			} else {
				map = new HashMap<String, Object>(size);
			}
			for (int i = 0; i < size; i++) {
				map.put(readUTF(), readGenericValue());
			}
			return map;
		} else if (type == 11) {
			return readByte();
		} else {
			throw new IOException("Can't read unknown type [" + type + "]");
		}
	}
}
