/**
 * Copyright (c) 2010-2011, Team maxdocs.org
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
 * ContentTypeTagTest:
 * Unit test for {@link ContentTypeTag}.
 * 
 * @author Team maxdocs.org
 */
public class ContentTypeTagTest
{
	private static Logger log = LoggerFactory.getLogger(ContentTypeTagTest.class);

	private MaxDocs mockEngine;

	private HtmlPage htmlPage;

	private final String pagePath = "/Main";

	private ContentTypeTag contentTypeTag;

	private MockServletContext mockServletContext;

	private MockPageContext mockPageContext;

	private WebApplicationContext mockWebApplicationContext;


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
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
		htmlPage.setContentType("text/jspwiki");

		// Create the mocked MaxDocs engine
		mockEngine = EasyMock.createMock(MaxDocs.class);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
		mockServletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, mockEngine);
		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		contentTypeTag = new ContentTypeTag();
		contentTypeTag.setPageContext(mockPageContext);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}


	/**
	 * testDoStartTagDefault:
	 * Check output for type set to 'author'.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagDefault() throws JspException, UnsupportedEncodingException
	{
		String expectedOutput = "<span class=\"maxdocsContentType\">" + htmlPage.getContentType() + "</span>";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		int tagReturnValue = contentTypeTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
	}


	/**
	 * testDoStartTagWithStyle:
	 * Check output for type set to 'author' and styleClass set to 'style'.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithStyle() throws JspException, UnsupportedEncodingException
	{
		String styleClass = "style";
		String expectedOutput = "<span class=\"" + styleClass + "\">" + htmlPage.getContentType() + "</span>";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		contentTypeTag.setStyleClass(styleClass);
		int tagReturnValue = contentTypeTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
	}


	/**
	 * testDoStartTagWithPlain:
	 * Check output for type set to 'author' and plain set to 'true'.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlain() throws JspException, UnsupportedEncodingException
	{
		boolean plain = true;
		String expectedOutput = htmlPage.getContentType();

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		contentTypeTag.setPlain(plain);
		int tagReturnValue = contentTypeTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
	}


	/**
	 * testDoStartTagWithPlainAndStyle:
	 * Check output for type set to 'author' and plain set to 'true'.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		boolean plain = true;
		String styleClass = "style";
		String expectedOutput = htmlPage.getContentType();

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		contentTypeTag.setPlain(plain);
		contentTypeTag.setStyleClass(styleClass);
		int tagReturnValue = contentTypeTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
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
		String expectedOutput = "";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(null);
		replayAllMocks();

		int tagReturnValue = contentTypeTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be empty", expectedOutput, output);

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
