package org.maxdocs.parser;

import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;

/**
 * MarkupLanguageFactory:
 * Factory bean for creating a {@link MarkupLanguage}.
 * 
 * @author Team maxdocs.org
 */
public interface MarkupLanguageFactory
{
	/**
	 * getMarkupLanguage:
	 * Returns the MarkupLanguage object to the given parameter
	 * 
	 * @param markupLanguageBeanName the bean name of the markupLanguage
	 * @return a {@link MarkupLanguage} object
	 */
	MarkupLanguage getMarkupLanguage(String markupLanguageBeanName);
}
