package org.maxdocs.parser;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MarkupParserImpl:
 * 
 * @author Team maxdocs.org
 */
public class MarkupParserImpl implements MarkupParser
{
	private static Logger log = LoggerFactory.getLogger(MarkupParserImpl.class);
	private Map<String, String> languageMapping = new HashMap<String, String>();
	private MarkupLanguageFactory markupLanguageFactory;


	/* (non-Javadoc)
	 * @see org.maxdocs.parser.MarkupParser#parseToHtml(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public HtmlPage parseToHtml(MarkupPage markupPage)
	{
		log.trace("parseToHtml", markupPage.getPagePath());
		HtmlPage htmlPage = new HtmlPage(markupPage);

		String languageName = markupPage.getMarkupLanguage();
		if (languageMapping.containsKey(languageName))
		{
			org.eclipse.mylyn.wikitext.core.parser.MarkupParser parser = new org.eclipse.mylyn.wikitext.core.parser.MarkupParser();
			String languageBeanName = languageMapping.get(languageName);
			MarkupLanguage language = markupLanguageFactory.getMarkupLanguage(languageBeanName);
			parser.setMarkupLanguage(language);
			StringWriter out = new StringWriter();
			parser.setBuilder(new HtmlDocumentBuilder(out));
			parser.parse(markupPage.getContent(), false);
			htmlPage.setContent(out.toString());
		}
		else
		{
			htmlPage.setContent("<b>error: markup-language " + languageName + " not supported</b>");
		}

		return htmlPage;
	}


	/**
	 * setMarkupLanguages(): The Spring container will inject a Map with the bean name as key and
	 * the bean type as value of all MarkupLanguage-beans defined in the Spring configuration.
	 * 
	 * @param markupLanguages
	 */
	@Autowired
	public void setMarkupLanguages(Map<String, AbstractMarkupLanguage> markupLanguages)
	{
		for (Map.Entry<String, ? extends AbstractMarkupLanguage> markupLanguage : markupLanguages.entrySet())
		{
			String languageName = ((AbstractMarkupLanguage) markupLanguage.getValue()).getName();
			String beanName = markupLanguage.getKey();
			languageMapping.put(languageName, beanName);
			log.debug("adding parser for markup language {} (bean: {})", languageName, beanName);
		}
	}


	/**
	 * getMarkupLanguageFactory() returns the markupLanguageFactory
	 * 
	 * @return the markupLanguageFactory
	 */
	public MarkupLanguageFactory getMarkupLanguageFactory()
	{
		return this.markupLanguageFactory;
	}


	/**
	 * setMarkupLanguageFactory() sets the markupLanguageFactory
	 * 
	 * @param markupLanguageFactory the markupLanguageFactory to set
	 */
	public void setMarkupLanguageFactory(MarkupLanguageFactory markupLanguageFactory)
	{
		this.markupLanguageFactory = markupLanguageFactory;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.parser.MarkupParser#getMarkupLanguages()
	 */
	@Override
	public Map<String, String> getMarkupLanguages()
	{
		return languageMapping;
	}
}
