/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PortsRange.java 2012-7-6 10:23:53 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import gnu.trove.list.array.TIntArrayList;

import java.util.StringTokenizer;


/**
 * The Class PortsRange.
 *
 * @author l.xue.nong
 */
public class PortsRange {

	
	/** The port range. */
	private final String portRange;

	
	/**
	 * Instantiates a new ports range.
	 *
	 * @param portRange the port range
	 */
	public PortsRange(String portRange) {
		this.portRange = portRange;
	}

	
	/**
	 * Ports.
	 *
	 * @return the int[]
	 * @throws NumberFormatException the number format exception
	 */
	public int[] ports() throws NumberFormatException {
		final TIntArrayList ports = new TIntArrayList();
		iterate(new PortCallback() {
			@Override
			public boolean onPortNumber(int portNumber) {
				ports.add(portNumber);
				return false;
			}
		});
		return ports.toArray(new int[ports.size()]);
	}

	
	/**
	 * Iterate.
	 *
	 * @param callback the callback
	 * @return true, if successful
	 * @throws NumberFormatException the number format exception
	 */
	public boolean iterate(PortCallback callback) throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(portRange, ",");
		boolean success = false;
		while (st.hasMoreTokens() && !success) {
			String portToken = st.nextToken().trim();
			int index = portToken.indexOf('-');
			if (index == -1) {
				int portNumber = Integer.parseInt(portToken.trim());
				success = callback.onPortNumber(portNumber);
				if (success) {
					break;
				}
			} else {
				int startPort = Integer.parseInt(portToken.substring(0, index).trim());
				int endPort = Integer.parseInt(portToken.substring(index + 1).trim());
				if (endPort < startPort) {
					throw new IllegalArgumentException("Start port [" + startPort + "] must be greater than end port ["
							+ endPort + "]");
				}
				for (int i = startPort; i <= endPort; i++) {
					success = callback.onPortNumber(i);
					if (success) {
						break;
					}
				}
			}
		}
		return success;
	}

	
	/**
	 * The Interface PortCallback.
	 *
	 * @author l.xue.nong
	 */
	public static interface PortCallback {
		
		
		/**
		 * On port number.
		 *
		 * @param portNumber the port number
		 * @return true, if successful
		 */
		boolean onPortNumber(int portNumber);
	}
}
