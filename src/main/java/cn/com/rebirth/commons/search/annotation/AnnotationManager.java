/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons AnnotationManager.java 2012-3-31 11:08:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.utils.ExceptionUtils;


/**
 * The Class AnnotationManager.
 *
 * @author l.xue.nong
 */
public class AnnotationManager {

	/** The logger. */
	protected static Logger logger = LoggerFactory.getLogger(AnnotationManager.class.getName());

	// todo: implement EntityListeners for timestamps
	/** The annotation map. */
	private Map<String, AnnotationInfo> annotationMap = new HashMap<String, AnnotationInfo>();

	/** The discriminator map. */
	private Map<String, AnnotationInfo> discriminatorMap = new HashMap<String, AnnotationInfo>();

	/**
	 * Gets the single instance of AnnotationManager.
	 *
	 * @return single instance of AnnotationManager
	 */
	public static AnnotationManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The instance. */
		static AnnotationManager instance = new AnnotationManager();
	}

	/**
	 * Instantiates a new annotation manager.
	 */
	protected AnnotationManager() {
		super();
	}

	/**
	 * Gets the annotation info.
	 *
	 * @param o the o
	 * @return the annotation info
	 */
	public AnnotationInfo getAnnotationInfo(Object o) {
		Class<?> c = getUnwrappedClass(o.getClass());
		AnnotationInfo ai = getAnnotationInfo(c);
		return ai;
	}

	/**
	 * Gets the unwrapped class.
	 *
	 * @param class1 the class1
	 * @return the unwrapped class
	 */
	private Class<?> getUnwrappedClass(Class<? extends Object> class1) {
		Class<?> result = class1;

		while (null != result && result.getClass().getSimpleName().contains("$$EnhancerBy"))
			result = result.getSuperclass();

		return result;
	}

	/**
	 * Gets the annotation map.
	 *
	 * @return the annotation map
	 */
	public Map<String, AnnotationInfo> getAnnotationMap() {
		return annotationMap;
	}

	/**
	 * Gets the annotation info.
	 *
	 * @param c the c
	 * @return the annotation info
	 */
	public AnnotationInfo getAnnotationInfo(Class<?> c) {
		c = stripEnhancerClass(c);
		AnnotationInfo ai = getAnnotationInfo(c.getName());
		if (ai == null) {
			ai = putAnnotationInfo(c);
		}
		return ai;
	}

	/**
	 * Gets the annotation info.
	 *
	 * @param className the class name
	 * @return the annotation info
	 */
	private AnnotationInfo getAnnotationInfo(String className) {
		AnnotationInfo ai = getAnnotationMap().get(className);
		return ai;
	}

	// I could have used the getAnnotationInfo() method but I am not sure how it will evolve.
	// I found that the meaning was more visible using another method.
	/**
	 * Gets the annotation info using full class name.
	 *
	 * @param fullClassName the full class name
	 * @return the annotation info using full class name
	 */
	public AnnotationInfo getAnnotationInfoUsingFullClassName(String fullClassName) {
		AnnotationInfo ai = getAnnotationMap().get(fullClassName);
		return ai;
	}

	/**
	 * Strip enhancer class.
	 *
	 * @param c the c
	 * @return the class
	 */
	public static Class<?> stripEnhancerClass(Class<?> c) {
		String className = c.getName();
		className = stripEnhancerClass(className);
		if (className.equals(c.getName())) {
			// no change, did this to fix groovy issue
			return c;
		} else {
			c = getClass(className, c.getClassLoader());
		}
		return c;
	}

	/**
	 * Strip enhancer class.
	 *
	 * @param className the class name
	 * @return the string
	 */
	public static String stripEnhancerClass(String className) {
		int enhancedIndex = className.indexOf("$$EnhancerByCGLIB");
		if (enhancedIndex != -1) {
			className = className.substring(0, enhancedIndex);
		}
		return className;
	}

	/**
	 * Gets the class.
	 *
	 * @param obClass the ob class
	 * @param classLoader the class loader
	 * @return the class
	 */
	public static Class<?> getClass(String obClass, ClassLoader classLoader) {
		try {
			Class<?> c = null;
			try {
				c = Class.forName(obClass, true, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException e) {
				try {
					c = Class.forName(obClass);
				} catch (ClassNotFoundException e1) {
					//                    e1.printStackTrace();
					//                    System.out.println("THIRD LEVEL CLASS LODER");
					//                    c = obClass.getClass().getClassLoader().loadClass(obClass);
					if (classLoader == null) {
						throw e1;
					} else {
						c = classLoader.loadClass(obClass);
					}
				}
			}
			return c;
		} catch (ClassNotFoundException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Put annotation info.
	 *
	 * @param c the c
	 * @return the annotation info
	 */
	public AnnotationInfo putAnnotationInfo(Class<?> c) {
		AnnotationInfo ai = new AnnotationInfo();
		ai.setClassAnnotations(c.getAnnotations());
		ai.setMainClass(c);
		Class<?> superClass = c;
		Class<?> rootClass = null;
		while ((superClass = superClass.getSuperclass()) != null && superClass != Object.class) {
			putProperties(ai, superClass);
			putMethods(ai, superClass);
			rootClass = superClass;
		}
		if (rootClass != null) {
			ai.setRootClass(rootClass);
		} else {
			ai.setRootClass(c);
		}
		putTableDeclaration(ai, c);
		putProperties(ai, c);
		putMethods(ai, c);
		if (ai.getIdMethod() == null) {
			throw new RuntimeException("No ID method specified for: " + c.getName());
		}

		getAnnotationMap().put(c.getName(), ai);
		return ai;
	}

	/**
	 * Put table declaration.
	 *
	 * @param ai the ai
	 * @param c the c
	 */
	protected void putTableDeclaration(AnnotationInfo ai, Class<?> c) {
		Table table = c.getAnnotation(Table.class);
		if (table != null) {
			ai.setTableName(table.name());
		}
	}

	/**
	 * Put methods.
	 *
	 * @param ai the ai
	 * @param c the c
	 */
	protected void putMethods(AnnotationInfo ai, Class<?> c) {
		Method[] methods = c.getDeclaredMethods();
		for (Method method : methods) {
			//            logger.fine("method=" + method.getName());
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isFinal(modifiers))
				continue;
			String methodName = method.getName();
			if (!methodName.startsWith("get"))
				continue;
			Transient transientM = method.getAnnotation(Transient.class);
			if (transientM != null)
				continue; // we don't save this one
			ai.addGetter(method);
		}
	}

	/**
	 * Put properties.
	 *
	 * @param ai the ai
	 * @param c the c
	 */
	protected void putProperties(AnnotationInfo ai, Class<?> c) {
		for (Field field : c.getDeclaredFields()) {
			parseProperty(ai, c, field);
		}
	}

	/**
	 * Parses the property.
	 *
	 * @param ai the ai
	 * @param c the c
	 * @param field the field
	 */
	private void parseProperty(AnnotationInfo ai, Class<?> c, Field field) {
		int modifiers = field.getModifiers();
		if (!field.isAnnotationPresent(Transient.class) && field.isAnnotationPresent(PKey.class)
				&& !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
			ai.addField(field);
		}
	}

	/**
	 * Gets the annotation info by discriminator.
	 *
	 * @param discriminatorValue the discriminator value
	 * @return the annotation info by discriminator
	 */
	public AnnotationInfo getAnnotationInfoByDiscriminator(String discriminatorValue) {
		return discriminatorMap.get(discriminatorValue);
	}

}
