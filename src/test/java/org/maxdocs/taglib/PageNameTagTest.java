/**
 * Copyright (c) 2011-2013, Team maxdocs.org
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
package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * PageNameTagTest:
 * Unit test for {@link PageNameTag}.
 * 
 * @author Team maxdocs.org
 */
public class PageNameTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(PageNameTagTest.class);

	private HtmlPage htmlPage;

	private final static String PAGE_PATH = "/foo/bar/Main";

	private final static String PAGE_NAME = "Main";

	private PageNameTag pageNameTag;


	/**
	 * setUp:
	 * Prepare test data
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp()
	{
		super.setUp();

		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		pageNameTag = new PageNameTag();
		pageNameTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setPagePath(PAGE_PATH);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, PAGE_PATH);
	}


	/**
	 * testDefault:
	 * Check output with no parameters set
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefault");

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<h1 class=\"maxdocsPageName\">" + PAGE_NAME + "</h1>";

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testWithStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String expectedOutput = "<h1 class=\"" + styleClass + "\">" + htmlPage.getPageName() + "</h1>";

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testWithPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String expectedOutput = htmlPage.getPageName();

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testWithPlainAndStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>styleClass = "style"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWithPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String expectedOutput = htmlPage.getPageName();

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testPageNotExists:
	 * Check output for non existing page.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testPageNotExists() throws JspException, UnsupportedEncodingException
	{
		log.trace("testPageNotExists");

		String expectedOutput = "<h1 class=\"maxdocsPageName\">" + PAGE_NAME + "</h1>";

		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(null);

		replayAllMocks();

		int tagReturnValue = pageNameTag.doStartTag();
		assertEquals("Tag should return 'SKIP_BODY'!", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output not equal!", expectedOutput, output);

		verifyAllMocks();
	}


	/**
	 * testTag():
	 * Tests the output with the given parameters.
	 * 
	 * @param plain
	 * @param styleClass
	 * @param expectedOutput
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	private void testTag(Boolean plain, String styleClass, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(htmlPage);
		replayAllMocks();

		if (plain != null)
		{
			pageNameTag.setPlain(plain.booleanValue());
		}
		if (StringUtils.isNotBlank(styleClass))
		{
			pageNameTag.setStyleClass(styleClass);
		}

		int tagReturnValue = pageNameTag.doStartTag();
		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
	}
}
