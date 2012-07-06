/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons StopWatch.java 2012-3-29 15:15:10 l.xue.nong$$
 */

package cn.com.rebirth.commons;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.rebirth.commons.unit.TimeValue;

/**
 * The Class StopWatch.
 *
 * @author l.xue.nong
 */
public class StopWatch {

	/** The id. */
	private final String id;

	/** The keep task list. */
	private boolean keepTaskList = true;

	/** The task list. */
	private final List<TaskInfo> taskList = new LinkedList<TaskInfo>();

	/** The start time millis. */
	private long startTimeMillis;

	/** The running. */
	private boolean running;

	/** The current task name. */
	private String currentTaskName;

	/** The last task info. */
	private TaskInfo lastTaskInfo;

	/** The task count. */
	private int taskCount;

	/** The total time millis. */
	private long totalTimeMillis;

	/**
	 * Instantiates a new stop watch.
	 */
	public StopWatch() {
		this.id = "";
	}

	/**
	 * Instantiates a new stop watch.
	 *
	 * @param id the id
	 */
	public StopWatch(String id) {
		this.id = id;
	}

	/**
	 * Keep task list.
	 *
	 * @param keepTaskList the keep task list
	 * @return the stop watch
	 */
	public StopWatch keepTaskList(boolean keepTaskList) {
		this.keepTaskList = keepTaskList;
		return this;
	}

	/**
	 * Start.
	 *
	 * @return the stop watch
	 * @throws IllegalStateException the illegal state exception
	 */
	public StopWatch start() throws IllegalStateException {
		return start("");
	}

	/**
	 * Start.
	 *
	 * @param taskName the task name
	 * @return the stop watch
	 * @throws IllegalStateException the illegal state exception
	 */
	public StopWatch start(String taskName) throws IllegalStateException {
		if (this.running) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		}
		this.startTimeMillis = System.currentTimeMillis();
		this.running = true;
		this.currentTaskName = taskName;
		return this;
	}

	/**
	 * Stop.
	 *
	 * @return the stop watch
	 * @throws IllegalStateException the illegal state exception
	 */
	public StopWatch stop() throws IllegalStateException {
		if (!this.running) {
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		}
		long lastTime = System.currentTimeMillis() - this.startTimeMillis;
		this.totalTimeMillis += lastTime;
		this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
		if (this.keepTaskList) {
			this.taskList.add(lastTaskInfo);
		}
		++this.taskCount;
		this.running = false;
		this.currentTaskName = null;
		return this;
	}

	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * Last task time.
	 *
	 * @return the time value
	 * @throws IllegalStateException the illegal state exception
	 */
	public TimeValue lastTaskTime() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tests run: can't get last interval");
		}
		return this.lastTaskInfo.getTime();
	}

	/**
	 * Last task name.
	 *
	 * @return the string
	 * @throws IllegalStateException the illegal state exception
	 */
	public String lastTaskName() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tests run: can't get last interval");
		}
		return this.lastTaskInfo.getTaskName();
	}

	/**
	 * Total time.
	 *
	 * @return the time value
	 */
	public TimeValue totalTime() {
		return new TimeValue(totalTimeMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Task count.
	 *
	 * @return the int
	 */
	public int taskCount() {
		return taskCount;
	}

	/**
	 * Task info.
	 *
	 * @return the task info[]
	 */
	public TaskInfo[] taskInfo() {
		if (!this.keepTaskList) {
			throw new UnsupportedOperationException("Task info is not being kept!");
		}
		return this.taskList.toArray(new TaskInfo[this.taskList.size()]);
	}

	/**
	 * Short summary.
	 *
	 * @return the string
	 */
	public String shortSummary() {
		return "StopWatch '" + this.id + "': running time  = " + totalTime();
	}

	/**
	 * Pretty print.
	 *
	 * @return the string
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		if (!this.keepTaskList) {
			sb.append("No task info kept");
		} else {
			sb.append("-----------------------------------------\n");
			sb.append("ms     %     Task name\n");
			sb.append("-----------------------------------------\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(5);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			for (TaskInfo task : taskInfo()) {
				sb.append(nf.format(task.getTime().millis())).append("  ");
				sb.append(pf.format(task.getTime().secondsFrac() / totalTime().secondsFrac())).append("  ");
				sb.append(task.getTaskName()).append("\n");
			}
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(shortSummary());
		if (this.keepTaskList) {
			for (TaskInfo task : taskInfo()) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTime());
				long percent = Math.round((100.0f * task.getTime().millis()) / totalTime().millis());
				sb.append(" = ").append(percent).append("%");
			}
		} else {
			sb.append("; no task info kept");
		}
		return sb.toString();
	}

	/**
	 * The Class TaskInfo.
	 *
	 * @author l.xue.nong
	 */
	public static class TaskInfo {

		/** The task name. */
		private final String taskName;

		/** The time value. */
		private final TimeValue timeValue;

		/**
		 * Instantiates a new task info.
		 *
		 * @param taskName the task name
		 * @param timeMillis the time millis
		 */
		private TaskInfo(String taskName, long timeMillis) {
			this.taskName = taskName;
			this.timeValue = new TimeValue(timeMillis, TimeUnit.MILLISECONDS);
		}

		/**
		 * Gets the task name.
		 *
		 * @return the task name
		 */
		public String getTaskName() {
			return taskName;
		}

		/**
		 * Gets the time.
		 *
		 * @return the time
		 */
		public TimeValue getTime() {
			return timeValue;
		}
	}

}
