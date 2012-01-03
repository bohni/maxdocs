package org.maxdocs.parser;

import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;

/**
 * MarkupLanguageFactory:
 * TODO 02.01.2012, K264226: Dokumentation ergänzen
 *
 * @author K264226
 */
public interface MarkupLanguageFactory
{
	public MarkupLanguage getMarkupLanguage(String markupLanguageBeanName);
}
