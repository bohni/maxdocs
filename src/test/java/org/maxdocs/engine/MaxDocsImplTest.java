package org.maxdocs.engine;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.parser.MarkupParser;
import org.maxdocs.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MaxDocsImplTest
 * Unit test for {@link MaxDocsImpl}.
 * 
 * @author Team maxdocs.org
 *
 */
public class MaxDocsImplTest
{
	private static Logger log = LoggerFactory.getLogger(MaxDocsImplTest.class);

	private Storage storage;
	private MarkupParser parser;
	private MaxDocsImpl maxdocs;

	private final String pagePath = "/Main";
	private HtmlPage htmlPage;
	private MarkupPage markupPage;


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp()
	{
		log.trace("setUp");
		storage = EasyMock.createMock(Storage.class);
		parser = EasyMock.createMock(MarkupParser.class);
		maxdocs = new MaxDocsImpl();
		maxdocs.setStorage(storage);
		maxdocs.setParser(parser);

		htmlPage = new HtmlPage();
		htmlPage.setPagePath(pagePath);
		htmlPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
		htmlPage.setContent("<p>Content</p>");

		markupPage = new MarkupPage();
	}


	/**
	 * testExists:
	 * Check exists().
	 */
	@Test
	public void testExists()
	{
		EasyMock.expect(storage.exists(pagePath)).andReturn(false);
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(storage, parser);

		assertEquals(false, maxdocs.exists(pagePath));
		EasyMock.verify(storage, parser);
	}


	/**
	 * testGetHtmlPage:
	 * Checks getHtmlPage().
	 */
	public void testGetHtmlPage()
	{
		EasyMock.expect(storage.exists(pagePath)).andReturn(true);
		EasyMock.expectLastCall().times(2); // one call in this test, one in getHtmlPage
		EasyMock.expect(storage.load(pagePath)).andReturn(markupPage);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(parser.parseToHtml(markupPage)).andReturn(htmlPage);
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(storage, parser);

		assertEquals(true, maxdocs.exists(pagePath));
		assertEquals(htmlPage, maxdocs.getHtmlPage(pagePath));

		EasyMock.verify(storage, parser);
	}
}
