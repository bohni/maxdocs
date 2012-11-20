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
package org.maxdocs.parser;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupParserTest
 *
 * @author Team maxdocs.org
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
		markupPage.setFirstVersionCreationDate(new Date(42, 1, 4, 8, 15));
		markupPage.setEditor("Rudi Rüssel");
		markupPage.setCurrentVersionCreationDate(new Date(66, 5, 6, 6, 6));
		
		MarkupParser markupParser = new MarkupParserImpl();
		HtmlPage htmlPage = markupParser.parseToHtml(markupPage);
		
		log.debug(htmlPage.getContent());
		
		// fail("Not yet implemented");
	}
}
