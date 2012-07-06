/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ZipUtils.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * The Class ZipUtils.
 *
 * @author l.xue.nong
 */
public abstract class ZipUtils {
	
	/**
	 * Unzip.
	 *
	 * @param from the from
	 * @param to the to
	 */
	public static void unzip(File from, File to) {
		try {
			ZipFile zipFile = new ZipFile(from);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					new File(to, entry.getName()).mkdir();
					continue;
				}
				File f = new File(to, entry.getName());
				f.getParentFile().mkdirs();
				FileOutputStream os = new FileOutputStream(f);
				IOUtils.copy(zipFile.getInputStream(entry), os);
				os.close();
			}
			zipFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Zip.
	 *
	 * @param directory the directory
	 * @param zipFile the zip file
	 */
	public static void zip(File directory, File zipFile) {
		try {
			FileOutputStream os = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(os);
			zipDirectory(directory, directory, zos);
			zos.close();
			os.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Zip directory.
	 *
	 * @param root the root
	 * @param directory the directory
	 * @param zos the zos
	 * @throws Exception the exception
	 */
	static void zipDirectory(File root, File directory, ZipOutputStream zos) throws Exception {
		for (File item : directory.listFiles()) {
			if (item.isDirectory()) {
				zipDirectory(root, item, zos);
			} else {
				byte[] readBuffer = new byte[2156];
				int bytesIn;
				FileInputStream fis = new FileInputStream(item);
				String path = item.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
				ZipEntry anEntry = new ZipEntry(path);
				zos.putNextEntry(anEntry);
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
				fis.close();
			}
		}
	}
}
