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
import static org.junit.Assert.assertTrue;

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
 * AuthorTagTest:
 * Unit test for @{link AuthorTag}.
 * 
 * @author Team maxdocs.org
 */
public class AuthorTagTest
{
	private static Logger log = LoggerFactory.getLogger(AuthorTagTest.class);

	private MaxDocs mockEngine;

	private HtmlPage htmlPage;

	private final String pagePath = "/Main";

	private AuthorTag authorTag;

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
		htmlPage.setAuthor("Author Name");
		htmlPage.setEditor("Editor Name");

		// Create the mocked MaxDocs engine
		mockEngine = EasyMock.createMock(MaxDocs.class);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
		mockServletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, mockEngine);
		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		authorTag = new AuthorTag();
		authorTag.setPageContext(mockPageContext);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}


	/**
	 * testDoStartTagAuthorDefault:
	 * Check output with parameters set
	 * <ul>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagAuthorDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagAuthorDefault");

		Boolean plain = null;
		String styleClass = null;
		String type = "author";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">" + htmlPage.getAuthor()
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagAuthorWithStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagAuthorWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagAuthorWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String type = "author";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">" + htmlPage.getAuthor()
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagAuthorWithPlain:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagAuthorWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagAuthorWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "author";
		String expectedOutput = htmlPage.getAuthor();

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagAuthorWithPlainAndStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>styleClass = "style"</li>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagAuthorWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagAuthorWithPlainAndStyle");
		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "author";
		String expectedOutput = htmlPage.getAuthor();

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));

	}


	/**
	 * testDoStartTagEditorDefault:
	 * Check output with parameters set
	 * <ul>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagEditorDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagEditorDefault");

		Boolean plain = null;
		String styleClass = null;
		String type = "editor";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">" + htmlPage.getEditor()
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagEditorWithStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagEditorWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagEditorWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String type = "editor";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">" + htmlPage.getEditor()
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagEditorWithPlain:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagEditorWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagEditorWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "editor";
		String expectedOutput = htmlPage.getEditor();

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagEditorWithPlainAndStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>styleClass = "style"</li>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagEditorWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagEditorWithPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "editor";
		String expectedOutput = htmlPage.getEditor();

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagWrongTypeDefault:
	 * Check output with parameters set
	 * <ul>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWrongTypeDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWrongType");

		Boolean plain = null;
		String styleClass = null;
		String type = "test";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">unsupported type '" + type
			+ "'</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagWrongTypeWithStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWrongTypeWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWrongTypePlain");

		Boolean plain = null;
		String styleClass = "style";
		String type = "test";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">unsupported type '" + type
			+ "'</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagWrongTypeWithPlain:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWrongTypeWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWrongTypePlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "test";
		String expectedOutput = "unsupported type '" + type + "'";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testDoStartTagWrongTypeWithPlainAndStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>styleClass = "style"</li>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWrongTypeWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagWrongTypePlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "test";
		String expectedOutput = "unsupported type '" + type + "'";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
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

		int tagReturnValue = authorTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be empty", expectedOutput, output);

		verifyAllMocks();
	}


	private boolean testTag(Boolean plain, String styleClass, String type, String expectedOutput)
		throws JspException, UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		if (plain != null)
		{
			authorTag.setPlain(plain.booleanValue());
		}
		if (StringUtils.isNotBlank(styleClass))
		{
			authorTag.setStyleClass(styleClass);
		}
		if (StringUtils.isNotBlank(type))
		{
			authorTag.setType(type);
		}

		int tagReturnValue = authorTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
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
