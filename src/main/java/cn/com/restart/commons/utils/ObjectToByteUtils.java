/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-server ObjectToByteUtils.java 2012-3-10 12:55:01 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * The Class ObjectToByteUtils.
 *
 * @author l.xue.nong
 */
public abstract class ObjectToByteUtils {
	
	/**
	 * Gets the object.
	 *
	 * @param bytes the bytes
	 * @return the object
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object obj = oi.readObject();
		bi.close();
		oi.close();
		return obj;
	}

	/**
	 * Gets the byte buffer.
	 *
	 * @param obj the obj
	 * @return the byte buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ByteBuffer getByteBuffer(Object obj) throws IOException {
		byte[] bytes = getBytes(obj);
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		return buff;
	}

	/**
	 * Gets the bytes.
	 *
	 * @param obj the obj
	 * @return the bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] getBytes(Object obj) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(obj);
		out.flush();
		byte[] bytes = bout.toByteArray();
		bout.close();
		out.close();
		return bytes;
	}
}
