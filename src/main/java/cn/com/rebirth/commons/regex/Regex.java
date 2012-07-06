/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Regex.java 2012-3-29 15:15:08 l.xue.nong$$
 */
package cn.com.rebirth.commons.regex;

import java.util.regex.Pattern;

import cn.com.rebirth.commons.Strings;
import cn.com.rebirth.commons.exception.RestartIllegalArgumentException;



/**
 * The Class Regex.
 *
 * @author l.xue.nong
 */
public class Regex {

	
	/**
	 * Checks if is simple match pattern.
	 *
	 * @param str the str
	 * @return true, if is simple match pattern
	 */
	public static boolean isSimpleMatchPattern(String str) {
		return str.indexOf('*') != -1;
	}

	
	/**
	 * Simple match.
	 *
	 * @param pattern the pattern
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean simpleMatch(String pattern, String str) {
		if (pattern == null || str == null) {
			return false;
		}
		int firstIndex = pattern.indexOf('*');
		if (firstIndex == -1) {
			return pattern.equals(str);
		}
		if (firstIndex == 0) {
			if (pattern.length() == 1) {
				return true;
			}
			int nextIndex = pattern.indexOf('*', firstIndex + 1);
			if (nextIndex == -1) {
				return str.endsWith(pattern.substring(1));
			}
			String part = pattern.substring(1, nextIndex);
			int partIndex = str.indexOf(part);
			while (partIndex != -1) {
				if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
					return true;
				}
				partIndex = str.indexOf(part, partIndex + 1);
			}
			return false;
		}
		return (str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) && simpleMatch(
				pattern.substring(firstIndex), str.substring(firstIndex)));
	}

	
	/**
	 * Simple match.
	 *
	 * @param patterns the patterns
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean simpleMatch(String[] patterns, String str) {
		if (patterns != null) {
			for (String pattern : patterns) {
				if (simpleMatch(pattern, str)) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * Compile.
	 *
	 * @param regex the regex
	 * @param flags the flags
	 * @return the pattern
	 */
	public static Pattern compile(String regex, String flags) {
		int pFlags = flags == null ? 0 : flagsFromString(flags);
		return Pattern.compile(regex, pFlags);
	}

	
	/**
	 * Flags from string.
	 *
	 * @param flags the flags
	 * @return the int
	 */
	public static int flagsFromString(String flags) {
		int pFlags = 0;
		for (String s : Strings.delimitedListToStringArray(flags, "|")) {
			if (s.isEmpty()) {
				continue;
			}
			if ("CASE_INSENSITIVE".equalsIgnoreCase(s)) {
				pFlags |= Pattern.CASE_INSENSITIVE;
			} else if ("MULTILINE".equalsIgnoreCase(s)) {
				pFlags |= Pattern.MULTILINE;
			} else if ("DOTALL".equalsIgnoreCase(s)) {
				pFlags |= Pattern.DOTALL;
			} else if ("UNICODE_CASE".equalsIgnoreCase(s)) {
				pFlags |= Pattern.UNICODE_CASE;
			} else if ("CANON_EQ".equalsIgnoreCase(s)) {
				pFlags |= Pattern.CANON_EQ;
			} else if ("UNIX_LINES".equalsIgnoreCase(s)) {
				pFlags |= Pattern.UNIX_LINES;
			} else if ("LITERAL".equalsIgnoreCase(s)) {
				pFlags |= Pattern.LITERAL;
			} else if ("COMMENTS".equalsIgnoreCase(s)) {
				pFlags |= Pattern.COMMENTS;
			} else {
				throw new RestartIllegalArgumentException("Unknown regex flag [" + s + "]");
			}
		}
		return pFlags;
	}

	
	/**
	 * Flags to string.
	 *
	 * @param flags the flags
	 * @return the string
	 */
	public static String flagsToString(int flags) {
		StringBuilder sb = new StringBuilder();
		if ((flags & Pattern.CASE_INSENSITIVE) != 0) {
			sb.append("CASE_INSENSITIVE|");
		}
		if ((flags & Pattern.MULTILINE) != 0) {
			sb.append("MULTILINE|");
		}
		if ((flags & Pattern.DOTALL) != 0) {
			sb.append("DOTALL|");
		}
		if ((flags & Pattern.UNICODE_CASE) != 0) {
			sb.append("UNICODE_CASE|");
		}
		if ((flags & Pattern.CANON_EQ) != 0) {
			sb.append("CANON_EQ|");
		}
		if ((flags & Pattern.UNIX_LINES) != 0) {
			sb.append("UNIX_LINES|");
		}
		if ((flags & Pattern.LITERAL) != 0) {
			sb.append("LITERAL|");
		}
		if ((flags & Pattern.COMMENTS) != 0) {
			sb.append("COMMENTS|");
		}
		return sb.toString();
	}
}
