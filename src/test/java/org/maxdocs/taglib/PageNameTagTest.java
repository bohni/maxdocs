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
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * PageNameTagTest:
 * Unit test for {@link PageNameTag}.
 * 
 * @author Team maxdocs.org
 */
public class PageNameTagTest
{
	private static Logger log = LoggerFactory.getLogger(PageNameTagTest.class);

	private MaxDocs mockEngine;

	private HtmlPage htmlPage;

	private final String pagePath = "/Main";

	private PageNameTag pageNameTag;

	private MockServletContext mockServletContext;

	private MockPageContext mockPageContext;

	private WebApplicationContext mockWebApplicationContext;

	/**
	 * setUp:
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		log.trace("setUp");
		// Create the mock servlet context
		mockServletContext = new MockServletContext();

		// Create the mock Spring Context so that we can mock out the calls to getBean in the custom tag
		// Then add the Spring Context to the Servlet Context
		mockWebApplicationContext = EasyMock.createMock(WebApplicationContext.class);
		mockServletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
			mockWebApplicationContext);

		// Create the MockPageContext passing in the mock servlet context created above
		mockPageContext = new MockPageContext(mockServletContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setPagePath(pagePath);

		// Create the mocked MaxDocs engine
		mockEngine = EasyMock.createMock(MaxDocs.class);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
		mockServletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, mockEngine);
		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		pageNameTag = new PageNameTag();
		pageNameTag.setPageContext(mockPageContext);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}

	/**
	 * testDoStartTagDefault:
	 * Check default output.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagDefault");

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<h1 class=\"maxdocsPageName\">" + htmlPage.getPageName() + "</h1>";

		testTag(plain, styleClass, expectedOutput);
	}

	/**
	 * testDoStartTagWithStyle:
	 * Check output with parameters set
	 * <ul>
	 *   <li>styleClass = "style"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String expectedOutput = "<h1 class=\"" + styleClass + "\">" + htmlPage.getPageName() + "</h1>";

		testTag(plain, styleClass, expectedOutput);
	}

	/**
	 * testDoStartTagWithPlain:
	 * Check output with parameters set
	 * <ul>
	 *   <li>plain = true</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String expectedOutput = htmlPage.getPageName();

		testTag(plain, styleClass, expectedOutput);
	}

	/**
	 * testDoStartTagWithPlainAndStyle:
	 * Check output with parameters set
	 * <ul>
	 *   <li>plain = true</li>
	 *   <li>styleClass = "style"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWithPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String expectedOutput = htmlPage.getPageName();

		testTag(plain, styleClass, expectedOutput);
	}

	/**
	 * testDoStartTagPageNotExists:
	 * Check output for non existing page.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagPageNotExists() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagPageNotExists");

		Boolean plain = null;
		String styleClass = null;

		String expectedOutput = "<h1 class=\"maxdocsPageName\">" + htmlPage.getPageName() + "</h1>";

		testTag(plain, styleClass, expectedOutput);
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
		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
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

	private void replayAllMocks()
	{
		EasyMock.replay(mockWebApplicationContext, mockEngine);
	}

	private void verifyAllMocks()
	{
		EasyMock.verify(mockWebApplicationContext, mockEngine);
	}
}
