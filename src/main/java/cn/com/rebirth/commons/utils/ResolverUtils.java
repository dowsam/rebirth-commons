/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ResolverUtils.java 2012-2-18 10:08:07 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ResolverUtils.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class ResolverUtils<T> {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ResolverUtils.class);
	
	/** The matches. */
	private Set<Class<? extends T>> matches = new HashSet<Class<? extends T>>();
	/** The classloader. */
	private ClassLoader classloader;

	/**
	 * Gets the classes.
	 *
	 * @return the classes
	 */
	public Set<Class<? extends T>> getClasses() {
		return matches;
	}

	/**
	 * Gets the class loader.
	 *
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		return classloader == null ? Thread.currentThread().getContextClassLoader() : classloader;
	}

	/**
	 * Sets the class loader.
	 *
	 * @param classloader the new class loader
	 */
	public void setClassLoader(ClassLoader classloader) {
		this.classloader = classloader;
	}

	/**
	 * Find implementations.
	 *
	 * @param parent the parent
	 * @param packageNames the package names
	 */
	public void findImplementations(Class<?> parent, String... packageNames) {
		if (packageNames == null)
			return;

		Matche matche = new AssignableFrom(parent);
		for (String pkg : packageNames) {
			findInPackage(matche, pkg);
		}
	}

	/**
	 * Find suffix.
	 *
	 * @param suffix the suffix
	 * @param packageNames the package names
	 */
	public void findSuffix(String suffix, String... packageNames) {
		if (packageNames == null)
			return;

		Matche matche = new NameEndsWith(suffix);
		for (String pkg : packageNames) {
			findInPackage(matche, pkg);
		}
	}

	/**
	 * Find annotated.
	 *
	 * @param annotation the annotation
	 * @param packageNames the package names
	 */
	public void findAnnotated(Class<? extends Annotation> annotation, String... packageNames) {
		if (packageNames == null)
			return;

		Matche matche = new AnnotatedWith(annotation);
		for (String pkg : packageNames) {
			findInPackage(matche, pkg);
		}
	}

	/**
	 * Find.
	 *
	 * @param matche the matche
	 * @param packageNames the package names
	 */
	public void find(Matche matche, String... packageNames) {
		if (packageNames == null)
			return;

		for (String pkg : packageNames) {
			findInPackage(matche, pkg);
		}
	}

	/**
	 * Find in package.
	 *
	 * @param matche the matche
	 * @param packageName the package name
	 */
	public void findInPackage(Matche matche, String packageName) {
		packageName = packageName.replace('.', '/');
		ClassLoader loader = getClassLoader();
		Enumeration<URL> urls;

		try {
			urls = loader.getResources(packageName);
		} catch (IOException ioe) {
			logger.warn("Could not read package: " + packageName, ioe);
			return;
		}

		while (urls.hasMoreElements()) {
			try {
				String urlPath = urls.nextElement().getFile();
				urlPath = URLDecoder.decode(urlPath, "UTF-8");

				// If it's a file in a directory, trim the stupid file: spec
				if (urlPath.startsWith("file:")) {
					urlPath = urlPath.substring(5);
				}

				// Else it's in a JAR, grab the path to the jar
				if (urlPath.indexOf('!') > 0) {
					urlPath = urlPath.substring(0, urlPath.indexOf('!'));
				}

				logger.info("Scanning for classes in [" + urlPath + "] matching criteria: " + matche);
				File file = new File(urlPath);
				if (file.isDirectory()) {
					loadImplementationsInDirectory(matche, packageName, file);
				} else {
					loadImplementationsInJar(matche, packageName, file);
				}
			} catch (IOException ioe) {
				logger.warn("could not read entries", ioe);
			}
		}
	}

	/**
	 * Load implementations in directory.
	 *
	 * @param matche the matche
	 * @param parent the parent
	 * @param location the location
	 */
	private void loadImplementationsInDirectory(Matche matche, String parent, File location) {
		File[] files = location.listFiles();
		StringBuilder builder = null;

		for (File file : files) {
			builder = new StringBuilder(100);
			builder.append(parent).append("/").append(file.getName());
			String packageOrClass = (parent == null ? file.getName() : builder.toString());

			if (file.isDirectory()) {
				loadImplementationsInDirectory(matche, packageOrClass, file);
			} else if (file.getName().endsWith(".class")) {
				addIfMatching(matche, packageOrClass);
			}
		}
	}

	/**
	 * Load implementations in jar.
	 *
	 * @param matche the matche
	 * @param parent the parent
	 * @param jarfile the jarfile
	 */
	private void loadImplementationsInJar(Matche matche, String parent, File jarfile) {

		try {
			JarEntry entry;
			JarInputStream jarStream = new JarInputStream(new FileInputStream(jarfile));

			while ((entry = jarStream.getNextJarEntry()) != null) {
				String name = entry.getName();
				if (!entry.isDirectory() && name.startsWith(parent) && name.endsWith(".class")) {
					addIfMatching(matche, name);
				}
			}
		} catch (IOException ioe) {
			logger.error("Could not search jar file '" + jarfile + "' for classes matching criteria: " + matche
					+ " due to an IOException", ioe);
		}
	}

	/**
	 * Adds the if matching.
	 *
	 * @param matche the matche
	 * @param fqn the fqn
	 */
	@SuppressWarnings("unchecked")
	protected void addIfMatching(Matche matche, String fqn) {
		try {
			String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
			ClassLoader loader = getClassLoader();
			if (logger.isDebugEnabled()) {
				logger.debug("Checking to see if class " + externalName + " matches criteria [" + matche + "]");
			}

			Class<?> type = loader.loadClass(externalName);
			if (matche.matches(type)) {
				matches.add((Class<? extends T>) type);
			}
		} catch (Throwable t) {
			logger.warn("Could not examine class '" + fqn + "' due to a " + t.getClass().getName() + " with message: "
					+ t.getMessage());
		}
	}

	/**
	 * The Interface Matche.
	 *
	 * @author l.xue.nong
	 */
	public static interface Matche {

		/**
		 * Matches.
		 *
		 * @param type the type
		 * @return true, if successful
		 */
		boolean matches(Class<?> type);
	}

	/**
	 * The Class AssignableFrom.
	 *
	 * @author l.xue.nong
	 */
	public static class AssignableFrom implements Matche {

		/** The parent. */
		private Class<?> parent;

		/**
		 * Instantiates a new assignable from.
		 *
		 * @param parent the parent
		 */
		public AssignableFrom(Class<?> parent) {
			this.parent = parent;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.commons.utils.ResolverUtils.Matche#matches(java.lang.Class)
		 */
		@Override
		public boolean matches(Class<?> type) {
			return type != null && parent.isAssignableFrom(type);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "is assignable to " + parent.getSimpleName();
		}

	}

	/**
	 * The Class NameEndsWith.
	 *
	 * @author l.xue.nong
	 */
	public static class NameEndsWith implements Matche {
		
		/** The suffix. */
		private String suffix;

		/**
		 * Instantiates a new name ends with.
		 *
		 * @param suffix the suffix
		 */
		public NameEndsWith(String suffix) {
			this.suffix = suffix;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.commons.utils.ResolverUtils.Matche#matches(java.lang.Class)
		 */
		@Override
		public boolean matches(Class<?> type) {
			return type != null && type.getName().endsWith(suffix);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ends with the suffix " + suffix;
		}
	}

	/**
	 * The Class AnnotatedWith.
	 *
	 * @author l.xue.nong
	 */
	public static class AnnotatedWith implements Matche {
		
		/** The annotation. */
		private Class<? extends Annotation> annotation;

		/**
		 * Instantiates a new annotated with.
		 *
		 * @param annotation the annotation
		 */
		public AnnotatedWith(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.commons.utils.ResolverUtils.Matche#matches(java.lang.Class)
		 */
		@Override
		public boolean matches(Class<?> type) {
			return type != null && type.isAnnotationPresent(annotation);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "annotated with @" + annotation.getSimpleName();
		}

	}

}
