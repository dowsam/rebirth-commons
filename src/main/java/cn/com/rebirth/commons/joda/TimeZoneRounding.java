/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons TimeZoneRounding.java 2012-3-29 15:15:07 l.xue.nong$$
 */

package cn.com.rebirth.commons.joda;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

import cn.com.rebirth.commons.unit.TimeValue;

/**
 * The Class TimeZoneRounding.
 *
 * @author l.xue.nong
 */
public abstract class TimeZoneRounding {

	/**
	 * Calc.
	 *
	 * @param utcMillis the utc millis
	 * @return the long
	 */
	public abstract long calc(long utcMillis);

	/**
	 * Builder.
	 *
	 * @param field the field
	 * @return the builder
	 */
	public static Builder builder(DateTimeField field) {
		return new Builder(field);
	}

	/**
	 * Builder.
	 *
	 * @param interval the interval
	 * @return the builder
	 */
	public static Builder builder(TimeValue interval) {
		return new Builder(interval);
	}

	/**
	 * The Class Builder.
	 *
	 * @author l.xue.nong
	 */
	public static class Builder {

		/** The field. */
		private DateTimeField field;

		/** The interval. */
		private long interval = -1;

		/** The pre tz. */
		private DateTimeZone preTz = DateTimeZone.UTC;

		/** The post tz. */
		private DateTimeZone postTz = DateTimeZone.UTC;

		/** The factor. */
		private float factor = 1.0f;

		/** The pre offset. */
		private long preOffset;

		/** The post offset. */
		private long postOffset;

		/** The pre zone adjust large interval. */
		private boolean preZoneAdjustLargeInterval = false;

		/**
		 * Instantiates a new builder.
		 *
		 * @param field the field
		 */
		public Builder(DateTimeField field) {
			this.field = field;
			this.interval = -1;
		}

		/**
		 * Instantiates a new builder.
		 *
		 * @param interval the interval
		 */
		public Builder(TimeValue interval) {
			this.field = null;
			this.interval = interval.millis();
		}

		/**
		 * Pre zone.
		 *
		 * @param preTz the pre tz
		 * @return the builder
		 */
		public Builder preZone(DateTimeZone preTz) {
			this.preTz = preTz;
			return this;
		}

		/**
		 * Pre zone adjust large interval.
		 *
		 * @param preZoneAdjustLargeInterval the pre zone adjust large interval
		 * @return the builder
		 */
		public Builder preZoneAdjustLargeInterval(boolean preZoneAdjustLargeInterval) {
			this.preZoneAdjustLargeInterval = preZoneAdjustLargeInterval;
			return this;
		}

		/**
		 * Post zone.
		 *
		 * @param postTz the post tz
		 * @return the builder
		 */
		public Builder postZone(DateTimeZone postTz) {
			this.postTz = postTz;
			return this;
		}

		/**
		 * Pre offset.
		 *
		 * @param preOffset the pre offset
		 * @return the builder
		 */
		public Builder preOffset(long preOffset) {
			this.preOffset = preOffset;
			return this;
		}

		/**
		 * Post offset.
		 *
		 * @param postOffset the post offset
		 * @return the builder
		 */
		public Builder postOffset(long postOffset) {
			this.postOffset = postOffset;
			return this;
		}

		/**
		 * Factor.
		 *
		 * @param factor the factor
		 * @return the builder
		 */
		public Builder factor(float factor) {
			this.factor = factor;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the time zone rounding
		 */
		public TimeZoneRounding build() {
			TimeZoneRounding timeZoneRounding;
			if (field != null) {
				if (preTz.equals(DateTimeZone.UTC) && postTz.equals(DateTimeZone.UTC)) {
					timeZoneRounding = new UTCTimeZoneRoundingFloor(field);
				} else if (preZoneAdjustLargeInterval
						|| field.getDurationField().getUnitMillis() < DateTimeConstants.MILLIS_PER_HOUR * 12) {
					timeZoneRounding = new TimeTimeZoneRoundingFloor(field, preTz, postTz);
				} else {
					timeZoneRounding = new DayTimeZoneRoundingFloor(field, preTz, postTz);
				}
			} else {
				if (preTz.equals(DateTimeZone.UTC) && postTz.equals(DateTimeZone.UTC)) {
					timeZoneRounding = new UTCIntervalTimeZoneRounding(interval);
				} else if (preZoneAdjustLargeInterval || interval < DateTimeConstants.MILLIS_PER_HOUR * 12) {
					timeZoneRounding = new TimeIntervalTimeZoneRounding(interval, preTz, postTz);
				} else {
					timeZoneRounding = new DayIntervalTimeZoneRounding(interval, preTz, postTz);
				}
			}
			if (preOffset != 0 || postOffset != 0) {
				timeZoneRounding = new PrePostTimeZoneRounding(timeZoneRounding, preOffset, postOffset);
			}
			if (factor != 1.0f) {
				timeZoneRounding = new FactorTimeZoneRounding(timeZoneRounding, factor);
			}
			return timeZoneRounding;
		}
	}

	/**
	 * The Class TimeTimeZoneRoundingFloor.
	 *
	 * @author l.xue.nong
	 */
	static class TimeTimeZoneRoundingFloor extends TimeZoneRounding {

		/** The field. */
		private final DateTimeField field;

		/** The pre tz. */
		private final DateTimeZone preTz;

		/** The post tz. */
		private final DateTimeZone postTz;

		/**
		 * Instantiates a new time time zone rounding floor.
		 *
		 * @param field the field
		 * @param preTz the pre tz
		 * @param postTz the post tz
		 */
		TimeTimeZoneRoundingFloor(DateTimeField field, DateTimeZone preTz, DateTimeZone postTz) {
			this.field = field;
			this.preTz = preTz;
			this.postTz = postTz;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			long time = utcMillis + preTz.getOffset(utcMillis);
			time = field.roundFloor(time);

			time = time - preTz.getOffset(time);

			time = time + postTz.getOffset(time);
			return time;
		}
	}

	/**
	 * The Class UTCTimeZoneRoundingFloor.
	 *
	 * @author l.xue.nong
	 */
	static class UTCTimeZoneRoundingFloor extends TimeZoneRounding {

		/** The field. */
		private final DateTimeField field;

		/**
		 * Instantiates a new uTC time zone rounding floor.
		 *
		 * @param field the field
		 */
		UTCTimeZoneRoundingFloor(DateTimeField field) {
			this.field = field;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			return field.roundFloor(utcMillis);
		}
	}

	/**
	 * The Class DayTimeZoneRoundingFloor.
	 *
	 * @author l.xue.nong
	 */
	static class DayTimeZoneRoundingFloor extends TimeZoneRounding {

		/** The field. */
		private final DateTimeField field;

		/** The pre tz. */
		private final DateTimeZone preTz;

		/** The post tz. */
		private final DateTimeZone postTz;

		/**
		 * Instantiates a new day time zone rounding floor.
		 *
		 * @param field the field
		 * @param preTz the pre tz
		 * @param postTz the post tz
		 */
		DayTimeZoneRoundingFloor(DateTimeField field, DateTimeZone preTz, DateTimeZone postTz) {
			this.field = field;
			this.preTz = preTz;
			this.postTz = postTz;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			long time = utcMillis + preTz.getOffset(utcMillis);
			time = field.roundFloor(time);

			time = time + postTz.getOffset(time);
			return time;
		}
	}

	/**
	 * The Class UTCIntervalTimeZoneRounding.
	 *
	 * @author l.xue.nong
	 */
	static class UTCIntervalTimeZoneRounding extends TimeZoneRounding {

		/** The interval. */
		private final long interval;

		/**
		 * Instantiates a new uTC interval time zone rounding.
		 *
		 * @param interval the interval
		 */
		UTCIntervalTimeZoneRounding(long interval) {
			this.interval = interval;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			return ((utcMillis / interval) * interval);
		}
	}

	/**
	 * The Class TimeIntervalTimeZoneRounding.
	 *
	 * @author l.xue.nong
	 */
	static class TimeIntervalTimeZoneRounding extends TimeZoneRounding {

		/** The interval. */
		private final long interval;

		/** The pre tz. */
		private final DateTimeZone preTz;

		/** The post tz. */
		private final DateTimeZone postTz;

		/**
		 * Instantiates a new time interval time zone rounding.
		 *
		 * @param interval the interval
		 * @param preTz the pre tz
		 * @param postTz the post tz
		 */
		TimeIntervalTimeZoneRounding(long interval, DateTimeZone preTz, DateTimeZone postTz) {
			this.interval = interval;
			this.preTz = preTz;
			this.postTz = postTz;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			long time = utcMillis + preTz.getOffset(utcMillis);
			time = ((time / interval) * interval);

			time = time - preTz.getOffset(time);

			time = time + postTz.getOffset(time);
			return time;
		}
	}

	/**
	 * The Class DayIntervalTimeZoneRounding.
	 *
	 * @author l.xue.nong
	 */
	static class DayIntervalTimeZoneRounding extends TimeZoneRounding {

		/** The interval. */
		private final long interval;

		/** The pre tz. */
		private final DateTimeZone preTz;

		/** The post tz. */
		private final DateTimeZone postTz;

		/**
		 * Instantiates a new day interval time zone rounding.
		 *
		 * @param interval the interval
		 * @param preTz the pre tz
		 * @param postTz the post tz
		 */
		DayIntervalTimeZoneRounding(long interval, DateTimeZone preTz, DateTimeZone postTz) {
			this.interval = interval;
			this.preTz = preTz;
			this.postTz = postTz;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			long time = utcMillis + preTz.getOffset(utcMillis);
			time = ((time / interval) * interval);

			time = time + postTz.getOffset(time);
			return time;
		}
	}

	/**
	 * The Class FactorTimeZoneRounding.
	 *
	 * @author l.xue.nong
	 */
	static class FactorTimeZoneRounding extends TimeZoneRounding {

		/** The time zone rounding. */
		private final TimeZoneRounding timeZoneRounding;

		/** The factor. */
		private final float factor;

		/**
		 * Instantiates a new factor time zone rounding.
		 *
		 * @param timeZoneRounding the time zone rounding
		 * @param factor the factor
		 */
		FactorTimeZoneRounding(TimeZoneRounding timeZoneRounding, float factor) {
			this.timeZoneRounding = timeZoneRounding;
			this.factor = factor;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			return timeZoneRounding.calc((long) (factor * utcMillis));
		}
	}

	/**
	 * The Class PrePostTimeZoneRounding.
	 *
	 * @author l.xue.nong
	 */
	static class PrePostTimeZoneRounding extends TimeZoneRounding {

		/** The time zone rounding. */
		private final TimeZoneRounding timeZoneRounding;

		/** The pre offset. */
		private final long preOffset;

		/** The post offset. */
		private final long postOffset;

		/**
		 * Instantiates a new pre post time zone rounding.
		 *
		 * @param timeZoneRounding the time zone rounding
		 * @param preOffset the pre offset
		 * @param postOffset the post offset
		 */
		PrePostTimeZoneRounding(TimeZoneRounding timeZoneRounding, long preOffset, long postOffset) {
			this.timeZoneRounding = timeZoneRounding;
			this.preOffset = preOffset;
			this.postOffset = postOffset;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.search.commons.joda.TimeZoneRounding#calc(long)
		 */
		@Override
		public long calc(long utcMillis) {
			return postOffset + timeZoneRounding.calc(utcMillis + preOffset);
		}
	}
}
