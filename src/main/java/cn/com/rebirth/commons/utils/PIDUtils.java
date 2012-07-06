/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons PIDUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * The Class PIDUtils.
 *
 * @author l.xue.nong
 */
public abstract class PIDUtils {
	
	/**
	 * Instantiates a new pID utils.
	 */
	private PIDUtils() {
		super();
	}

	/**
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public static String getPID() {
		String pid = System.getProperty("pid");
		if (pid == null) {
			final RuntimeMXBean rtb = ManagementFactory.getRuntimeMXBean();
			final String processName = rtb.getName();
			if (processName.indexOf('@') != -1) {
				pid = processName.substring(0, processName.indexOf('@'));
			} else {
				pid = getPIDFromOS();
			}
			System.setProperty("pid", pid);
		}
		return pid;
	}

	/**
	 * Gets the pID from os.
	 *
	 * @return the pID from os
	 */
	public static String getPIDFromOS() {
		String pid;
		final String[] cmd;
		File tempFile = null;
		Process process = null;
		try {
			try {
				if (!System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("windows")) {
					cmd = new String[] { "/bin/sh", "-c", "echo $$ $PPID" };
				} else {
					tempFile = File.createTempFile("getpids", ".exe");

					pump(PIDUtils.class.getResourceAsStream("getpids.exe"), new FileOutputStream(tempFile), true, true);
					cmd = new String[] { tempFile.getAbsolutePath() };
				}
				process = Runtime.getRuntime().exec(cmd);
				final ByteArrayOutputStream bout = new ByteArrayOutputStream();
				pump(process.getInputStream(), bout, false, true);

				final StringTokenizer stok = new StringTokenizer(bout.toString());
				stok.nextToken(); // this is pid of the process we spanned
				pid = stok.nextToken();

				process.waitFor();
			} finally {
				if (process != null) {
					process.getInputStream().close();
					process.getOutputStream().close();
					process.getErrorStream().close();
					process.destroy();
				}
				if (tempFile != null && !tempFile.delete()) {
					tempFile.deleteOnExit();
				}
			}
		} catch (final InterruptedException e) {
			pid = e.toString();
		} catch (final IOException e) {
			pid = e.toString();
		}
		return pid;
	}

	/**
	 * Pump.
	 *
	 * @param is the is
	 * @param os the os
	 * @param closeIn the close in
	 * @param closeOut the close out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void pump(InputStream is, OutputStream os, boolean closeIn, boolean closeOut) throws IOException {
		try {
			final byte[] bytes = new byte[4 * 1024];
			int length = is.read(bytes);
			while (length != -1) {
				os.write(bytes, 0, length);
				length = is.read(bytes);
			}
		} finally {
			try {
				if (closeIn) {
					is.close();
				}
			} finally {
				if (closeOut) {
					os.close();
				}
			}
		}
	}
}
