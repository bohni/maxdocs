package org.maxdocs.parser;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupParserTest
 *
 * @author Matthias Mezger
 */
public class MarkupParserTest
{
	 private static Logger log = LoggerFactory.getLogger(MarkupParserTest.class);

	/**
	 * setUp()
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{}



	/**
	 * Test method for {@link org.maxdocs.parser.MarkupParser#parseToHtml(org.maxdocs.data.MarkupPage)}.
	 */
	@Test
	public void testParseToHtml()
	{
		MarkupPage markupPage = new MarkupPage();
		markupPage.setContent("=== Heading ===\nHier normaler Text mit ''italic'' und '''bold'''.");
		markupPage.setPagePath("/Foo");
		markupPage.setPageName("Foo");
		markupPage.setContentType(MaxDocsConstants.MARKUP_CONTENT_TYPE_MEDIAWIKI);
		markupPage.setVersion(1);
		markupPage.setAuthor("Karl Klämmerle");
		markupPage.setFirstVersionCreationDate(new Date(42, 1, 4, 8, 15));
		markupPage.setEditor("Rudi Rüssel");
		markupPage.setCurrentVersionCreationDate(new Date(66, 5, 6, 6, 6));
		
		MarkupParser markupParser = new MarkupParserImpl();
		HtmlPage htmlPage = markupParser.parseToHtml(markupPage);
		
		log.debug(htmlPage.getContent());
		
		// fail("Not yet implemented");
	}
}
