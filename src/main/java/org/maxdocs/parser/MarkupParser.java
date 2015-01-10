package org.maxdocs.parser;

import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;

/**
 * MarkupParser:
 * Interface for markup parsers of MaxDocs.
 * 
 * @author Team maxdocs.org
 */
public interface MarkupParser
{
	/**
	 * parseToHtml: Parses the given {@link MarkupPage} to a {@link HtmlPage}
	 * 
	 * @param markupPage the MarkupPage to parse
	 * @return the resulting HtmlPage
	 */
	HtmlPage parseToHtml(MarkupPage markupPage);


	/**
	 * getMarkupLanguages: Returns a Map with the MarkupLanguage objects
	 * The key value is the markup language name, value is the corresponding object
	 * 
	 * @return the markup languages map
	 */
	Map<String, String> getMarkupLanguages();
}
