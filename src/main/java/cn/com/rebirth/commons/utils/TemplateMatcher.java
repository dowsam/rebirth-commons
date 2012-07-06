/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-core TemplateMatcher.java 2012-3-23 17:34:34 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class TemplateMatcher.
 *
 * @author l.xue.nong
 */
public class TemplateMatcher {
	
	/** The pattern. */
	private Pattern pattern;

	/**
	 * Instantiates a new template matcher.
	 *
	 * @param leftBrace the left brace
	 * @param rightBrace the right brace
	 */
	public TemplateMatcher(String leftBrace, String rightBrace) {
		leftBrace = Pattern.quote(leftBrace);
		rightBrace = Pattern.quote(rightBrace);
		pattern = Pattern.compile(leftBrace + "(.*?)" + rightBrace);
	}

	/**
	 * Instantiates a new template matcher.
	 *
	 * @param prefix the prefix
	 */
	public TemplateMatcher(String prefix) {
		prefix = Pattern.quote(prefix);
		pattern = Pattern.compile(prefix + "(\\w*)");
	}

	/*-------------------------------------------------[ Replace ]---------------------------------------------------*/

	/**
	 * Replace.
	 *
	 * @param input the input
	 * @param resolver the resolver
	 * @return the string
	 */
	public String replace(CharSequence input, VariableResolver resolver) {
		StringBuilder buff = new StringBuilder();

		Matcher matcher = pattern.matcher(input);
		int cursor = 0;
		while (cursor < input.length() && matcher.find(cursor)) {
			buff.append(input.subSequence(cursor, matcher.start()));
			String value = resolver.resolve(matcher.group(1));
			buff.append(value != null ? value : matcher.group());
			cursor = matcher.end();
		}
		buff.append(input.subSequence(cursor, input.length()));
		return buff.toString();
	}

	/**
	 * Replace.
	 *
	 * @param input the input
	 * @param variables the variables
	 * @return the string
	 */
	public String replace(String input, final Map<String, String> variables) {
		return replace(input, new MapVariableResolver(variables));
	}

	/*-------------------------------------------------[ Character Streams ]---------------------------------------------------*/

	/**
	 * Replace.
	 *
	 * @param reader the reader
	 * @param writer the writer
	 * @param resolver the resolver
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void replace(Reader reader, Writer writer, VariableResolver resolver) throws IOException {
		BufferedReader breader = reader instanceof BufferedReader ? (BufferedReader) reader
				: new BufferedReader(reader);
		BufferedWriter bwriter = writer instanceof BufferedWriter ? (BufferedWriter) writer
				: new BufferedWriter(writer);
		try {
			boolean firstLine = true;
			for (String line; (line = breader.readLine()) != null;) {
				if (firstLine)
					firstLine = false;
				else
					bwriter.newLine();
				bwriter.write(replace(line, resolver));
			}
		} finally {
			try {
				breader.close();
			} finally {
				bwriter.close();
			}
		}
	}

	/**
	 * Replace.
	 *
	 * @param reader the reader
	 * @param writer the writer
	 * @param variables the variables
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void replace(Reader reader, Writer writer, Map<String, String> variables) throws IOException {
		replace(reader, writer, new MapVariableResolver(variables));
	}

	/*-------------------------------------------------[ VariableResolver ]---------------------------------------------------*/

	/**
	 * The Interface VariableResolver.
	 *
	 * @author l.xue.nong
	 */
	public static interface VariableResolver {
		
		/**
		 * Resolve.
		 *
		 * @param variable the variable
		 * @return the string
		 */
		public String resolve(String variable);
	}

	/**
	 * The Class MapVariableResolver.
	 *
	 * @author l.xue.nong
	 */
	public static class MapVariableResolver implements VariableResolver {
		
		/** The variables. */
		private Map<String, String> variables;

		/**
		 * Instantiates a new map variable resolver.
		 *
		 * @param variables the variables
		 */
		public MapVariableResolver(Map<String, String> variables) {
			this.variables = variables;
		}

		/* (non-Javadoc)
		 * @see com.infolion.utils.TemplateMatcher.VariableResolver#resolve(java.lang.String)
		 */
		@Override
		public String resolve(String variable) {
			return variables.get(variable);
		}
	}

	/*-------------------------------------------------[ Testing ]---------------------------------------------------*/

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(new TemplateMatcher("${", "}").replace(
				"this is ${santhosh}ghgjh\n ${kumar} sdf ${tekuri}abc", new VariableResolver() {
					@Override
					public String resolve(String variable) {
						if (variable.equals("santhosh"))
							return null;
						return variable.toUpperCase();
					}
				}));

		System.out.println(new TemplateMatcher("$").replace("this is $santhosh ghgjh\n $kumar sdf $tekuri\n$ abc",
				new VariableResolver() {
					@Override
					public String resolve(String variable) {
						return variable.toUpperCase();
					}
				}));
	}
}
