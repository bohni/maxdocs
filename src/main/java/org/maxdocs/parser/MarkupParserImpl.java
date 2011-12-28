package org.maxdocs.parser;

import java.io.StringWriter;

import net.java.textilej.parser.builder.HtmlDocumentBuilder;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.parser.markup.mediawiki.MediaWikiDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupParserImpl:
 *
 * @author Matthias Mezger
 */
public class MarkupParserImpl implements MarkupParser
{
	private static Logger log = LoggerFactory.getLogger(MarkupParserImpl.class);

	/* (non-Javadoc)
	 * @see org.maxdocs.parser.MarkupParser#parseToHtml(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public HtmlPage parseToHtml(MarkupPage markupPage)
	{
		HtmlPage htmlPage = new HtmlPage(markupPage);
		
		if(MaxDocsConstants.MARKUP_CONTENT_TYPE_MEDIAWIKI.equals(markupPage.getContentType()))
		{
			net.java.textilej.parser.MarkupParser parser = new net.java.textilej.parser.MarkupParser();
			MediaWikiDialect dialect = new MediaWikiDialect();
			parser.setDialect(dialect);
			StringWriter out = new StringWriter();
			parser.setBuilder(new HtmlDocumentBuilder(out));
			parser.parse(markupPage.getContent(), false);
			htmlPage.setContent(out.toString());
		}
		else
		{
			htmlPage.setContent("<b>error: content-type "+markupPage.getContentType()+" not supported</b>");
		}

		return htmlPage;
	}
}
