/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AbstractSearchProperty.java 2012-8-1 17:23:51 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * The Class AbstractSearchProperty.
 *
 * @author l.xue.nong
 */
public abstract class AbstractSearchProperty {

	/** The element. */
	protected final AnnotatedElement element;

	/** The elements. */
	protected volatile static Map<AnnotatedElement, ElementCallback<?>> elements = Maps.newHashMap();
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Adds the.
	 *
	 * @param annotatedElement the annotated element
	 * @param callback the callback
	 */
	public static void add(AnnotatedElement annotatedElement, ElementCallback<?> callback) {
		synchronized (AbstractSearchProperty.class) {
			elements.put(annotatedElement, callback);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param annotatedElement the annotated element
	 * @return the element callback
	 */
	public static ElementCallback<?> remove(AnnotatedElement annotatedElement) {
		synchronized (AbstractSearchProperty.class) {
			return elements.remove(annotatedElement);
		}
	}

	/**
	 * Instantiates a new abstract search property.
	 *
	 * @param element the element
	 */
	public AbstractSearchProperty(AnnotatedElement element) {
		super();
		this.element = element;
	}

	/**
	 * Gets the getter.
	 *
	 * @return the getter
	 */
	public abstract Method getGetter();

	/**
	 * Gets the setter.
	 *
	 * @return the setter
	 */
	public abstract Method getSetter();

	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	public abstract String getFieldName();

	/**
	 * Checks if is field index.
	 *
	 * @return true, if is field index
	 */
	public boolean isFieldIndex() {
		return execute(new ElementCallback<Boolean>() {

			@Override
			public Boolean doExecute(AnnotatedElement element) {
				return element.isAnnotationPresent(FieldIndex.class);
			}

		});
	}

	/**
	 * Gets the field index.
	 *
	 * @return the field index
	 */
	public FieldIndex getFieldIndex() {
		return execute(new ElementCallback<FieldIndex>() {

			@Override
			public FieldIndex doExecute(AnnotatedElement element) {
				FieldIndex fieldIndex = element.getAnnotation(FieldIndex.class);
				return (FieldIndex) (fieldIndex == null ? FieldIndex.NO_INDEX : fieldIndex);
			}
		});
	}

	/**
	 * Gets the field boost.
	 *
	 * @return the field boost
	 */
	public FieldBoost getFieldBoost() {
		return execute(new ElementCallback<FieldBoost>() {

			@Override
			public FieldBoost doExecute(AnnotatedElement element) {
				return element.getAnnotation(FieldBoost.class);
			}
		});
	}

	/**
	 * Checks if is field store.
	 *
	 * @return true, if is field store
	 */
	public boolean isFieldStore() {
		return execute(new ElementCallback<Boolean>() {

			@Override
			public Boolean doExecute(AnnotatedElement element) {
				return element.isAnnotationPresent(FieldStore.class);
			}
		});
	}

	/**
	 * Gets the field store.
	 *
	 * @return the field store
	 */
	public FieldStore getFieldStore() {
		return execute(new ElementCallback<FieldStore>() {

			@Override
			public FieldStore doExecute(AnnotatedElement element) {
				FieldStore fieldStore = element.getAnnotation(FieldStore.class);
				return (FieldStore) (fieldStore == null ? FieldStore.NO : fieldStore);
			}

		});
	}

	/**
	 * Gets the field analyzer.
	 *
	 * @return the field analyzer
	 */
	public FieldAnalyzer getFieldAnalyzer() {
		return execute(new ElementCallback<FieldAnalyzer>() {

			@Override
			public FieldAnalyzer doExecute(AnnotatedElement element) {
				return element.getAnnotation(FieldAnalyzer.class);
			}

		});
	}

	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public AnnotatedElement getElement() {
		return element;
	}

	/**
	 * Checks if is skip html escape.
	 *
	 * @return true, if is skip html escape
	 */
	public boolean isSkipHTMLEscape() {
		return execute(new ElementCallback<Boolean>() {

			@Override
			public Boolean doExecute(AnnotatedElement element) {
				return element.isAnnotationPresent(SkipHTMLEscape.class);
			}
		});
	}

	/**
	 * Gets the property.
	 *
	 * @param target the target
	 * @return the property
	 */
	public Object getProperty(Object target) {
		try {
			return getGetter().invoke(target);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Sets the property.
	 *
	 * @param target the target
	 * @param value the value
	 */
	public void setProperty(Object target, Object value) {
		try {
			getSetter().invoke(target, value);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Gets the column name.
	 *
	 * @return the column name
	 */
	public String getColumnName() {
		Boolean b = execute(new ElementCallback<Boolean>() {
			@Override
			public Boolean doExecute(AnnotatedElement element) {
				return element.isAnnotationPresent(Column.class);
			}
		});
		if (b) {
			Column column = element.getAnnotation(Column.class);
			if (column.name() != null && !column.name().trim().isEmpty()) {
				String columnName = column.name();
				return columnName;
			}
		}
		return StringUtils.uncapitalize(getFieldName());
	}

	/**
	 * Gets the property class.
	 *
	 * @return the property class
	 */
	public Class<?> getPropertyClass() {
		Class<?> clazz = getGetter().getReturnType();
		if (Collection.class.isAssignableFrom(clazz)) {
			return (Class<?>) ((ParameterizedType) getGetter().getGenericReturnType()).getActualTypeArguments()[0];
		}
		return clazz;
	}

	/**
	 * Gets the raw class.
	 *
	 * @return the raw class
	 */
	public Class<?> getRawClass() {
		return getGetter().getReturnType();
	}

	/**
	 * Checks if is id.
	 *
	 * @return true, if is id
	 */
	public boolean isId() {
		return execute(new ElementCallback<Boolean>() {

			@Override
			public Boolean doExecute(AnnotatedElement element) {
				return element.isAnnotationPresent(PKey.class) || element.isAnnotationPresent(Id.class);
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFieldName();
	}

	/**
	 * Execute.
	 *
	 * @param <T> the generic type
	 * @param action the action
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T execute(ElementCallback<T> action) {
		ElementCallback<?> callback = elements.get(element);
		if (callback != null) {
			return (T) callback.doExecute(element);
		}
		return action.doExecute(element);
	}

	/**
	 * The Interface ElementCallback.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	public static interface ElementCallback<T> {

		/**
		 * Do execute.
		 *
		 * @param element the element
		 * @return the t
		 */
		public T doExecute(AnnotatedElement element);
	}

}
