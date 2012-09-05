/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons RebirthContainer.java 2012-7-6 15:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.commons.utils.ClassResolverUtils.AbstractFindCallback;
import cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback;
import cn.com.rebirth.commons.utils.ResolverUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class RebirthContainer.
 *
 * @author l.xue.nong
 */
public final class RebirthContainer {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The start. */
	private boolean start = false;

	/** The initializations. */
	private volatile List<Initialization> initializations;

	/** The containers. */
	private volatile List<Container> containers;
	/** The context. */
	protected volatile Map<Class<? extends Initialization>, Initialization> context;
	/** The find callback. */
	private static FindCallback<Initialization> findCallback = new FindCallback<Initialization>() {

		@Override
		public boolean needFor(int mod) {
			return (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod) && Modifier.isPublic(mod));
		}

		@Override
		public void findType(ResolverUtils<Initialization> resolverUtils) {
			resolverUtils.findImplementations(Initialization.class, StringUtils.EMPTY);
		}

		@Override
		public Initialization constructor(Class<?> entityClass) throws RebirthException {
			try {
				Constructor<?> ctor = (Constructor<?>) entityClass.getDeclaredConstructor();
				if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass()
						.getModifiers())) && !ctor.isAccessible()) {
					ctor.setAccessible(true);
				}
				return (Initialization) ctor.newInstance();
			} catch (Exception e) {
				throw new RebirthException(e.getMessage(), e);
			}
		}
	};

	/** The pre sort. */
	private List<PreInitialization> preSort = Lists.newArrayList();

	/** The after sort. */
	private List<AfterInitialization> afterSort = Lists.newArrayList();

	/** The orther. */
	private List<Initialization> orther = Lists.newArrayList();

	/** The comparator. */
	private static Comparator<SortInitialization> comparator = new Comparator<SortInitialization>() {

		@Override
		public int compare(SortInitialization o1, SortInitialization o2) {
			return o2.sort().compareTo(o1.sort());
		}
	};

	/**
	 * Instantiates a new rebirth container.
	 */
	private RebirthContainer() {
		super();
		this.initializations = ClassResolverUtils.find(findCallback);
		this.containers = ClassResolverUtils.find(new AbstractFindCallback<Container>() {

			@Override
			protected void doFindType(ResolverUtils<Container> resolverUtils, Class<Container> entityClass) {
				resolverUtils.findImplementations(entityClass, StringUtils.EMPTY);
			}

		});
		this.context = Maps.newLinkedHashMap();
		bulid();
	}

	/**
	 * Gets the single instance of RebirthContainer.
	 *
	 * @return single instance of RebirthContainer
	 */
	public static RebirthContainer getInstance() {
		return SingletonHolder.rebirthContainer;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The rebirth container. */
		static RebirthContainer rebirthContainer = new RebirthContainer();
	}

	/**
	 * Bulid.
	 */
	void bulid() {
		for (Initialization initialization : initializations) {
			if (initialization instanceof PreInitialization) {
				preSort.add((PreInitialization) initialization);
			} else if (initialization instanceof AfterInitialization) {
				afterSort.add((AfterInitialization) initialization);
			} else {
				orther.add(initialization);
			}
			this.context.put(initialization.getClass(), initialization);
		}
		Collections.sort(preSort, comparator);
		Collections.sort(afterSort, comparator);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T extends Initialization> T get(Class<T> entityClass) {
		return (T) this.context.get(entityClass);
	}

	/**
	 * Start.
	 */
	public synchronized void start() {
		if (!start) {
			this.start = true;
			logger.info("Initialization Rebirth Container……………………");
			List<AbstractContainer> abstractContainers = Lists.newArrayList();
			for (Container container : containers) {
				container.start();
				if (container instanceof AbstractContainer) {
					abstractContainers.add((AbstractContainer) container);
				}
			}
			for (AbstractContainer abstractContainer : abstractContainers) {
				abstractContainer.beforeContainerStart(this);
			}
			init(preSort);
			init(orther);
			init(afterSort);
			for (AbstractContainer abstractContainer : abstractContainers) {
				abstractContainer.afterContainerStart(this);
			}
			logger.info("Initialization Rebirth Container……………………end");
		}
	}

	/**
	 * Inits the.
	 *
	 * @param orther the orther
	 */
	private void init(List<? extends Initialization> orther) {
		for (Initialization initialization : orther) {
			if (initialization instanceof PreInitialization) {
				((PreInitialization) initialization).beforeInit(this);
			}
			initialization.init();
			if (initialization instanceof AfterInitialization) {
				((AfterInitialization) initialization).afterInit(this);
			}
		}
	}

	/**
	 * Stop.
	 */
	public synchronized void stop() {
		if (start) {
			logger.info("Close Rebirth Container……………………");
			stop(preSort);
			stop(orther);
			stop(afterSort);
			for (Container container : containers) {
				container.stop();
			}
			logger.info("Close Rebirth Container……………………end");
		}
	}

	/**
	 * Stop.
	 *
	 * @param preSort2 the pre sort2
	 */
	private void stop(List<? extends Initialization> preSort2) {
		for (Initialization initialization : preSort2) {
			initialization.stop();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 */
	public static void main(String[] args) throws SecurityException, NoSuchMethodException {
		RebirthContainer.getInstance().start();
		RebirthContainer.getInstance().stop();
	}

}
