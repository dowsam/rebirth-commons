/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons JdbcWrapperUtils.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * The Class JdbcWrapperUtils.
 *
 * @author l.xue.nong
 */
public abstract class JdbcWrapperUtils {
	
	/** The Constant SPRING_DATASOURCES. */
	private static final Map<String, DataSource> SPRING_DATASOURCES = new LinkedHashMap<String, DataSource>();

	/** The Constant JNDI_DATASOURCES_BACKUP. */
	private static final Map<String, DataSource> JNDI_DATASOURCES_BACKUP = new LinkedHashMap<String, DataSource>();

	/**
	 * Register spring data source.
	 *
	 * @param name the name
	 * @param dataSource the data source
	 */
	public static void registerSpringDataSource(String name, DataSource dataSource) {
		SPRING_DATASOURCES.put(name, dataSource);
	}

	/**
	 * Rebind data source.
	 *
	 * @param servletContext the servlet context
	 * @param jndiName the jndi name
	 * @param dataSource the data source
	 * @param dataSourceProxy the data source proxy
	 * @throws Throwable the throwable
	 */
	public static void rebindDataSource(ServletContext servletContext, String jndiName, DataSource dataSource,
			DataSource dataSourceProxy) throws Throwable {
		final Object lock = changeContextWritable(servletContext, null);
		final InitialContext initialContext = new InitialContext();
		initialContext.rebind(jndiName, dataSourceProxy);
		JNDI_DATASOURCES_BACKUP.put(jndiName, dataSource);
		changeContextWritable(servletContext, lock);
		initialContext.close();
	}

	/**
	 * Rebind initial data sources.
	 *
	 * @param servletContext the servlet context
	 * @throws Throwable the throwable
	 */
	public static void rebindInitialDataSources(ServletContext servletContext) throws Throwable {
		try {
			final InitialContext initialContext = new InitialContext();
			for (final Map.Entry<String, DataSource> entry : JNDI_DATASOURCES_BACKUP.entrySet()) {
				final String jndiName = entry.getKey();
				final DataSource dataSource = entry.getValue();
				final Object lock = changeContextWritable(servletContext, null);
				initialContext.rebind(jndiName, dataSource);
				changeContextWritable(servletContext, lock);
			}
			initialContext.close();
		} finally {
			JNDI_DATASOURCES_BACKUP.clear();
		}
	}

	/**
	 * Gets the jndi and spring data sources.
	 *
	 * @return the jndi and spring data sources
	 * @throws NamingException the naming exception
	 */
	public static Map<String, DataSource> getJndiAndSpringDataSources() throws NamingException {
		Map<String, DataSource> dataSources;
		try {
			dataSources = new LinkedHashMap<String, DataSource>(getJndiDataSources());
		} catch (final NoInitialContextException e) {
			dataSources = new LinkedHashMap<String, DataSource>();
		}
		dataSources.putAll(SPRING_DATASOURCES);
		return dataSources;
	}

	/**
	 * Gets the jndi data sources.
	 *
	 * @return the jndi data sources
	 * @throws NamingException the naming exception
	 */
	public static Map<String, DataSource> getJndiDataSources() throws NamingException {
		final Map<String, DataSource> dataSources = new LinkedHashMap<String, DataSource>(2);
		dataSources.putAll(getJndiDataSourcesAt("java:comp/env/jdbc"));
		// pour jboss sans jboss-env.xml ou sans resource-ref dans web.xml :
		dataSources.putAll(getJndiDataSourcesAt("java:/jdbc"));
		// pour JavaEE 6 :
		// (voir par exemple http://smokeandice.blogspot.com/2009/12/datasourcedefinition-hidden-gem-from.html)
		dataSources.putAll(getJndiDataSourcesAt("java:global/jdbc"));
		// pour WebLogic 10 et WebSphere 7, cf issue 68
		dataSources.putAll(getJndiDataSourcesAt("jdbc"));
		return Collections.unmodifiableMap(dataSources);
	}

	/**
	 * Gets the jndi data sources at.
	 *
	 * @param jndiPrefix the jndi prefix
	 * @return the jndi data sources at
	 * @throws NamingException the naming exception
	 */
	private static Map<String, DataSource> getJndiDataSourcesAt(String jndiPrefix) throws NamingException {
		final InitialContext initialContext = new InitialContext();
		final Map<String, DataSource> dataSources = new LinkedHashMap<String, DataSource>(2);
		try {
			for (final NameClassPair nameClassPair : Collections.list(initialContext.list(jndiPrefix))) {
				// note: il ne suffit pas de tester
				// (DataSource.class.isAssignableFrom(Class.forName(nameClassPair.getClassName())))
				// car nameClassPair.getClassName() vaut "javax.naming.LinkRef" sous jboss 5.1.0.GA
				// par exemple, donc on fait le lookup pour voir
				final String jndiName;
				if (nameClassPair.getName().startsWith("java:")) {
					// pour glassfish v3
					jndiName = nameClassPair.getName();
				} else {
					jndiName = jndiPrefix + '/' + nameClassPair.getName();
				}
				final Object value = initialContext.lookup(jndiName);
				if (value instanceof DataSource) {
					dataSources.put(jndiName, (DataSource) value);
				}
			}
		} catch (final NamingException e) {
			// le préfixe ("comp/env/jdbc", "/jdbc" ou "java:global/jdbc", etc) n'existe pas dans jndi,
			// (dans glassfish 3.0.1, c'est une NamingException et non une NameNotFoundException)
			return dataSources;
		}
		initialContext.close();
		return dataSources;
	}

	/**
	 * Change context writable.
	 *
	 * @param servletContext the servlet context
	 * @param lock the lock
	 * @return the object
	 * @throws NoSuchFieldException the no such field exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws NamingException the naming exception
	 */
	@SuppressWarnings("all")
	// CHECKSTYLE:OFF
	private static Object changeContextWritable(ServletContext servletContext, Object lock)
			throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, NamingException {
		// cette méthode ne peut pas être utilisée avec un simple JdbcDriver
		assert servletContext != null;
		if (servletContext.getServerInfo().contains("Tomcat") && System.getProperty("jonas.name") == null) {
			// on n'exécute cela que si c'est tomcat
			// et si ce n'est pas tomcat dans jonas
			final Field field = Class.forName("org.apache.naming.ContextAccessController").getDeclaredField(
					"readOnlyContexts");
			setFieldAccessible(field);
			@SuppressWarnings("unchecked")
			final Hashtable<String, Object> readOnlyContexts = (Hashtable<String, Object>) field.get(null);
			// la clé dans cette Hashtable est normalement
			// "/Catalina/" + hostName + Parameters.getContextPath(servletContext) ;
			// hostName vaut en général "localhost" (ou autre selon le Host dans server.xml)
			// et contextPath vaut "/myapp" par exemple ;
			// la valeur est un securityToken
			if (lock == null) {
				// on utilise clear et non remove au cas où le host ne soit pas localhost dans server.xml
				// (cf issue 105)
				final Hashtable<String, Object> clone = new Hashtable<String, Object>(readOnlyContexts);
				readOnlyContexts.clear();
				return clone;
			}
			// on remet le contexte not writable comme avant
			@SuppressWarnings("unchecked")
			final Hashtable<String, Object> myLock = (Hashtable<String, Object>) lock;
			readOnlyContexts.putAll(myLock);

			return null;
		} else if (servletContext.getServerInfo().contains("jetty")) {
			// on n'exécute cela que si c'est jetty
			final Context jdbcContext = (Context) new InitialContext().lookup("java:comp");
			final Field field = getAccessibleField(jdbcContext, "_env");
			@SuppressWarnings("unchecked")
			final Hashtable<Object, Object> env = (Hashtable<Object, Object>) field.get(jdbcContext);
			if (lock == null) {
				// on rend le contexte writable
				return env.remove("org.mortbay.jndi.lock");
			}
			// on remet le contexte not writable comme avant
			env.put("org.mortbay.jndi.lock", lock);

			return null;
		}
		return null;
	}

	/**
	 * Gets the field value.
	 *
	 * @param object the object
	 * @param fieldName the field name
	 * @return the field value
	 * @throws IllegalAccessException the illegal access exception
	 */
	public static Object getFieldValue(Object object, String fieldName) throws IllegalAccessException {
		return getAccessibleField(object, fieldName).get(object);
	}

	/**
	 * Sets the field value.
	 *
	 * @param object the object
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IllegalAccessException the illegal access exception
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) throws IllegalAccessException {
		getAccessibleField(object, fieldName).set(object, value);
	}

	/**
	 * Gets the accessible field.
	 *
	 * @param object the object
	 * @param fieldName the field name
	 * @return the accessible field
	 */
	private static Field getAccessibleField(Object object, String fieldName) {
		assert fieldName != null;
		Class<?> classe = object.getClass();
		Field result = null;
		do {
			for (final Field field : classe.getDeclaredFields()) {
				if (fieldName.equals(field.getName())) {
					result = field;
					break;
				}
			}
			classe = classe.getSuperclass();
		} while (result == null && classe != null);

		assert result != null;
		setFieldAccessible(result);
		return result;
	}

	/**
	 * Sets the field accessible.
	 *
	 * @param field the new field accessible
	 */
	private static void setFieldAccessible(final Field field) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() { // pour findbugs
					/** {@inheritDoc} */
					public Object run() {
						field.setAccessible(true);
						return null;
					}
				});
	}
	// CHECKSTYLE:ON
}
