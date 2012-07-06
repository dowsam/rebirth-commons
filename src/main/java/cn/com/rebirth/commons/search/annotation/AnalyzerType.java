/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AnalyzerType.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;


/**
 * The Enum AnalyzerType.
 *
 * @author l.xue.nong
 */
public enum AnalyzerType {

	/** The indonesian. */
	INDONESIAN("indonesian", "org.apache.lucene.analysis.id.IndonesianAnalyzer"),

	/** The arabic. */
	ARABIC("arabic", "org.apache.lucene.analysis.ar.ArabicAnalyzer"),

	/** The stop. */
	STOP("stop", "org.apache.lucene.analysis.StopAnalyzer"),

	/** The finnish. */
	FINNISH("finnish", "org.apache.lucene.analysis.fi.FinnishAnalyzer"),

	/** The italian. */
	ITALIAN("italian", "org.apache.lucene.analysis.it.ItalianAnalyzer"),

	/** The persian. */
	PERSIAN("persian", "org.apache.lucene.analysis.fa.PersianAnalyzer"),

	/** The simple. */
	SIMPLE("simple", "org.apache.lucene.analysis.SimpleAnalyzer"),

	/** The thai. */
	THAI("thai", "org.apache.lucene.analysis.th.ThaiAnalyzer"),

	/** The catalan. */
	CATALAN("catalan", "org.apache.lucene.analysis.ca.CatalanAnalyzer"),

	/** The french. */
	FRENCH("french", "org.apache.lucene.analysis.fr.FrenchAnalyzer"),

	/** The brazilian. */
	BRAZILIAN("brazilian", "org.apache.lucene.analysis.br.BrazilianAnalyzer"),

	/** The chinese. */
	CHINESE("chinese", "org.apache.lucene.analysis.cn.ChineseAnalyzer"),

	/** The portuguese. */
	PORTUGUESE("portuguese", "org.apache.lucene.analysis.pt.PortugueseAnalyzer"),

	/** The dutch. */
	DUTCH("dutch", "org.apache.lucene.analysis.nl.DutchAnalyzer"),

	/** The hindi. */
	HINDI("hindi", "org.apache.lucene.analysis.hi.HindiAnalyzer"),

	/** The classic. */
	CLASSIC("classic", "org.apache.lucene.analysis.standard.ClassicAnalyzer"),

	/** The spanish. */
	SPANISH("spanish", "org.apache.lucene.analysis.es.SpanishAnalyzer"),

	/** The armenian. */
	ARMENIAN("armenian", "org.apache.lucene.analysis.hy.ArmenianAnalyzer"),

	/** The standard. */
	STANDARD("standard", "org.apache.lucene.analysis.standard.StandardAnalyzer"),

	/** The greek. */
	GREEK("greek", "org.apache.lucene.analysis.el.GreekAnalyzer"),

	/** The swedish. */
	SWEDISH("swedish", "org.apache.lucene.analysis.sv.SwedishAnalyzer"),

	/** The keyword. */
	KEYWORD("keyword", "org.apache.lucene.analysis.KeywordAnalyzer"),

	/** The german. */
	GERMAN("german", "org.apache.lucene.analysis.de.GermanAnalyzer"),

	/** The romanian. */
	ROMANIAN("romanian", "org.apache.lucene.analysis.ro.RomanianAnalyzer"),

	/** The snowball. */
	SNOWBALL("snowball", "org.apache.lucene.analysis.snowball.SnowballAnalyzer"),

	/** The whitespace. */
	WHITESPACE("whitespace", "org.apache.lucene.analysis.WhitespaceAnalyzer"),

	/** The standard html strip. */
	STANDARD_HTML_STRIP("standard_html_strip", "cn.com.rebirth.search.core.index.analysis.StandardHtmlStripAnalyzer"),

	/** The czech. */
	CZECH("czech", "org.apache.lucene.analysis.cz.CzechAnalyzer"),

	/** The standardhtmlstrip. */
	STANDARDHTMLSTRIP("standard_html_strip", "cn.com.rebirth.search.core.index.analysis.StandardHtmlStripAnalyzer"),

	/** The turkish. */
	TURKISH("turkish", "org.apache.lucene.analysis.tr.TurkishAnalyzer"),

	/** The hungarian. */
	HUNGARIAN("hungarian", "org.apache.lucene.analysis.hu.HungarianAnalyzer"),

	/** The galician. */
	GALICIAN("galician", "org.apache.lucene.analysis.gl.GalicianAnalyzer"),

	/** The cjk. */
	CJK("cjk", "org.apache.lucene.analysis.cjk.CJKAnalyzer"),

	/** The default. */
	DEFAULT("default", "cn.com.rebirth.analyzer.support.rebirthAnalyzer"),

	/** The pattern. */
	PATTERN("pattern", "org.apache.lucene.analysis.miscellaneous.PatternAnalyzer"),

	/** The norwegian. */
	NORWEGIAN("norwegian", "org.apache.lucene.analysis.no.NorwegianAnalyzer"),

	/** The russian. */
	RUSSIAN("russian", "org.apache.lucene.analysis.ru.RussianAnalyzer"),

	/** The danish. */
	DANISH("danish", "org.apache.lucene.analysis.da.DanishAnalyzer"),

	/** The basque. */
	BASQUE("basque", "org.apache.lucene.analysis.eu.BasqueAnalyzer"),

	/** The bulgarian. */
	BULGARIAN("bulgarian", "org.apache.lucene.analysis.bg.BulgarianAnalyzer"),

	/** The english. */
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
	 * @param <T> the generic type
	 * @return the t
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
