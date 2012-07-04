/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons AnnotationInfo.java 2012-2-16 17:43:28 l.xue.nong$$
 */
package cn.com.restart.commons.search.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AnnotationInfo.
 *
 * @author l.xue.nong
 */
public class AnnotationInfo {

	/** The class annotations. */
	private Annotation[] classAnnotations;

	/** The id method. */
	private AbstractSearchProperty idMethod;

	/** The properties. */
	private Map<String, AbstractSearchProperty> properties = new HashMap<String, AbstractSearchProperty>();

	/** The table name. */
	private String tableName;
	/** The root class. */
	private Class<?> rootClass;

	/** The main class. */
	private Class<?> mainClass;

	/**
	 * Adds the getter.
	 *
	 * @param method the method
	 * @return the abstract search property
	 */
	public AbstractSearchProperty addGetter(Method method) {
		SearchAnnotationMethod persistentMethod = new SearchAnnotationMethod(method);
		// if we already have an accessor in the list, don't overwrite it
		if (properties.containsKey(persistentMethod.getFieldName()))
			return properties.get(persistentMethod.getFieldName());
		properties.put(persistentMethod.getFieldName(), persistentMethod);
		if (persistentMethod.isId())
			setIdMethod(persistentMethod);
		return persistentMethod;
	}

	/**
	 * Adds the field.
	 *
	 * @param field the field
	 * @return the abstract search property
	 */
	public AbstractSearchProperty addField(Field field) {
		SearchAnnotationField persistentField = new SearchAnnotationField(field);
		// if we already have an accessor in the list, don't overwrite it
		if (properties.containsKey(persistentField.getFieldName()))
			return properties.get(persistentField.getFieldName());
		properties.put(persistentField.getFieldName(), persistentField);
		if (persistentField.isId())
			setIdMethod(persistentField);
		return persistentField;
	}

	/**
	 * Gets the class annotations.
	 *
	 * @return the class annotations
	 */
	public Annotation[] getClassAnnotations() {
		return classAnnotations;
	}

	/**
	 * Sets the class annotations.
	 *
	 * @param classAnnotations the new class annotations
	 */
	public void setClassAnnotations(Annotation[] classAnnotations) {
		this.classAnnotations = classAnnotations;
	}

	/**
	 * Gets the id method.
	 *
	 * @return the id method
	 */
	public AbstractSearchProperty getIdMethod() {
		return idMethod;
	}

	/**
	 * Sets the id method.
	 *
	 * @param idMethod the new id method
	 */
	public void setIdMethod(AbstractSearchProperty idMethod) {
		this.idMethod = idMethod;
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Map<String, AbstractSearchProperty> getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(Map<String, AbstractSearchProperty> properties) {
		this.properties = properties;
	}

	/**
	 * Gets the root class.
	 *
	 * @return the root class
	 */
	public Class<?> getRootClass() {
		return rootClass;
	}

	/**
	 * Sets the root class.
	 *
	 * @param rootClass the new root class
	 */
	public void setRootClass(Class<?> rootClass) {
		this.rootClass = rootClass;
	}

	/**
	 * Gets the main class.
	 *
	 * @return the main class
	 */
	public Class<?> getMainClass() {
		return mainClass;
	}

	/**
	 * Sets the main class.
	 *
	 * @param mainClass the new main class
	 */
	public void setMainClass(Class<?> mainClass) {
		this.mainClass = mainClass;
	}

	/**
	 * Gets the table name.
	 *
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Sets the table name.
	 *
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
