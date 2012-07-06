/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons VirtualMachineUtils.java 2012-7-6 10:22:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The Class VirtualMachineUtils.
 *
 * @author l.xue.nong
 */
public abstract class VirtualMachineUtils {
	
	/** The enabled. */
	private static boolean enabled = isSupported();
	
	/** The jvm virtual machine. */
	private static Object jvmVirtualMachine;

	/**
	 * Instantiates a new virtual machine utils.
	 */
	private VirtualMachineUtils() {
		super();
	}

	/**
	 * Checks if is supported.
	 *
	 * @return true, if is supported
	 */
	public static boolean isSupported() {
		final String javaVersion = System.getProperty("java.version");
		final String javaVendor = System.getProperty("java.vendor");
		return "1.6".compareTo(javaVersion) < 0
				&& (javaVendor.contains("Sun") || javaVendor.contains("Oracle") || javaVendor.contains("Apple") || isJRockit());
	}

	/**
	 * Checks if is j rockit.
	 *
	 * @return true, if is j rockit
	 */
	public static boolean isJRockit() {
		return System.getProperty("java.vendor").contains("BEA");
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public static synchronized boolean isEnabled() { // NOPMD
		return enabled;
	}

	/**
	 * Gets the jvm virtual machine.
	 *
	 * @return the jvm virtual machine
	 * @throws Exception the exception
	 */
	public static synchronized Object getJvmVirtualMachine() throws Exception { // NOPMD
		if (jvmVirtualMachine == null) {
			final Class<?> virtualMachineClass = findVirtualMachineClass();
			final Method attachMethod = virtualMachineClass.getMethod("attach", String.class);
			final String pid = PIDUtils.getPID();
			try {
				jvmVirtualMachine = invoke(attachMethod, null, pid);
			} finally {
				enabled = jvmVirtualMachine != null;
			}
		}
		return jvmVirtualMachine;
	}

	/**
	 * Find virtual machine class.
	 *
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 * @throws MalformedURLException the malformed url exception
	 */
	private static Class<?> findVirtualMachineClass() throws ClassNotFoundException, MalformedURLException {
		final String virtualMachineClassName = "com.sun.tools.attach.VirtualMachine";
		try {
			return Class.forName(virtualMachineClassName);
		} catch (final ClassNotFoundException e) {
			File file = new File(System.getProperty("java.home"));
			if (file.getName().equalsIgnoreCase("jre")) {
				file = file.getParentFile();
			}
			final String[] defaultToolsLocation = { "lib", "tools.jar" };
			for (final String name : defaultToolsLocation) {
				file = new File(file, name);
			}
			final URL[] urls = { file.toURI().toURL() };
			final ClassLoader cl = URLClassLoader.newInstance(urls);
			return Class.forName(virtualMachineClassName, true, cl);
		}
	}

	/**
	 * Detach.
	 *
	 * @throws Exception the exception
	 */
	public static synchronized void detach() throws Exception { // NOPMD
		if (jvmVirtualMachine != null) {
			final Class<?> virtualMachineClass = jvmVirtualMachine.getClass();
			final Method detachMethod = virtualMachineClass.getMethod("detach");
			jvmVirtualMachine = invoke(detachMethod, jvmVirtualMachine);
			jvmVirtualMachine = null;
		}
	}

	/**
	 * Heap histo.
	 *
	 * @return the input stream
	 * @throws Exception the exception
	 */
	public static InputStream heapHisto() throws Exception { // NOPMD
		if (!isSupported()) {
			throw new IllegalStateException("Memory histogram not supported on this jvm");
		}
		if (!isEnabled()) {
			throw new IllegalStateException("Memory histogram inactive on this jvm");
		}

		try {
			final Class<?> virtualMachineClass = getJvmVirtualMachine().getClass();
			final Method heapHistoMethod = virtualMachineClass.getMethod("heapHisto", Object[].class);
			return (InputStream) invoke(heapHistoMethod, getJvmVirtualMachine(),
					new Object[] { new Object[] { "-all" } });
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException(
					"Memory histogram not supported because the server uses the JRE and not the JDK."
							+ "Check that a JDK is installed and check the environment variable JAVA_HOME or the launch file.",
					e);
		} catch (final Exception e) {
			if ("com.sun.tools.attach.AttachNotSupportedException".equals(e.getClass().getName())) {
				throw new IllegalStateException(
						"Memory histogram not supported because the server uses the JRE and not the JDK."
								+ "Check that a JDK is installed and check the environment variable JAVA_HOME or the launch file.",
						e);
			}
			throw e;
		}
	}

	/**
	 * Invoke.
	 *
	 * @param method the method
	 * @param object the object
	 * @param args the args
	 * @return the object
	 * @throws Exception the exception
	 */
	private static Object invoke(Method method, Object object, Object... args) throws Exception { // NOPMD
		try {
			return method.invoke(object, args);
		} catch (final InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			} else if (e.getCause() instanceof Error) {
				throw (Error) e.getCause();
			} else {
				throw new Exception(e.getCause()); // NOPMD
			}
		}
	}

	// Note : on pourrait aussi charger dynamiquement un agent avec jvmVirtualMachine.loadAgent
	// (en générant un jar temporaire avec ZipFile à partir de
	// getClass().getResourceAsStream(getClass().getName() + ".class"), d'un manifest contenant Agent-Class)
	// pour obtenir la taille sans ses références d'un objet (Instrumentation.getObjectSize)
	// ou pour ajouter de la programmation par aspect à la volée (datasource jdbc, façades...)
	// (addClassTransformer(new org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter())
	// ou loadAgent("aspectjweaver.jar") par exemple).
	// Voir http://java.sun.com/javase/6/docs/api/java/lang/instrument/package-summary.html?is-external=true

	//	private static final String MONITORING_INSTRUMENTATION_KEY = "javamelody.instrumentation";
	//	private static Instrumentation instrumentation;
	//
	//	public void loadAgent(String jarFile) throws AgentLoadException, AgentInitializationException,
	//			IOException {
	//		try {
	//			jvmVirtualMachine.loadAgent(jarFile);
	//		} finally {
	//			instrumentation = (Instrumentation) System.getProperties().get(
	//					MONITORING_INSTRUMENTATION_KEY);
	//			System.getProperties().remove(MONITORING_INSTRUMENTATION_KEY);
	//		}
	//	}
	//
	//	public static void agentmain(@SuppressWarnings("unused") String agentArgs, Instrumentation inst) {
	//		System.getProperties().put(MONITORING_INSTRUMENTATION_KEY, inst);
	//	}
	//
	//	public long getObjectSize(Object objectToSize) {
	//		return instrumentation.getObjectSize(objectToSize);
	//	}
	//
	//	public void addClassTransformer(ClassFileTransformer transformer, boolean canRetransform) {
	//		instrumentation.addTransformer(transformer, canRetransform);
	//	}
}
