/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Strings.java 2012-3-29 15:15:07 l.xue.nong$$
 */

package cn.com.rebirth.commons;

import gnu.trove.set.hash.THashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


/**
 * The Class Strings.
 *
 * @author l.xue.nong
 */
public class Strings {

	
	/** The Constant EMPTY_ARRAY. */
	public static final String[] EMPTY_ARRAY = new String[0];

	
	/** The Constant FOLDER_SEPARATOR. */
	private static final String FOLDER_SEPARATOR = "/";

	
	/** The Constant WINDOWS_FOLDER_SEPARATOR. */
	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	
	/** The Constant TOP_PATH. */
	private static final String TOP_PATH = "src/test";

	
	/** The Constant CURRENT_PATH. */
	private static final String CURRENT_PATH = ".";

	
	/** The Constant EXTENSION_SEPARATOR. */
	private static final char EXTENSION_SEPARATOR = '.';

	
	/**
	 * Split smart.
	 *
	 * @param s the s
	 * @param separator the separator
	 * @param decode the decode
	 * @return the list
	 */
	public static List<String> splitSmart(String s, String separator, boolean decode) {
		ArrayList<String> lst = new ArrayList<String>(2);
		StringBuilder sb = new StringBuilder();
		int pos = 0, end = s.length();
		while (pos < end) {
			if (s.startsWith(separator, pos)) {
				if (sb.length() > 0) {
					lst.add(sb.toString());
					sb = new StringBuilder();
				}
				pos += separator.length();
				continue;
			}

			char ch = s.charAt(pos++);
			if (ch == '\\') {
				if (!decode)
					sb.append(ch);
				if (pos >= end)
					break; 
				ch = s.charAt(pos++);
				if (decode) {
					switch (ch) {
					case 'n':
						ch = '\n';
						break;
					case 't':
						ch = '\t';
						break;
					case 'r':
						ch = '\r';
						break;
					case 'b':
						ch = '\b';
						break;
					case 'f':
						ch = '\f';
						break;
					}
				}
			}

			sb.append(ch);
		}

		if (sb.length() > 0) {
			lst.add(sb.toString());
		}

		return lst;
	}

	
	/**
	 * Split ws.
	 *
	 * @param s the s
	 * @param decode the decode
	 * @return the list
	 */
	public static List<String> splitWS(String s, boolean decode) {
		ArrayList<String> lst = new ArrayList<String>(2);
		StringBuilder sb = new StringBuilder();
		int pos = 0, end = s.length();
		while (pos < end) {
			char ch = s.charAt(pos++);
			if (Character.isWhitespace(ch)) {
				if (sb.length() > 0) {
					lst.add(sb.toString());
					sb = new StringBuilder();
				}
				continue;
			}

			if (ch == '\\') {
				if (!decode)
					sb.append(ch);
				if (pos >= end)
					break; 
				ch = s.charAt(pos++);
				if (decode) {
					switch (ch) {
					case 'n':
						ch = '\n';
						break;
					case 't':
						ch = '\t';
						break;
					case 'r':
						ch = '\r';
						break;
					case 'b':
						ch = '\b';
						break;
					case 'f':
						ch = '\f';
						break;
					}
				}
			}

			sb.append(ch);
		}

		if (sb.length() > 0) {
			lst.add(sb.toString());
		}

		return lst;
	}

	
	
	

	
	/**
	 * Checks for length.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	
	/**
	 * Checks for length.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	
	/**
	 * Checks for text.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Checks for text.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	
	/**
	 * Contains whitespace.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean containsWhitespace(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Contains whitespace.
	 *
	 * @param str the str
	 * @return true, if successful
	 */
	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	
	/**
	 * Trim whitespace.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	
	/**
	 * Trim all whitespace.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int index = 0;
		while (sb.length() > index) {
			if (Character.isWhitespace(sb.charAt(index))) {
				sb.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return sb.toString();
	}

	
	/**
	 * Trim leading whitespace.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	
	/**
	 * Trim trailing whitespace.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	
	/**
	 * Trim leading character.
	 *
	 * @param str the str
	 * @param leadingCharacter the leading character
	 * @return the string
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	
	/**
	 * Trim trailing character.
	 *
	 * @param str the str
	 * @param trailingCharacter the trailing character
	 * @return the string
	 */
	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	
	/**
	 * Starts with ignore case.
	 *
	 * @param str the str
	 * @param prefix the prefix
	 * @return true, if successful
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	
	/**
	 * Ends with ignore case.
	 *
	 * @param str the str
	 * @param suffix the suffix
	 * @return true, if successful
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	
	/**
	 * Substring match.
	 *
	 * @param str the str
	 * @param index the index
	 * @param substring the substring
	 * @return true, if successful
	 */
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Count occurrences of.
	 *
	 * @param str the str
	 * @param sub the sub
	 * @return the int
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	
	/**
	 * Replace.
	 *
	 * @param inString the in string
	 * @param oldPattern the old pattern
	 * @param newPattern the new pattern
	 * @return the string
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0; 
		int index = inString.indexOf(oldPattern);
		
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));
		
		return sb.toString();
	}

	
	/**
	 * Delete.
	 *
	 * @param inString the in string
	 * @param pattern the pattern
	 * @return the string
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	
	/**
	 * Delete any.
	 *
	 * @param inString the in string
	 * @param charsToDelete the chars to delete
	 * @return the string
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	
	
	

	
	/**
	 * Quote.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	
	/**
	 * Quote if string.
	 *
	 * @param obj the obj
	 * @return the object
	 */
	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	
	/**
	 * Unqualify.
	 *
	 * @param qualifiedName the qualified name
	 * @return the string
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	
	/**
	 * Unqualify.
	 *
	 * @param qualifiedName the qualified name
	 * @param separator the separator
	 * @return the string
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	
	/**
	 * Capitalize.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	
	/**
	 * Uncapitalize.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	
	/**
	 * Change first character case.
	 *
	 * @param str the str
	 * @param capitalize the capitalize
	 * @return the string
	 */
	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str.length());
		if (capitalize) {
			sb.append(Character.toUpperCase(str.charAt(0)));
		} else {
			sb.append(Character.toLowerCase(str.charAt(0)));
		}
		sb.append(str.substring(1));
		return sb.toString();
	}

	
	/** The Constant INVALID_FILENAME_CHARS. */
	public static final ImmutableSet<Character> INVALID_FILENAME_CHARS = ImmutableSet.of('\\', '/', '*', '?', '"', '<',
			'>', '|', ' ', ',');

	
	/**
	 * Valid file name.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public static boolean validFileName(String fileName) {
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (INVALID_FILENAME_CHARS.contains(c)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Valid file name excluding astrix.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public static boolean validFileNameExcludingAstrix(String fileName) {
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (c != '*' && INVALID_FILENAME_CHARS.contains(c)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Gets the filename.
	 *
	 * @param path the path
	 * @return the filename
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	
	/**
	 * Gets the filename extension.
	 *
	 * @param path the path
	 * @return the filename extension
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}

	
	/**
	 * Strip filename extension.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	
	/**
	 * Apply relative path.
	 *
	 * @param path the path
	 * @param relativePath the relative path
	 * @return the string
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	
	/**
	 * Clean path.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		
		
		
		
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				
			} else if (TOP_PATH.equals(element)) {
				
				tops++;
			} else {
				if (tops > 0) {
					
					tops--;
				} else {
					
					pathElements.add(0, element);
				}
			}
		}

		
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	
	/**
	 * Path equals.
	 *
	 * @param path1 the path1
	 * @param path2 the path2
	 * @return true, if successful
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	
	/**
	 * Parses the locale string.
	 *
	 * @param localeString the locale string
	 * @return the locale
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = "";
		if (parts.length >= 2) {
			
			
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

	
	/**
	 * To language tag.
	 *
	 * @param locale the locale
	 * @return the string
	 */
	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}

	
	
	

	
	/**
	 * Adds the string to array.
	 *
	 * @param array the array
	 * @param str the str
	 * @return the string[]
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	
	/**
	 * Concatenate string arrays.
	 *
	 * @param array1 the array1
	 * @param array2 the array2
	 * @return the string[]
	 */
	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if (isEmpty(array1)) {
			return array2;
		}
		if (isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	
	/**
	 * Merge string arrays.
	 *
	 * @param array1 the array1
	 * @param array2 the array2
	 * @return the string[]
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (isEmpty(array1)) {
			return array2;
		}
		if (isEmpty(array2)) {
			return array1;
		}
		List<String> result = new ArrayList<String>();
		result.addAll(Arrays.asList(array1));
		for (String str : array2) {
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	
	/**
	 * Sort string array.
	 *
	 * @param array the array
	 * @return the string[]
	 */
	public static String[] sortStringArray(String[] array) {
		if (isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	
	/**
	 * To string array.
	 *
	 * @param collection the collection
	 * @return the string[]
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	
	/**
	 * To string array.
	 *
	 * @param enumeration the enumeration
	 * @return the string[]
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		if (enumeration == null) {
			return null;
		}
		List<String> list = Collections.list(enumeration);
		return list.toArray(new String[list.size()]);
	}

	
	/**
	 * Trim array elements.
	 *
	 * @param array the array
	 * @return the string[]
	 */
	public static String[] trimArrayElements(String[] array) {
		if (isEmpty(array)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	
	/**
	 * Removes the duplicate strings.
	 *
	 * @param array the array
	 * @return the string[]
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (isEmpty(array)) {
			return array;
		}
		Set<String> set = new TreeSet<String>();
		set.addAll(Arrays.asList(array));
		return toStringArray(set);
	}

	
	/**
	 * Split string by comma to set.
	 *
	 * @param s the s
	 * @return the sets the
	 */
	public static Set<String> splitStringByCommaToSet(final String s) {
		return splitStringToSet(s, ',');
	}

	
	/**
	 * Split string by comma to array.
	 *
	 * @param s the s
	 * @return the string[]
	 */
	public static String[] splitStringByCommaToArray(final String s) {
		return splitStringToArray(s, ',');
	}

	
	/**
	 * Split string to set.
	 *
	 * @param s the s
	 * @param c the c
	 * @return the sets the
	 */
	public static Set<String> splitStringToSet(final String s, final char c) {
		final char[] chars = s.toCharArray();
		int count = 1;
		for (final char x : chars) {
			if (x == c) {
				count++;
			}
		}
		final THashSet<String> result = new THashSet<String>(count);
		final int len = chars.length;
		int start = 0; 
		int pos = 0; 
		for (; pos < len; pos++) {
			if (chars[pos] == c) {
				int size = pos - start;
				if (size > 0) { 
					result.add(new String(chars, start, size));
				}
				start = pos + 1;
			}
		}
		int size = pos - start;
		if (size > 0) {
			result.add(new String(chars, start, size));
		}
		return result;
	}

	
	/**
	 * Split string to array.
	 *
	 * @param s the s
	 * @param c the c
	 * @return the string[]
	 */
	public static String[] splitStringToArray(final String s, final char c) {
		if (s.length() == 0) {
			return Strings.EMPTY_ARRAY;
		}
		final char[] chars = s.toCharArray();
		int count = 1;
		for (final char x : chars) {
			if (x == c) {
				count++;
			}
		}
		final String[] result = new String[count];
		final int len = chars.length;
		int start = 0; 
		int pos = 0; 
		int i = 0; 
		for (; pos < len; pos++) {
			if (chars[pos] == c) {
				int size = pos - start;
				if (size > 0) {
					result[i++] = new String(chars, start, size);
				}
				start = pos + 1;
			}
		}
		int size = pos - start;
		if (size > 0) {
			result[i++] = new String(chars, start, size);
		}
		if (i != count) {
			
			String[] result1 = new String[i];
			System.arraycopy(result, 0, result1, 0, i);
			return result1;
		}
		return result;
	}

	
	/**
	 * Split.
	 *
	 * @param toSplit the to split
	 * @param delimiter the delimiter
	 * @return the string[]
	 */
	public static String[] split(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	
	/**
	 * Split array elements into properties.
	 *
	 * @param array the array
	 * @param delimiter the delimiter
	 * @return the properties
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	
	/**
	 * Split array elements into properties.
	 *
	 * @param array the array
	 * @param delimiter the delimiter
	 * @param charsToDelete the chars to delete
	 * @return the properties
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (String element : array) {
			if (charsToDelete != null) {
				element = deleteAny(element, charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	
	/**
	 * Tokenize to string array.
	 *
	 * @param str the str
	 * @param delimiters the delimiters
	 * @return the string[]
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	
	/**
	 * Tokenize to string array.
	 *
	 * @param str the str
	 * @param delimiters the delimiters
	 * @param trimTokens the trim tokens
	 * @param ignoreEmptyTokens the ignore empty tokens
	 * @return the string[]
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	
	/**
	 * Delimited list to string array.
	 *
	 * @param str the str
	 * @param delimiter the delimiter
	 * @return the string[]
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	
	/**
	 * Delimited list to string array.
	 *
	 * @param str the str
	 * @param delimiter the delimiter
	 * @param charsToDelete the chars to delete
	 * @return the string[]
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	
	/**
	 * Comma delimited list to string array.
	 *
	 * @param str the str
	 * @return the string[]
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	
	/**
	 * Comma delimited list to set.
	 *
	 * @param str the str
	 * @return the sets the
	 */
	public static Set<String> commaDelimitedListToSet(String str) {
		Set<String> set = new TreeSet<String>();
		String[] tokens = commaDelimitedListToStringArray(str);
		set.addAll(Arrays.asList(tokens));
		return set;
	}

	
	/**
	 * Collection to delimited string.
	 *
	 * @param coll the coll
	 * @param delim the delim
	 * @param prefix the prefix
	 * @param suffix the suffix
	 * @return the string
	 */
	public static String collectionToDelimitedString(Iterable<?> coll, String delim, String prefix, String suffix) {
		if (Iterables.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	
	/**
	 * Collection to delimited string.
	 *
	 * @param coll the coll
	 * @param delim the delim
	 * @return the string
	 */
	public static String collectionToDelimitedString(Iterable<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	
	/**
	 * Collection to comma delimited string.
	 *
	 * @param coll the coll
	 * @return the string
	 */
	public static String collectionToCommaDelimitedString(Iterable<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	
	/**
	 * Array to delimited string.
	 *
	 * @param arr the arr
	 * @param delim the delim
	 * @return the string
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (isEmpty(arr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	
	/**
	 * Array to comma delimited string.
	 *
	 * @param arr the arr
	 * @return the string
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	
	/**
	 * Format1 decimals.
	 *
	 * @param value the value
	 * @param suffix the suffix
	 * @return the string
	 */
	public static String format1Decimals(double value, String suffix) {
		String p = String.valueOf(value);
		int ix = p.indexOf('.') + 1;
		int ex = p.indexOf('E');
		char fraction = p.charAt(ix);
		if (fraction == '0') {
			if (ex != -1) {
				return p.substring(0, ix - 1) + p.substring(ex) + suffix;
			} else {
				return p.substring(0, ix - 1) + suffix;
			}
		} else {
			if (ex != -1) {
				return p.substring(0, ix) + fraction + p.substring(ex) + suffix;
			} else {
				return p.substring(0, ix) + fraction + suffix;
			}
		}
	}

	
	/**
	 * To camel case.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String toCamelCase(String value) {
		return toCamelCase(value, null);
	}

	
	/**
	 * To camel case.
	 *
	 * @param value the value
	 * @param sb the sb
	 * @return the string
	 */
	public static String toCamelCase(String value, StringBuilder sb) {
		boolean changed = false;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '_') {
				if (!changed) {
					if (sb != null) {
						sb.setLength(0);
					} else {
						sb = new StringBuilder();
					}
					
					for (int j = 0; j < i; j++) {
						sb.append(value.charAt(j));
					}
					changed = true;
				}
				sb.append(Character.toUpperCase(value.charAt(++i)));
			} else {
				if (changed) {
					sb.append(c);
				}
			}
		}
		if (!changed) {
			return value;
		}
		return sb.toString();
	}

	
	/**
	 * To underscore case.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String toUnderscoreCase(String value) {
		return toUnderscoreCase(value, null);
	}

	
	/**
	 * To underscore case.
	 *
	 * @param value the value
	 * @param sb the sb
	 * @return the string
	 */
	public static String toUnderscoreCase(String value, StringBuilder sb) {
		boolean changed = false;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (Character.isUpperCase(c)) {
				if (!changed) {
					if (sb != null) {
						sb.setLength(0);
					} else {
						sb = new StringBuilder();
					}
					
					for (int j = 0; j < i; j++) {
						sb.append(value.charAt(j));
					}
					changed = true;
					if (i == 0) {
						sb.append(Character.toLowerCase(c));
					} else {
						sb.append('_');
						sb.append(Character.toLowerCase(c));
					}
				} else {
					sb.append('_');
					sb.append(Character.toLowerCase(c));
				}
			} else {
				if (changed) {
					sb.append(c);
				}
			}
		}
		if (!changed) {
			return value;
		}
		return sb.toString();
	}

	
	/**
	 * Checks if is empty.
	 *
	 * @param array the array
	 * @return true, if is empty
	 */
	private static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	
	/**
	 * Checks if is empty.
	 *
	 * @param collection the collection
	 * @return true, if is empty
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	
	/**
	 * Instantiates a new strings.
	 */
	private Strings() {

	}
}
