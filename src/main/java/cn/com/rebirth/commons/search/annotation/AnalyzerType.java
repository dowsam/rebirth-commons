/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons AnalyzerType.java 2012-4-18 14:08:23 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;


/**
 * The Enum AnalyzerType.
 *
 * @author l.xue.nong
 */
public enum AnalyzerType {

	/** The INDONESIAN. */
	INDONESIAN("indonesian", "org.apache.lucene.analysis.id.IndonesianAnalyzer"),

	/** The ARABIC. */
	ARABIC("arabic", "org.apache.lucene.analysis.ar.ArabicAnalyzer"),

	/** The STOP. */
	STOP("stop", "org.apache.lucene.analysis.StopAnalyzer"),

	/** The FINNISH. */
	FINNISH("finnish", "org.apache.lucene.analysis.fi.FinnishAnalyzer"),

	/** The ITALIAN. */
	ITALIAN("italian", "org.apache.lucene.analysis.it.ItalianAnalyzer"),

	/** The PERSIAN. */
	PERSIAN("persian", "org.apache.lucene.analysis.fa.PersianAnalyzer"),

	/** The SIMPLE. */
	SIMPLE("simple", "org.apache.lucene.analysis.SimpleAnalyzer"),

	/** The THAI. */
	THAI("thai", "org.apache.lucene.analysis.th.ThaiAnalyzer"),

	/** The CATALAN. */
	CATALAN("catalan", "org.apache.lucene.analysis.ca.CatalanAnalyzer"),

	/** The FRENCH. */
	FRENCH("french", "org.apache.lucene.analysis.fr.FrenchAnalyzer"),

	/** The BRAZILIAN. */
	BRAZILIAN("brazilian", "org.apache.lucene.analysis.br.BrazilianAnalyzer"),

	/** The CHINESE. */
	CHINESE("chinese", "org.apache.lucene.analysis.cn.ChineseAnalyzer"),

	/** The PORTUGUESE. */
	PORTUGUESE("portuguese", "org.apache.lucene.analysis.pt.PortugueseAnalyzer"),

	/** The DUTCH. */
	DUTCH("dutch", "org.apache.lucene.analysis.nl.DutchAnalyzer"),

	/** The HINDI. */
	HINDI("hindi", "org.apache.lucene.analysis.hi.HindiAnalyzer"),

	/** The CLASSIC. */
	CLASSIC("classic", "org.apache.lucene.analysis.standard.ClassicAnalyzer"),

	/** The SPANISH. */
	SPANISH("spanish", "org.apache.lucene.analysis.es.SpanishAnalyzer"),

	/** The ARMENIAN. */
	ARMENIAN("armenian", "org.apache.lucene.analysis.hy.ArmenianAnalyzer"),

	/** The STANDARD. */
	STANDARD("standard", "org.apache.lucene.analysis.standard.StandardAnalyzer"),

	/** The GREEK. */
	GREEK("greek", "org.apache.lucene.analysis.el.GreekAnalyzer"),

	/** The SWEDISH. */
	SWEDISH("swedish", "org.apache.lucene.analysis.sv.SwedishAnalyzer"),

	/** The KEYWORD. */
	KEYWORD("keyword", "org.apache.lucene.analysis.KeywordAnalyzer"),

	/** The GERMAN. */
	GERMAN("german", "org.apache.lucene.analysis.de.GermanAnalyzer"),

	/** The ROMANIAN. */
	ROMANIAN("romanian", "org.apache.lucene.analysis.ro.RomanianAnalyzer"),

	/** The SNOWBALL. */
	SNOWBALL("snowball", "org.apache.lucene.analysis.snowball.SnowballAnalyzer"),

	/** The WHITESPACE. */
	WHITESPACE("whitespace", "org.apache.lucene.analysis.WhitespaceAnalyzer"),

	/** The STANDAR d_ htm l_ strip. */
	STANDARD_HTML_STRIP("standard_html_strip", "cn.com.summall.search.core.index.analysis.StandardHtmlStripAnalyzer"),

	/** The CZECH. */
	CZECH("czech", "org.apache.lucene.analysis.cz.CzechAnalyzer"),

	/** The STANDARDHTMLSTRIP. */
	STANDARDHTMLSTRIP("standard_html_strip", "cn.com.summall.search.core.index.analysis.StandardHtmlStripAnalyzer"),

	/** The TURKISH. */
	TURKISH("turkish", "org.apache.lucene.analysis.tr.TurkishAnalyzer"),

	/** The HUNGARIAN. */
	HUNGARIAN("hungarian", "org.apache.lucene.analysis.hu.HungarianAnalyzer"),

	/** The GALICIAN. */
	GALICIAN("galician", "org.apache.lucene.analysis.gl.GalicianAnalyzer"),

	/** The CJK. */
	CJK("cjk", "org.apache.lucene.analysis.cjk.CJKAnalyzer"),

	/** The DEFAULT. */
	DEFAULT("default", "cn.com.summall.analyzer.support.SumMallAnalyzer"),

	/** The PATTERN. */
	PATTERN("pattern", "org.apache.lucene.analysis.miscellaneous.PatternAnalyzer"),

	/** The NORWEGIAN. */
	NORWEGIAN("norwegian", "org.apache.lucene.analysis.no.NorwegianAnalyzer"),

	/** The RUSSIAN. */
	RUSSIAN("russian", "org.apache.lucene.analysis.ru.RussianAnalyzer"),

	/** The DANISH. */
	DANISH("danish", "org.apache.lucene.analysis.da.DanishAnalyzer"),

	/** The BASQUE. */
	BASQUE("basque", "org.apache.lucene.analysis.eu.BasqueAnalyzer"),

	/** The BULGARIAN. */
	BULGARIAN("bulgarian", "org.apache.lucene.analysis.bg.BulgarianAnalyzer"),

	/** The ENGLISH. */
	ENGLISH("english", "org.apache.lucene.analysis.en.EnglishAnalyzer");

	/** The name. */
	private final String name;

	/** The class name. */
	private final String className;

	/**
	 * Instantiates a new analyzer type.
	 *
	 * @param name the name
	 * @param className the class name
	 */
	AnalyzerType(String name, String className) {
		this.name = name;
		this.className = className;
	}

	/**
	 * New analyzer.
	 *
	 * @return the analyzer
	 */
	@SuppressWarnings("unchecked")
	public <T> T newAnalyzer() {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return "name:" + getName() + ",className:" + getClassName();
	}

}
