package org.maxdocs.parser;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * MarkupParserTest
 * 
 * @author Team maxdocs.org
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-context-parser.xml" })
public class MarkupParserTest
{
	private static Logger log = LoggerFactory.getLogger(MarkupParserTest.class);
	
	@Autowired
	private MarkupParser markupParser;

	/**
	 * setUp()
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		// nothing to do yet
	}


	/**
	 * Test method for {@link org.maxdocs.parser.MarkupParser#parseToHtml(org.maxdocs.data.MarkupPage)}.
	 */
	@Test
	public void testParseToHtml()
	{
		MarkupPage markupPage = new MarkupPage();
		markupPage.setContent("=== Heading ===\nHier normaler Text mit ''italic'' und '''bold'''.");
		markupPage.setPagePath("/Foo");
		markupPage.setMarkupLanguage("MediaWiki");
		markupPage.setVersion(1);
		markupPage.setAuthor("Karl Klämmerle");
		markupPage.setFirstVersionCreationDate(new Date());
		markupPage.setEditor("Rudi Rüssel");
		markupPage.setCurrentVersionCreationDate(new Date());

		HtmlPage htmlPage = markupParser.parseToHtml(markupPage);

		assertEquals("", "<h3 id=\"Heading\">Heading</h3><p>Hier normaler Text mit <i>italic</i> und <b>bold</b>.</p>", htmlPage.getContent());
	}
}
