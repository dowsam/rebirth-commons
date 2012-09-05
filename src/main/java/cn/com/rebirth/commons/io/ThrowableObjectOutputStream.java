/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ThrowableObjectOutputStream.java 2012-7-6 10:23:48 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

/**
 * The Class ThrowableObjectOutputStream.
 *
 * @author l.xue.nong
 */
public class ThrowableObjectOutputStream extends ObjectOutputStream {

	/** The Constant TYPE_FAT_DESCRIPTOR. */
	static final int TYPE_FAT_DESCRIPTOR = 0;

	/** The Constant TYPE_THIN_DESCRIPTOR. */
	static final int TYPE_THIN_DESCRIPTOR = 1;

	/** The Constant EXCEPTION_CLASSNAME. */
	private static final String EXCEPTION_CLASSNAME = Exception.class.getName();

	/** The Constant TYPE_EXCEPTION. */
	static final int TYPE_EXCEPTION = 2;

	/** The Constant STACKTRACEELEMENT_CLASSNAME. */
	private static final String STACKTRACEELEMENT_CLASSNAME = StackTraceElement.class.getName();

	/** The Constant TYPE_STACKTRACEELEMENT. */
	static final int TYPE_STACKTRACEELEMENT = 3;

	/**
	 * Instantiates a new throwable object output stream.
	 *
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ThrowableObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectOutputStream#writeStreamHeader()
	 */
	@Override
	protected void writeStreamHeader() throws IOException {
		writeByte(STREAM_VERSION);
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectOutputStream#writeClassDescriptor(java.io.ObjectStreamClass)
	 */
	@Override
	protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
		if (desc.getName().equals(EXCEPTION_CLASSNAME)) {
			write(TYPE_EXCEPTION);
		} else if (desc.getName().equals(STACKTRACEELEMENT_CLASSNAME)) {
			write(TYPE_STACKTRACEELEMENT);
		} else {
			Class<?> clazz = desc.forClass();
			if (clazz.isPrimitive() || clazz.isArray()) {
				write(TYPE_FAT_DESCRIPTOR);
				super.writeClassDescriptor(desc);
			} else {
				write(TYPE_THIN_DESCRIPTOR);
				writeUTF(desc.getName());
			}
		}
	}
}
