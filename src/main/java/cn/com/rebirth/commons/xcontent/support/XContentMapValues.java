/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentMapValues.java 2012-7-6 10:23:45 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.rebirth.commons.Strings;
import cn.com.rebirth.commons.regex.Regex;
import cn.com.rebirth.commons.unit.TimeValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The Class XContentMapValues.
 *
 * @author l.xue.nong
 */
public class XContentMapValues {

	
	/**
	 * Extract raw values.
	 *
	 * @param path the path
	 * @param map the map
	 * @return the list
	 */
	public static List<Object> extractRawValues(String path, Map<String, Object> map) {
		List<Object> values = Lists.newArrayList();
		String[] pathElements = Strings.splitStringToArray(path, '.');
		if (pathElements.length == 0) {
			return values;
		}
		extractRawValues(values, map, pathElements, 0);
		return values;
	}

	
	/**
	 * Extract raw values.
	 *
	 * @param values the values
	 * @param part the part
	 * @param pathElements the path elements
	 * @param index the index
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void extractRawValues(List values, Map<String, Object> part, String[] pathElements, int index) {
		if (index == pathElements.length) {
			return;
		}
		String currentPath = pathElements[index];
		Object currentValue = part.get(currentPath);
		if (currentValue == null) {
			return;
		}
		if (currentValue instanceof Map) {
			extractRawValues(values, (Map<String, Object>) currentValue, pathElements, index + 1);
		} else if (currentValue instanceof List) {
			extractRawValues(values, (List) currentValue, pathElements, index + 1);
		} else {
			values.add(currentValue);
		}
	}

	
	/**
	 * Extract raw values.
	 *
	 * @param values the values
	 * @param part the part
	 * @param pathElements the path elements
	 * @param index the index
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void extractRawValues(List values, List<Object> part, String[] pathElements, int index) {
		for (Object value : part) {
			if (value == null) {
				continue;
			}
			if (value instanceof Map) {
				extractRawValues(values, (Map<String, Object>) value, pathElements, index);
			} else if (value instanceof List) {
				extractRawValues(values, (List) value, pathElements, index);
			} else {
				values.add(value);
			}
		}
	}

	
	/**
	 * Extract value.
	 *
	 * @param path the path
	 * @param map the map
	 * @return the object
	 */
	public static Object extractValue(String path, Map<String, Object> map) {
		String[] pathElements = Strings.splitStringToArray(path, '.');
		if (pathElements.length == 0) {
			return null;
		}
		return extractValue(pathElements, 0, map);
	}

	
	/**
	 * Extract value.
	 *
	 * @param pathElements the path elements
	 * @param index the index
	 * @param currentValue the current value
	 * @return the object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object extractValue(String[] pathElements, int index, Object currentValue) {
		if (index == pathElements.length) {
			return currentValue;
		}
		if (currentValue == null) {
			return null;
		}
		if (currentValue instanceof Map) {
			Map map = (Map) currentValue;
			return extractValue(pathElements, index + 1, map.get(pathElements[index]));
		}
		if (currentValue instanceof List) {
			List valueList = (List) currentValue;
			List newList = new ArrayList(valueList.size());
			for (Object o : valueList) {
				Object listValue = extractValue(pathElements, index, o);
				if (listValue != null) {
					newList.add(listValue);
				}
			}
			return newList;
		}
		return null;
	}

	
	/**
	 * Filter.
	 *
	 * @param map the map
	 * @param includes the includes
	 * @param excludes the excludes
	 * @return the map
	 */
	public static Map<String, Object> filter(Map<String, Object> map, String[] includes, String[] excludes) {
		Map<String, Object> result = Maps.newHashMap();
		filter(map, result, includes, excludes, new StringBuilder());
		return result;
	}

	
	/**
	 * Filter.
	 *
	 * @param map the map
	 * @param into the into
	 * @param includes the includes
	 * @param excludes the excludes
	 * @param sb the sb
	 */
	@SuppressWarnings("unchecked")
	private static void filter(Map<String, Object> map, Map<String, Object> into, String[] includes, String[] excludes,
			StringBuilder sb) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			int mark = sb.length();
			if (sb.length() > 0) {
				sb.append('.');
			}
			sb.append(key);
			String path = sb.toString();
			boolean excluded = false;
			for (String exclude : excludes) {
				if (Regex.simpleMatch(exclude, path)) {
					excluded = true;
					break;
				}
			}
			if (excluded) {
				sb.setLength(mark);
				continue;
			}
			if (includes.length > 0) {
				boolean atLeastOnOneIncludeMatched = false;
				for (String include : includes) {
					
					
					if (include.startsWith(path) || Regex.simpleMatch(include, path)) {
						atLeastOnOneIncludeMatched = true;
						break;
					}
				}
				if (!atLeastOnOneIncludeMatched) {
					sb.setLength(mark);
					continue;
				}
			}

			if (entry.getValue() instanceof Map) {
				Map<String, Object> innerInto = Maps.newHashMap();
				filter((Map<String, Object>) entry.getValue(), innerInto, includes, excludes, sb);
				if (!innerInto.isEmpty()) {
					into.put(entry.getKey(), innerInto);
				}
			} else if (entry.getValue() instanceof List) {
				List<Object> list = (List<Object>) entry.getValue();
				List<Object> innerInto = new ArrayList<Object>(list.size());
				filter(list, innerInto, includes, excludes, sb);
				into.put(entry.getKey(), innerInto);
			} else {
				into.put(entry.getKey(), entry.getValue());
			}
			sb.setLength(mark);
		}
	}

	
	/**
	 * Filter.
	 *
	 * @param from the from
	 * @param to the to
	 * @param includes the includes
	 * @param excludes the excludes
	 * @param sb the sb
	 */
	@SuppressWarnings("unchecked")
	private static void filter(List<Object> from, List<Object> to, String[] includes, String[] excludes,
			StringBuilder sb) {
		for (Object o : from) {
			if (o instanceof Map) {
				Map<String, Object> innerInto = Maps.newHashMap();
				filter((Map<String, Object>) o, innerInto, includes, excludes, sb);
				if (!innerInto.isEmpty()) {
					to.add(innerInto);
				}
			} else if (o instanceof List) {
				List<Object> innerInto = new ArrayList<Object>();
				filter((List<Object>) o, innerInto, includes, excludes, sb);
			} else {
				to.add(o);
			}
		}
	}

	
	/**
	 * Checks if is object.
	 *
	 * @param node the node
	 * @return true, if is object
	 */
	public static boolean isObject(Object node) {
		return node instanceof Map;
	}

	
	/**
	 * Checks if is array.
	 *
	 * @param node the node
	 * @return true, if is array
	 */
	public static boolean isArray(Object node) {
		return node instanceof List;
	}

	
	/**
	 * Node string value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the string
	 */
	public static String nodeStringValue(Object node, String defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return node.toString();
	}

	
	/**
	 * Node float value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the float
	 */
	public static float nodeFloatValue(Object node, float defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeFloatValue(node);
	}

	
	/**
	 * Node float value.
	 *
	 * @param node the node
	 * @return the float
	 */
	public static float nodeFloatValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).floatValue();
		}
		return Float.parseFloat(node.toString());
	}

	
	/**
	 * Node double value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the double
	 */
	public static double nodeDoubleValue(Object node, double defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeDoubleValue(node);
	}

	
	/**
	 * Node double value.
	 *
	 * @param node the node
	 * @return the double
	 */
	public static double nodeDoubleValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).doubleValue();
		}
		return Double.parseDouble(node.toString());
	}

	
	/**
	 * Node integer value.
	 *
	 * @param node the node
	 * @return the int
	 */
	public static int nodeIntegerValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).intValue();
		}
		return Integer.parseInt(node.toString());
	}

	
	/**
	 * Node integer value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the int
	 */
	public static int nodeIntegerValue(Object node, int defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		if (node instanceof Number) {
			return ((Number) node).intValue();
		}
		return Integer.parseInt(node.toString());
	}

	
	/**
	 * Node short value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the short
	 */
	public static short nodeShortValue(Object node, short defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeShortValue(node);
	}

	
	/**
	 * Node short value.
	 *
	 * @param node the node
	 * @return the short
	 */
	public static short nodeShortValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).shortValue();
		}
		return Short.parseShort(node.toString());
	}

	
	/**
	 * Node byte value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the byte
	 */
	public static byte nodeByteValue(Object node, byte defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeByteValue(node);
	}

	
	/**
	 * Node byte value.
	 *
	 * @param node the node
	 * @return the byte
	 */
	public static byte nodeByteValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).byteValue();
		}
		return Byte.parseByte(node.toString());
	}

	
	/**
	 * Node long value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the long
	 */
	public static long nodeLongValue(Object node, long defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeLongValue(node);
	}

	
	/**
	 * Node long value.
	 *
	 * @param node the node
	 * @return the long
	 */
	public static long nodeLongValue(Object node) {
		if (node instanceof Number) {
			return ((Number) node).longValue();
		}
		return Long.parseLong(node.toString());
	}

	
	/**
	 * Node boolean value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return true, if successful
	 */
	public static boolean nodeBooleanValue(Object node, boolean defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeBooleanValue(node);
	}

	
	/**
	 * Node boolean value.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	public static boolean nodeBooleanValue(Object node) {
		if (node instanceof Boolean) {
			return (Boolean) node;
		}
		if (node instanceof Number) {
			return ((Number) node).intValue() != 0;
		}
		String value = node.toString();
		return !(value.equals("false") || value.equals("0") || value.equals("off"));
	}

	
	/**
	 * Node time value.
	 *
	 * @param node the node
	 * @param defaultValue the default value
	 * @return the time value
	 */
	public static TimeValue nodeTimeValue(Object node, TimeValue defaultValue) {
		if (node == null) {
			return defaultValue;
		}
		return nodeTimeValue(node);
	}

	
	/**
	 * Node time value.
	 *
	 * @param node the node
	 * @return the time value
	 */
	public static TimeValue nodeTimeValue(Object node) {
		if (node instanceof Number) {
			return TimeValue.timeValueMillis(((Number) node).longValue());
		}
		return TimeValue.parseTimeValue(node.toString(), null);
	}
}
