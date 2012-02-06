/**
 * Copyright (c) 2011-2012, Team maxdocs.org
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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


	/**
	 * Sets up the fixture, for example, open a network connection.
	 * This method is called before a test is executed.
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
