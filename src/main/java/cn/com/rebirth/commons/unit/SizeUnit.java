/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SizeUnit.java 2012-3-29 15:15:08 l.xue.nong$$
 */


package cn.com.rebirth.commons.unit;


/**
 * The Enum SizeUnit.
 *
 * @author l.xue.nong
 */
public enum SizeUnit {
	
	
	/** The SINGLE. */
	SINGLE {
		@Override
		public long toSingles(long size) {
			return size;
		}

		@Override
		public long toKilo(long size) {
			return size / (C1 / C0);
		}

		@Override
		public long toMega(long size) {
			return size / (C2 / C0);
		}

		@Override
		public long toGiga(long size) {
			return size / (C3 / C0);
		}
	},
	
	
	/** The KILO. */
	KILO {
		@Override
		public long toSingles(long size) {
			return x(size, C1 / C0, MAX / (C1 / C0));
		}

		@Override
		public long toKilo(long size) {
			return size;
		}

		@Override
		public long toMega(long size) {
			return size / (C2 / C1);
		}

		@Override
		public long toGiga(long size) {
			return size / (C3 / C1);
		}
	},
	
	
	/** The MEGA. */
	MEGA {
		@Override
		public long toSingles(long size) {
			return x(size, C2 / C0, MAX / (C2 / C0));
		}

		@Override
		public long toKilo(long size) {
			return x(size, C2 / C1, MAX / (C2 / C1));
		}

		@Override
		public long toMega(long size) {
			return size;
		}

		@Override
		public long toGiga(long size) {
			return size / (C3 / C2);
		}
	},
	
	
	/** The GIGA. */
	GIGA {
		@Override
		public long toSingles(long size) {
			return x(size, C3 / C0, MAX / (C3 / C0));
		}

		@Override
		public long toKilo(long size) {
			return x(size, C3 / C1, MAX / (C3 / C1));
		}

		@Override
		public long toMega(long size) {
			return x(size, C3 / C2, MAX / (C3 / C2));
		}

		@Override
		public long toGiga(long size) {
			return size;
		}
	};

	
	/** The Constant C0. */
	static final long C0 = 1L;
	
	
	/** The Constant C1. */
	static final long C1 = C0 * 1000L;
	
	
	/** The Constant C2. */
	static final long C2 = C1 * 1000L;
	
	
	/** The Constant C3. */
	static final long C3 = C2 * 1000L;

	
	/** The Constant MAX. */
	static final long MAX = Long.MAX_VALUE;

	
	/**
	 * X.
	 *
	 * @param d the d
	 * @param m the m
	 * @param over the over
	 * @return the long
	 */
	static long x(long d, long m, long over) {
		if (d > over)
			return Long.MAX_VALUE;
		if (d < -over)
			return Long.MIN_VALUE;
		return d * m;
	}

	
	/**
	 * To singles.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toSingles(long size) {
		throw new AbstractMethodError();
	}

	
	/**
	 * To kilo.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toKilo(long size) {
		throw new AbstractMethodError();
	}

	
	/**
	 * To mega.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toMega(long size) {
		throw new AbstractMethodError();
	}

	
	/**
	 * To giga.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toGiga(long size) {
		throw new AbstractMethodError();
	}
}