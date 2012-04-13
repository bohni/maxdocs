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
package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
 * MarkupLanguageTagTest:
 * Unit test for {@link MarkupLanguageTag}.
 * 
 * @author Team maxdocs.org
 */
public class MarkupLanguageTagTest
{
	private static Logger log = LoggerFactory.getLogger(MarkupLanguageTagTest.class);

	private MaxDocs mockEngine;

	private HtmlPage htmlPage;

	private final String pagePath = "/Main";

	private MarkupLanguageTag markupLanguageTag;

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
		htmlPage.setMarkupLanguage("MediaWiki");
		Map<String, String> languages = new HashMap<String, String>();
		languages.put("MediaWiki", "mediawiki");

		// Create the mocked MaxDocs engine
		mockEngine = EasyMock.createMock(MaxDocs.class);
		EasyMock.expect(mockEngine.getMarkupLangages()).andReturn(languages);
		EasyMock.expect(mockEngine.getDefaultMarkupLangage()).andReturn("MediaWiki");

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
		mockServletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, mockEngine);
		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		markupLanguageTag = new MarkupLanguageTag();
		markupLanguageTag.setPageContext(mockPageContext);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}


	/**
	 * testDoStartTagDefault:
	 * Check output.
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
		String expectedOutput = "<span class=\"maxdocsMarkupLanguage\">" + htmlPage.getMarkupLanguage()
			+ "</span>";

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testDoStartTagWithStyle:
	 * Check output for with parameters set
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
		String expectedOutput = "<span class=\"" + styleClass + "\">" + htmlPage.getMarkupLanguage()
			+ "</span>";

		testTag(plain, styleClass, expectedOutput);
	}


	/**
	 * testDoStartTagWithPlain:
	 * Check output for with parameters set
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

		Boolean plain = true;
		String styleClass = null;
		String expectedOutput = htmlPage.getMarkupLanguage();

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

		Boolean plain = true;
		String styleClass = "style";
		String expectedOutput = htmlPage.getMarkupLanguage();

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

		String expectedOutput = "<span class=\"maxdocsMarkupLanguage\">MediaWiki</span>";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(null);
		replayAllMocks();

		int tagReturnValue = markupLanguageTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be empty", expectedOutput, output);

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
		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		if (plain != null)
		{
			markupLanguageTag.setPlain(plain.booleanValue());
		}
		if (StringUtils.isNotBlank(styleClass))
		{
			markupLanguageTag.setStyleClass(styleClass);
		}

		int tagReturnValue = markupLanguageTag.doStartTag();
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
