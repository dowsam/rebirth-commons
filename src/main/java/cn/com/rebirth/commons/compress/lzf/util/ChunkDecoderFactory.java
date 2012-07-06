/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ChunkDecoderFactory.java 2012-3-29 15:15:12 l.xue.nong$$
 */

package cn.com.rebirth.commons.compress.lzf.util;

import cn.com.rebirth.commons.Booleans;
import cn.com.rebirth.commons.compress.lzf.ChunkDecoder;
import cn.com.rebirth.commons.compress.lzf.impl.UnsafeChunkDecoder;
import cn.com.rebirth.commons.compress.lzf.impl.VanillaChunkDecoder;



/**
 * A factory for creating ChunkDecoder objects.
 */
public class ChunkDecoderFactory {
	
	/** The Constant _instance. */
	private final static ChunkDecoderFactory _instance;

	static {
		Class<?> impl = null;
		try {
			
			impl = (Class<?>) Class.forName(UnsafeChunkDecoder.class.getName());
		} catch (Throwable t) {
		}
		if (impl == null) {
			impl = VanillaChunkDecoder.class;
		}
		
		if (!Booleans.parseBoolean(System.getProperty("compress.lzf.decoder.optimized"), true)) {
			impl = VanillaChunkDecoder.class;
		}
		_instance = new ChunkDecoderFactory(impl);
	}

	
	/** The _impl class. */
	private final Class<? extends ChunkDecoder> _implClass;

	
	/**
	 * Instantiates a new chunk decoder factory.
	 *
	 * @param imp the imp
	 */
	@SuppressWarnings("unchecked")
	private ChunkDecoderFactory(Class<?> imp) {
		_implClass = (Class<? extends ChunkDecoder>) imp;
	}

	

	
	/**
	 * Optimal instance.
	 *
	 * @return the chunk decoder
	 */
	public static ChunkDecoder optimalInstance() {
		try {
			return _instance._implClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load a ChunkDecoder instance (" + e.getClass().getName() + "): "
					+ e.getMessage(), e);
		}
	}

	
	/**
	 * Safe instance.
	 *
	 * @return the chunk decoder
	 */
	public static ChunkDecoder safeInstance() {
		return new VanillaChunkDecoder();
	}
}
