/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons IdentityHashSet.java 2012-3-29 15:15:17 l.xue.nong$$
 */

package cn.com.rebirth.commons.collect;

import java.io.IOException;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * The Class IdentityHashSet.
 *
 * @param <E> the element type
 * @author l.xue.nong
 */
public class IdentityHashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {

	
	/** The Constant serialVersionUID. */
	static final long serialVersionUID = -5024744406713321677L;

	
	/** The map. */
	private transient IdentityHashMap<E, Object> map;

	
	
	/** The Constant PRESENT. */
	private static final Object PRESENT = new Object();

	
	/**
	 * Instantiates a new identity hash set.
	 */
	public IdentityHashSet() {
		map = new IdentityHashMap<E, Object>();
	}

	
	/**
	 * Instantiates a new identity hash set.
	 *
	 * @param c the c
	 */
	public IdentityHashSet(Collection<? extends E> c) {
		map = new IdentityHashMap<E, Object>(Math.max((int) (c.size() / .75f) + 1, 16));
		addAll(c);
	}

	
	/**
	 * Instantiates a new identity hash set.
	 *
	 * @param expectedSize the expected size
	 */
	public IdentityHashSet(int expectedSize) {
		map = new IdentityHashMap<E, Object>(expectedSize);
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	public int size() {
		return map.size();
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	public boolean add(E e) {
		return map.put(e, PRESENT) == null;
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return map.remove(o) == PRESENT;
	}

	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	public void clear() {
		map.clear();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	public Object clone() {
		try {
			IdentityHashSet<E> newSet = (IdentityHashSet<E>) super.clone();
			newSet.map = (IdentityHashMap<E, Object>) map.clone();
			return newSet;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	
	/**
	 * Write object.
	 *
	 * @param s the s
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		
		s.defaultWriteObject();

		
		s.writeInt(map.size());

		
		for (Iterator<E> i = map.keySet().iterator(); i.hasNext();)
			s.writeObject(i.next());
	}

	
	/**
	 * Read object.
	 *
	 * @param s the s
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		
		s.defaultReadObject();

		
		int size = s.readInt();

		map = new IdentityHashMap<E, Object>(size);

		
		for (int i = 0; i < size; i++) {
			E e = (E) s.readObject();
			map.put(e, PRESENT);
		}
	}
}
