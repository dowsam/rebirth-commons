/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons BoundedTreeSet.java 2012-3-29 15:15:08 l.xue.nong$$
 */


package cn.com.rebirth.commons.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;


/**
 * The Class BoundedTreeSet.
 *
 * @param <E> the element type
 * @author l.xue.nong
 */
public class BoundedTreeSet<E> extends TreeSet<E> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7117207179454836039L;
	
	/** The size. */
	private final int size;

	
	/**
	 * Instantiates a new bounded tree set.
	 *
	 * @param size the size
	 */
	public BoundedTreeSet(int size) {
		this.size = size;
	}

	
	/**
	 * Instantiates a new bounded tree set.
	 *
	 * @param comparator the comparator
	 * @param size the size
	 */
	public BoundedTreeSet(Comparator<? super E> comparator, int size) {
		super(comparator);
		this.size = size;
	}

	
	/* (non-Javadoc)
	 * @see java.util.TreeSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		boolean result = super.add(e);
		rebound();
		return result;
	}

	
	/* (non-Javadoc)
	 * @see java.util.TreeSet#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = super.addAll(c);
		rebound();
		return result;
	}

	
	/**
	 * Rebound.
	 */
	private void rebound() {
		while (size() > size) {
			remove(last());
		}
	}
}
