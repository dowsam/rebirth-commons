/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons H2ServerUtils.java 2012-2-2 10:47:22 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.sql.SQLException;

import org.h2.tools.Server;

/**
 * The Class H2ServerUtils.
 *
 * @author l.xue.nong
 */
public abstract class H2ServerUtils {
	/** The pg. */
	private static Server web, tcp, pg;

	/**
	 * Builds the h2 server.
	 *
	 * @param args the args
	 * @return the server
	 * @throws SQLException the sQL exception
	 */
	public static void buildH2Server(String... args) throws SQLException {
		boolean tcpStart = false, pgStart = false, webStart = false;
		boolean tcpShutdown = false, tcpShutdownForce = false;
		String tcpPassword = "";
		String tcpShutdownServer = "";
		boolean startDefaultServers = true;
		for (int i = 0; args != null && i < args.length; i++) {
			String arg = args[i];
			if (arg == null) {
				continue;
			} else if ("-?".equals(arg) || "-help".equals(arg)) {
				return;
			} else if (arg.startsWith("-web")) {
				if ("-web".equals(arg)) {
					startDefaultServers = false;
					webStart = true;
				} else if ("-webAllowOthers".equals(arg)) {
					// no parameters
				} else if ("-webDaemon".equals(arg)) {
					// no parameters
				} else if ("-webSSL".equals(arg)) {
					// no parameters
				} else if ("-webPort".equals(arg)) {
					i++;
				} else {
					throwUnsupportedOption(arg);
				}
			} else if ("-browser".equals(arg)) {
				startDefaultServers = false;
			} else if (arg.startsWith("-tcp")) {
				if ("-tcp".equals(arg)) {
					startDefaultServers = false;
					tcpStart = true;
				} else if ("-tcpAllowOthers".equals(arg)) {
					// no parameters
				} else if ("-tcpDaemon".equals(arg)) {
					// no parameters
				} else if ("-tcpSSL".equals(arg)) {
					// no parameters
				} else if ("-tcpPort".equals(arg)) {
					i++;
				} else if ("-tcpPassword".equals(arg)) {
					tcpPassword = args[++i];
				} else if ("-tcpShutdown".equals(arg)) {
					startDefaultServers = false;
					tcpShutdown = true;
					tcpShutdownServer = args[++i];
				} else if ("-tcpShutdownForce".equals(arg)) {
					tcpShutdownForce = true;
				} else {
					throwUnsupportedOption(arg);
				}
			} else if (arg.startsWith("-pg")) {
				if ("-pg".equals(arg)) {
					startDefaultServers = false;
					pgStart = true;
				} else if ("-pgAllowOthers".equals(arg)) {
					// no parameters
				} else if ("-pgDaemon".equals(arg)) {
					// no parameters
				} else if ("-pgPort".equals(arg)) {
					i++;
				} else {
					throwUnsupportedOption(arg);
				}
			} else if ("-properties".equals(arg)) {
				i++;
			} else if ("-trace".equals(arg)) {
				// no parameters
			} else if ("-ifExists".equals(arg)) {
				// no parameters
			} else if ("-baseDir".equals(arg)) {
				i++;
			} else {
				throwUnsupportedOption(arg);
			}
		}
		if (startDefaultServers) {
			tcpStart = true;
			pgStart = true;
			webStart = true;
		}
		if (tcpShutdown) {
			System.out.println("Shutting down TCP Server at " + tcpShutdownServer);
			Server.shutdownTcpServer(tcpShutdownServer, tcpPassword, tcpShutdownForce, false);
		}
		try {
			if (webStart) {
				web = Server.createWebServer(args);
				web.start();
				System.out.println(web.getStatus());
			}
			if (tcpStart) {
				tcp = Server.createTcpServer(args);
				tcp.start();
				System.out.println(tcp.getStatus());
			}
			if (pgStart) {
				pg = Server.createPgServer(args);
				pg.start();
				System.out.println(pg.getStatus());
			}
			initShutdownHook();
		} catch (SQLException e) {
			stopAll();
			throw e;
		}
	}

	/**
	 * Throw unsupported option.
	 *
	 * @param option the option
	 * @throws SQLException the sQL exception
	 */
	private static void throwUnsupportedOption(String option) throws SQLException {
		throw new SQLException("Unsupported option: " + option);
	}

	/**
	 * Stop all.
	 */
	public static void stopAll() {
		if (web != null && web.isRunning(false)) {
			web.stop();
			web = null;
		}
		if (tcp != null && tcp.isRunning(false)) {
			tcp.stop();
			tcp = null;
		}
		if (pg != null && pg.isRunning(false)) {
			pg.stop();
			pg = null;
		}
	}

	/**
	 * Inits the shutdown hook.
	 */
	public static void initShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				stopAll();
			}
		});
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws SQLException the sQL exception
	 */
	public static void main(String[] args) throws SQLException {
		buildH2Server("-tcp", "-web");
		ThreadUtils.sleep(10000000);
		stopAll();
	}
}
