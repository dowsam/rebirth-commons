/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Preconditions.java 2012-7-6 10:23:52 l.xue.nong$$
 */


package cn.com.rebirth.commons;

import java.util.Collection;

import cn.com.rebirth.commons.exception.RebirthIllegalArgumentException;
import cn.com.rebirth.commons.exception.RebirthIllegalStateException;
import cn.com.rebirth.commons.exception.RebirthNullPointerException;


/**
 * The Class Preconditions.
 *
 * @author l.xue.nong
 */
public final class Preconditions {

	
	/**
	 * Instantiates a new preconditions.
	 */
	private Preconditions() {
	}

	
	/**
	 * Check argument.
	 *
	 * @param expression the expression
	 */
	public static void checkArgument(boolean expression) {
		if (!expression) {
			throw new RebirthIllegalArgumentException();
		}
	}

	
	/**
	 * Check argument.
	 *
	 * @param expression the expression
	 * @param errorMessage the error message
	 */
	public static void checkArgument(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new RebirthIllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	
	/**
	 * Check argument.
	 *
	 * @param expression the expression
	 * @param errorMessageTemplate the error message template
	 * @param errorMessageArgs the error message args
	 */
	public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (!expression) {
			throw new RebirthIllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	
	/**
	 * Check state.
	 *
	 * @param expression the expression
	 */
	public static void checkState(boolean expression) {
		if (!expression) {
			throw new RebirthIllegalStateException();
		}
	}

	
	/**
	 * Check state.
	 *
	 * @param expression the expression
	 * @param errorMessage the error message
	 */
	public static void checkState(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new RebirthIllegalStateException(String.valueOf(errorMessage));
		}
	}

	
	/**
	 * Check state.
	 *
	 * @param expression the expression
	 * @param errorMessageTemplate the error message template
	 * @param errorMessageArgs the error message args
	 */
	public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
		if (!expression) {
			throw new RebirthIllegalStateException(format(errorMessageTemplate, errorMessageArgs));
		}
	}

	
	/**
	 * Check not null.
	 *
	 * @param <T> the generic type
	 * @param reference the reference
	 * @return the t
	 */
	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new RebirthNullPointerException();
		}
		return reference;
	}

	
	/**
	 * Check not null.
	 *
	 * @param <T> the generic type
	 * @param reference the reference
	 * @param errorMessage the error message
	 * @return the t
	 */
	public static <T> T checkNotNull(T reference, Object errorMessage) {
		if (reference == null) {
			throw new RebirthNullPointerException(String.valueOf(errorMessage));
		}
		return reference;
	}

	
	/**
	 * Check not null.
	 *
	 * @param <T> the generic type
	 * @param reference the reference
	 * @param errorMessageTemplate the error message template
	 * @param errorMessageArgs the error message args
	 * @return the t
	 */
	public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
		if (reference == null) {
			
			throw new RebirthNullPointerException(format(errorMessageTemplate, errorMessageArgs));
		}
		return reference;
	}

	
	/**
	 * Check contents not null.
	 *
	 * @param <T> the generic type
	 * @param iterable the iterable
	 * @return the t
	 */
	public static <T extends Iterable<?>> T checkContentsNotNull(T iterable) {
		if (containsOrIsNull(iterable)) {
			throw new RebirthNullPointerException();
		}
		return iterable;
	}

	
	/**
	 * Check contents not null.
	 *
	 * @param <T> the generic type
	 * @param iterable the iterable
	 * @param errorMessage the error message
	 * @return the t
	 */
	public static <T extends Iterable<?>> T checkContentsNotNull(T iterable, Object errorMessage) {
		if (containsOrIsNull(iterable)) {
			throw new RebirthNullPointerException(String.valueOf(errorMessage));
		}
		return iterable;
	}

	
	/**
	 * Check contents not null.
	 *
	 * @param <T> the generic type
	 * @param iterable the iterable
	 * @param errorMessageTemplate the error message template
	 * @param errorMessageArgs the error message args
	 * @return the t
	 */
	public static <T extends Iterable<?>> T checkContentsNotNull(T iterable, String errorMessageTemplate,
			Object... errorMessageArgs) {
		if (containsOrIsNull(iterable)) {
			throw new RebirthNullPointerException(format(errorMessageTemplate, errorMessageArgs));
		}
		return iterable;
	}

	
	/**
	 * Contains or is null.
	 *
	 * @param iterable the iterable
	 * @return true, if successful
	 */
	private static boolean containsOrIsNull(Iterable<?> iterable) {
		if (iterable == null) {
			return true;
		}

		if (iterable instanceof Collection) {
			Collection<?> collection = (Collection<?>) iterable;
			try {
				return collection.contains(null);
			} catch (RebirthNullPointerException e) {
				return false;
			}
		} else {
			for (Object element : iterable) {
				if (element == null) {
					return true;
				}
			}
			return false;
		}
	}

	
	/**
	 * Check element index.
	 *
	 * @param index the index
	 * @param size the size
	 */
	public static void checkElementIndex(int index, int size) {
		checkElementIndex(index, size, "index");
	}

	
	/**
	 * Check element index.
	 *
	 * @param index the index
	 * @param size the size
	 * @param desc the desc
	 */
	public static void checkElementIndex(int index, int size, String desc) {
		checkArgument(size >= 0, "negative size: %s", size);
		if (index < 0) {
			throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", desc, index));
		}
		if (index >= size) {
			throw new IndexOutOfBoundsException(format("%s (%s) must be less than size (%s)", desc, index, size));
		}
	}

	
	/**
	 * Check position index.
	 *
	 * @param index the index
	 * @param size the size
	 */
	public static void checkPositionIndex(int index, int size) {
		checkPositionIndex(index, size, "index");
	}

	
	/**
	 * Check position index.
	 *
	 * @param index the index
	 * @param size the size
	 * @param desc the desc
	 */
	public static void checkPositionIndex(int index, int size, String desc) {
		checkArgument(size >= 0, "negative size: %s", size);
		if (index < 0) {
			throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", desc, index));
		}
		if (index > size) {
			throw new IndexOutOfBoundsException(format("%s (%s) must not be greater than size (%s)", desc, index, size));
		}
	}

	
	/**
	 * Check position indexes.
	 *
	 * @param start the start
	 * @param end the end
	 * @param size the size
	 */
	public static void checkPositionIndexes(int start, int end, int size) {
		checkPositionIndex(start, size, "start index");
		checkPositionIndex(end, size, "end index");
		if (end < start) {
			throw new IndexOutOfBoundsException(format("end index (%s) must not be less than start index (%s)", end,
					start));
		}
	}

	
	
	/**
	 * Format.
	 *
	 * @param template the template
	 * @param args the args
	 * @return the string
	 */
	static String format(String template, Object... args) {
		
		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template.substring(templateStart, placeholderStart));
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template.substring(templateStart));

		
		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append("]");
		}

		return builder.toString();
	}
}
