/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons NetworkUtils.java 2012-7-17 13:25:25 l.xue.nong$$
 */

package cn.com.rebirth.commons.utils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.exception.RebirthIllegalStateException;
import cn.com.rebirth.commons.utils.OsUtils;

import com.google.common.collect.Lists;

/**
 * The Class NetworkUtils.
 *
 * @author l.xue.nong
 */
public abstract class NetworkUtils {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

	/**
	 * The Enum StackType.
	 *
	 * @author l.xue.nong
	 */
	public static enum StackType {

		/** The I pv4. */
		IPv4,

		/** The I pv6. */
		IPv6,

		/** The Unknown. */
		Unknown
	}

	/** The Constant IPv4_SETTING. */
	public static final String IPv4_SETTING = "java.net.preferIPv4Stack";

	/** The Constant IPv6_SETTING. */
	public static final String IPv6_SETTING = "java.net.preferIPv6Addresses";

	/** The Constant NON_LOOPBACK_ADDRESS. */
	public static final String NON_LOOPBACK_ADDRESS = "non_loopback_address";

	/** The Constant localAddress. */
	private final static InetAddress localAddress;

	static {
		InetAddress localAddressX = null;
		try {
			localAddressX = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			logger.trace("Failed to find local host", e);
		}
		localAddress = localAddressX;
	}

	/**
	 * Default reuse address.
	 *
	 * @return the boolean
	 */
	public static Boolean defaultReuseAddress() {
		return OsUtils.WINDOWS ? null : true;
	}

	/**
	 * Checks if is i pv4.
	 *
	 * @return true, if is i pv4
	 */
	public static boolean isIPv4() {
		return System.getProperty("java.net.preferIPv4Stack") != null
				&& System.getProperty("java.net.preferIPv4Stack").equals("true");
	}

	/**
	 * Gets the i pv4 localhost.
	 *
	 * @return the i pv4 localhost
	 * @throws UnknownHostException the unknown host exception
	 */
	public static InetAddress getIPv4Localhost() throws UnknownHostException {
		return getLocalhost(StackType.IPv4);
	}

	/**
	 * Gets the i pv6 localhost.
	 *
	 * @return the i pv6 localhost
	 * @throws UnknownHostException the unknown host exception
	 */
	public static InetAddress getIPv6Localhost() throws UnknownHostException {
		return getLocalhost(StackType.IPv6);
	}

	/**
	 * Gets the local address.
	 *
	 * @return the local address
	 */
	public static InetAddress getLocalAddress() {
		return localAddress;
	}

	/**
	 * Gets the localhost.
	 *
	 * @param ip_version the ip_version
	 * @return the localhost
	 * @throws UnknownHostException the unknown host exception
	 */
	public static InetAddress getLocalhost(StackType ip_version) throws UnknownHostException {
		if (ip_version == StackType.IPv4)
			return InetAddress.getByName("127.0.0.1");
		else
			return InetAddress.getByName("::1");
	}

	/**
	 * Can bind to mcast address.
	 *
	 * @return true, if successful
	 */
	public static boolean canBindToMcastAddress() {
		return OsUtils.LINUX || OsUtils.SOLARIS || OsUtils.HP;
	}

	/**
	 * Gets the first non loopback address.
	 *
	 * @param ip_version the ip_version
	 * @return the first non loopback address
	 * @throws SocketException the socket exception
	 */
	public static InetAddress getFirstNonLoopbackAddress(StackType ip_version) throws SocketException {
		InetAddress address = null;
		Enumeration<NetworkInterface> intfs = NetworkInterface.getNetworkInterfaces();
		List<NetworkInterface> intfsList = Lists.newArrayList();
		while (intfs.hasMoreElements()) {
			intfsList.add((NetworkInterface) intfs.nextElement());
		}
		try {
			final Method getIndexMethod = NetworkInterface.class.getDeclaredMethod("getIndex");
			getIndexMethod.setAccessible(true);

			Collections.sort(intfsList, new Comparator<NetworkInterface>() {
				@Override
				public int compare(NetworkInterface o1, NetworkInterface o2) {
					try {
						return ((Integer) getIndexMethod.invoke(o1)).intValue()
								- ((Integer) getIndexMethod.invoke(o2)).intValue();
					} catch (Exception e) {
						throw new RebirthIllegalStateException("failed to fetch index of network interface");
					}
				}
			});
		} catch (Exception e) {

		}

		for (NetworkInterface intf : intfsList) {
			try {
				if (!intf.isUp() || intf.isLoopback())
					continue;
			} catch (Exception e) {

				continue;
			}
			address = getFirstNonLoopbackAddress(intf, ip_version);
			if (address != null) {
				return address;
			}
		}

		return null;
	}

	/**
	 * Gets the first non loopback address.
	 *
	 * @param intf the intf
	 * @param ipVersion the ip version
	 * @return the first non loopback address
	 * @throws SocketException the socket exception
	 */
	public static InetAddress getFirstNonLoopbackAddress(NetworkInterface intf, StackType ipVersion)
			throws SocketException {
		if (intf == null)
			throw new IllegalArgumentException("Network interface pointer is null");

		for (Enumeration<InetAddress> addresses = intf.getInetAddresses(); addresses.hasMoreElements();) {
			InetAddress address = (InetAddress) addresses.nextElement();
			if (!address.isLoopbackAddress()) {
				if ((address instanceof Inet4Address && ipVersion == StackType.IPv4)
						|| (address instanceof Inet6Address && ipVersion == StackType.IPv6))
					return address;
			}
		}
		return null;
	}

	/**
	 * Interface has ip addresses.
	 *
	 * @param intf the intf
	 * @param ipVersion the ip version
	 * @return true, if successful
	 * @throws SocketException the socket exception
	 * @throws UnknownHostException the unknown host exception
	 */
	public static boolean interfaceHasIPAddresses(NetworkInterface intf, StackType ipVersion) throws SocketException,
			UnknownHostException {
		boolean supportsVersion = false;
		if (intf != null) {

			Enumeration<InetAddress> addresses = intf.getInetAddresses();
			while (addresses != null && addresses.hasMoreElements()) {

				InetAddress address = (InetAddress) addresses.nextElement();

				if ((address instanceof Inet4Address && (ipVersion == StackType.IPv4))
						|| (address instanceof Inet6Address && (ipVersion == StackType.IPv6))) {
					supportsVersion = true;
					break;
				}
			}
		} else {
			throw new UnknownHostException("network interface not found");
		}
		return supportsVersion;
	}

	/**
	 * Gets the ip stack type.
	 *
	 * @return the ip stack type
	 */
	public static StackType getIpStackType() {
		boolean isIPv4StackAvailable = isStackAvailable(true);
		boolean isIPv6StackAvailable = isStackAvailable(false);

		if (isIPv4StackAvailable && !isIPv6StackAvailable) {
			return StackType.IPv4;
		}

		else if (isIPv6StackAvailable && !isIPv4StackAvailable) {
			return StackType.IPv6;
		}

		else if (isIPv4StackAvailable && isIPv6StackAvailable) {

			if (Boolean.getBoolean(IPv4_SETTING))
				return StackType.IPv4;
			if (Boolean.getBoolean(IPv6_SETTING))
				return StackType.IPv6;
			return StackType.IPv6;
		}
		return StackType.Unknown;
	}

	/**
	 * Checks if is stack available.
	 *
	 * @param ipv4 the ipv4
	 * @return true, if is stack available
	 */
	public static boolean isStackAvailable(boolean ipv4) {
		Collection<InetAddress> allAddrs = getAllAvailableAddresses();
		for (InetAddress addr : allAddrs)
			if (ipv4 && addr instanceof Inet4Address || (!ipv4 && addr instanceof Inet6Address))
				return true;
		return false;
	}

	/**
	 * Gets the all available interfaces.
	 *
	 * @return the all available interfaces
	 * @throws SocketException the socket exception
	 */
	public static List<NetworkInterface> getAllAvailableInterfaces() throws SocketException {
		List<NetworkInterface> allInterfaces = new ArrayList<NetworkInterface>();
		for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
				.hasMoreElements();) {
			NetworkInterface intf = interfaces.nextElement();
			allInterfaces.add(intf);

			Enumeration<NetworkInterface> subInterfaces = intf.getSubInterfaces();
			if (subInterfaces != null && subInterfaces.hasMoreElements()) {
				while (subInterfaces.hasMoreElements()) {
					allInterfaces.add(subInterfaces.nextElement());
				}
			}
		}
		return allInterfaces;
	}

	/**
	 * Gets the all available addresses.
	 *
	 * @return the all available addresses
	 */
	public static Collection<InetAddress> getAllAvailableAddresses() {
		Set<InetAddress> retval = new HashSet<InetAddress>();
		Enumeration<NetworkInterface> en;

		try {
			en = NetworkInterface.getNetworkInterfaces();
			if (en == null)
				return retval;
			while (en.hasMoreElements()) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				Enumeration<InetAddress> addrs = intf.getInetAddresses();
				while (addrs.hasMoreElements())
					retval.add(addrs.nextElement());
			}
		} catch (SocketException e) {
			logger.warn("Failed to derive all available interfaces", e);
		}

		return retval;
	}

	/**
	 * Instantiates a new network utils.
	 */
	private NetworkUtils() {

	}
}
