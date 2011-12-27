package org.maxdocs.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
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
		String pagePath = markupPage.getPagePath();
		
		HtmlPage htmlPage = new HtmlPage();
		htmlPage.setContent("");

		if (StringUtils.equals(pagePath, "/LeftMenu"))
		{
			htmlPage.setAuthor("John Doe Senior");
			htmlPage.setEditor("John Doe");
			htmlPage.setContent("<a href=\"#\">Link 1</a><br/><a href=\"#\">Link 2</a><br/><a href=\"#\">Link 3</a><br/><a href=\"#\">Link 4</a>");
			htmlPage.setContentType("text/textile");
			Calendar date = GregorianCalendar.getInstance();
			date.set(2011, 8, 16, 14, 27);
			htmlPage.setFirstVersionCreationDate(date.getTime());
			htmlPage.setCurrentVersionCreationDate(new Date());
			htmlPage.setPagePath(pagePath);
			htmlPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
			htmlPage.setVersion(2);
		}
		else
		{
			htmlPage.setAuthor("John Doe Senior");
			htmlPage.setEditor("John Doe");
			htmlPage.setContent("<p>This is the Content...</p><ul><li>List 1</li><li>List 2</li></ul><p><strong>Bold Text</strong></p>");
			htmlPage.setContentType("text/textile");
			Calendar date = GregorianCalendar.getInstance();
			date.set(2011, 8, 16, 14, 27);
			htmlPage.setFirstVersionCreationDate(date.getTime());
			htmlPage.setCurrentVersionCreationDate(new Date());
			htmlPage.setPagePath(pagePath);
			htmlPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
			htmlPage.setVersion(2);
		}

		return htmlPage;
	}
}
