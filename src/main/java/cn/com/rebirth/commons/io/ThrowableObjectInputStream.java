/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ThrowableObjectInputStream.java 2012-7-6 10:23:47 l.xue.nong$$
 */

package cn.com.rebirth.commons.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

import cn.com.rebirth.commons.Classes;

/**
 * The Class ThrowableObjectInputStream.
 *
 * @author l.xue.nong
 */
public class ThrowableObjectInputStream extends ObjectInputStream {

	/** The class loader. */
	private final ClassLoader classLoader;

	/**
	 * Instantiates a new throwable object input stream.
	 *
	 * @param in the in
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ThrowableObjectInputStream(InputStream in) throws IOException {
		this(in, null);
	}

	/**
	 * Instantiates a new throwable object input stream.
	 *
	 * @param in the in
	 * @param classLoader the class loader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ThrowableObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
		super(in);
		this.classLoader = classLoader;
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#readStreamHeader()
	 */
	@Override
	protected void readStreamHeader() throws IOException, StreamCorruptedException {
		int version = readByte() & 0xFF;
		if (version != STREAM_VERSION) {
			throw new StreamCorruptedException("Unsupported version: " + version);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#readClassDescriptor()
	 */
	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
		int type = read();
		if (type < 0) {
			throw new EOFException();
		}
		switch (type) {
		case ThrowableObjectOutputStream.TYPE_EXCEPTION:
			return ObjectStreamClass.lookup(Exception.class);
		case ThrowableObjectOutputStream.TYPE_STACKTRACEELEMENT:
			return ObjectStreamClass.lookup(StackTraceElement.class);
		case ThrowableObjectOutputStream.TYPE_FAT_DESCRIPTOR:
			return super.readClassDescriptor();
		case ThrowableObjectOutputStream.TYPE_THIN_DESCRIPTOR:
			String className = readUTF();
			Class<?> clazz = loadClass(className);
			return ObjectStreamClass.lookup(clazz);
		default:
			throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		String className = desc.getName();
		try {
			return loadClass(className);
		} catch (ClassNotFoundException ex) {
			return super.resolveClass(desc);
		}
	}

	/**
	 * Load class.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	protected Class<?> loadClass(String className) throws ClassNotFoundException {
		Class<?> clazz;
		ClassLoader classLoader = this.classLoader;
		if (classLoader == null) {
			classLoader = Classes.getDefaultClassLoader();
		}

		if (classLoader != null) {
			clazz = classLoader.loadClass(className);
		} else {
			clazz = Class.forName(className);
		}
		return clazz;
	}
}
