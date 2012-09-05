/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ClassUtils.java 2012-8-23 13:20:45 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class ClassUtils.
 *
 * @author l.xue.nong
 */
public class ClassUtils {

	/** The Constant PROTOCOL_FILE. */
	private static final String PROTOCOL_FILE = "file";
	
	/** The Constant PROTOCOL_JAR. */
	private static final String PROTOCOL_JAR = "jar";

	/** The Constant PREFIX_FILE. */
	private static final String PREFIX_FILE = "file:";

	/** The Constant JAR_URL_SEPERATOR. */
	private static final String JAR_URL_SEPERATOR = "!/";
	
	/** The Constant CLASS_FILE. */
	private static final String CLASS_FILE = ".class";

	/** The Constant log. */
	private final static Log log = LogFactory.getLog(ClassUtils.class);

	/**
	 * Gets the classes.
	 *
	 * @param packageName the package name
	 * @return the classes
	 */
	public static List<Class<?>> getClasses(String packageName) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Enumeration<URL> en = null;
		try {
			en = ClassUtils.class.getClassLoader().getResources(nameToPath(packageName));
			while (en.hasMoreElements()) {
				URL url = en.nextElement();
				if (PROTOCOL_FILE.equals(url.getProtocol())) {
					File root = new File(url.getFile());
					findInDirectory(list, root, root, packageName);
				} else if (PROTOCOL_JAR.equals(url.getProtocol()))
					findInJar(list, getJarFile(url), packageName);
			}
		} catch (IOException e) {
			log.error("无效的包名:" + packageName, e.getCause());
		}
		return list;
	}

	/**
	 * Gets the jar file.
	 *
	 * @param url the url
	 * @return the jar file
	 */
	public static File getJarFile(URL url) {
		String file = url.getFile();
		if (file.startsWith(PREFIX_FILE))
			file = file.substring(PREFIX_FILE.length());
		int end = file.indexOf(JAR_URL_SEPERATOR);
		if (end != (-1))
			file = file.substring(0, end);
		return new File(file);
	}

	/**
	 * Find in directory.
	 *
	 * @param results the results
	 * @param rootDir the root dir
	 * @param dir the dir
	 * @param packageName the package name
	 */
	private static void findInDirectory(List<Class<?>> results, File rootDir, File dir, String packageName) {
		File[] files = dir.listFiles();
		String rootPath = rootDir.getPath();
		for (File file : files)
			if (file.isFile()) {
				String classFileName = file.getPath();
				if (classFileName.endsWith(CLASS_FILE)) {
					String className = classFileName.substring(rootPath.length() - packageName.length(), classFileName
							.length()
							- CLASS_FILE.length());
					try {
						Class<?> clz = Class.forName(pathToName(className));
						results.add(clz);
					} catch (ClassNotFoundException e) {
						log.error("无法获取类:" + className, e.getCause()); // 该错误应该不会出现
					}
				}
			} else if (file.isDirectory())
				findInDirectory(results, rootDir, file, packageName);
	}

	/**
	 * Find in jar.
	 *
	 * @param results the results
	 * @param file the file
	 * @param packageName the package name
	 */
	private static void findInJar(List<Class<?>> results, File file, String packageName) {
		JarFile jarFile = null;
		String packagePath = nameToPath(packageName) + "/";
		try {
			jarFile = new JarFile(file);
			Enumeration<JarEntry> en = jarFile.entries();
			while (en.hasMoreElements()) {
				JarEntry je = en.nextElement();
				String name = je.getName();
				if (name.startsWith(packagePath) && name.endsWith(CLASS_FILE)) {
					String className = name.substring(0, name.length() - CLASS_FILE.length());
					try {
						Class<?> clz = Class.forName(pathToName(className));
						results.add(clz);
					} catch (ClassNotFoundException e) {
						log.error("无法获取类:" + className, e.getCause()); // 该错误应该不会出现
					}
				}
			}
		} catch (IOException e) {
			log.error("无法读取 Jar 文件:" + file.getName(), e);
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * 将类名的字符串形式转换为路径表现形式.
	 *
	 * @param className 类名
	 * @return 路径的字符串
	 */
	private static String nameToPath(String className) {
		return className.replace('.', '/');
	}

	/**
	 * 将路径转换为类的字符串表现形式.
	 *
	 * @param path the path
	 * @return 类名的字符串形式
	 */
	private static String pathToName(String path) {
		return path.replace('/', '.').replace('\\', '.');
	}
	
	/**
	 * Gets the sorted method list.
	 *
	 * @param clazz the clazz
	 * @return the sorted method list
	 */
	public static List<Method> getSortedMethodList(Class<?> clazz){
		List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
		Collections.sort(methods, new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				String lower1 = m1.toString().toLowerCase();
				String lower2 = m2.toString().toLowerCase();
				return lower1.compareTo(lower2);
			}
		});
		
		return methods;
	}
}