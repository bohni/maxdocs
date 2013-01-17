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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * AuthorTagTest:
 * Unit test for @{link AuthorTag}.
 * 
 * @author Team maxdocs.org
 */
public class AuthorTagTest extends AbstractTagTest
{
	private static final String EDITOR_NAME = "Editor Name";

	private static final String AUTHOR_NAME = "Author Name";

	static Logger log = LoggerFactory.getLogger(AuthorTagTest.class);

	private final static String PAGE_PATH = "/Main";

	HtmlPage htmlPage;

	AuthorTag authorTag;


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
		authorTag = new AuthorTag();
		authorTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setAuthor(AUTHOR_NAME);
		htmlPage.setEditor(EDITOR_NAME);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, PAGE_PATH);
	}


	/**
	 * testAuthorDefault:
	 * Check output with following parameters set
	 * <ul>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testAuthorDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testAuthorDefault");

		Boolean plain = null;
		String styleClass = null;
		String type = "author";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">" + AUTHOR_NAME
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testAuthorWithStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testAuthorWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testAuthorWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String type = "author";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">" + AUTHOR_NAME
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testAuthorWithPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "author"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testAuthorWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testAuthorWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "author";
		String expectedOutput = AUTHOR_NAME;

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testAuthorWithPlainAndStyle:
	 * Check output with following parameters set
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
	public void testAuthorWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testAuthorWithPlainAndStyle");
		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "author";
		String expectedOutput = AUTHOR_NAME;

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));

	}


	/**
	 * testEditorDefault:
	 * Check output with following parameters set
	 * <ul>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testEditorDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testEditorDefault");

		Boolean plain = null;
		String styleClass = null;
		String type = "editor";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">" + EDITOR_NAME
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testEditorWithStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testEditorWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testEditorWithStyle");

		Boolean plain = null;
		String styleClass = "style";
		String type = "editor";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">" + EDITOR_NAME
			+ "</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testEditorWithPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "editor"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testEditorWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testEditorWithPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "editor";
		String expectedOutput = EDITOR_NAME;

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testEditorWithPlainAndStyle:
	 * Check output with following parameters set
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
	public void testEditorWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testEditorWithPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "editor";
		String expectedOutput = EDITOR_NAME;

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testWrongTypeDefault:
	 * Check output with following parameters set
	 * <ul>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWrongTypeDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWrongType");

		Boolean plain = null;
		String styleClass = null;
		String type = "test";
		String expectedOutput = "<span class=\"maxdocsAuthor\"><a href=\"#\">unsupported type '" + type
			+ "'</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testWrongTypeWithStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWrongTypeWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWrongTypePlain");

		Boolean plain = null;
		String styleClass = "style";
		String type = "test";
		String expectedOutput = "<span class=\"" + styleClass + "\"><a href=\"#\">unsupported type '" + type
			+ "'</a></span>";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testWrongTypeWithPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>type = "test"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWrongTypeWithPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWrongTypePlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "test";
		String expectedOutput = "unsupported type '" + type + "'";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
	}


	/**
	 * testWrongTypeWithPlainAndStyle:
	 * Check output with following parameters set
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
	public void testWrongTypeWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWrongTypePlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String type = "test";
		String expectedOutput = "unsupported type '" + type + "'";

		assertTrue("testTag must return true", testTag(plain, styleClass, type, expectedOutput));
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

		String expectedOutput = "";

		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(null);
		replayAllMocks();

		int tagReturnValue = authorTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be an empty string", expectedOutput, output);

		verifyAllMocks();
	}


	private boolean testTag(Boolean plain, String styleClass, String type, String expectedOutput)
		throws JspException, UnsupportedEncodingException
	{
		if (StringUtils.isBlank(PAGE_PATH))
		{
			EasyMock.expect(mockEngine.getHtmlPage("")).andReturn(null);
		}
		else
		{
			EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(htmlPage);
		}
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
		assertEquals("Output not equal!", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
