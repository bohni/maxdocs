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
 * InsertPAgeTagTest
 * Unit tests for {@link InsertPageTag}
 * 
 * @author Team maxdocs.org
 */
public class InsertPageTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(InsertPageTagTest.class);

	private static final String CONTENT = "<h1>Page</h1>";

	private static final String PAGE_PATH = "/foo/bar/Page";

	private static final String PAGE_NAME = "Page";

	private InsertPageTag insertPageTag;

	private HtmlPage htmlPage;


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
		insertPageTag = new InsertPageTag();
		insertPageTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setPagePath(PAGE_PATH);
		htmlPage.setContent(CONTENT);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, PAGE_PATH);
	}


	/**
	 * testDefault:
	 * Check output with parameters set
	 * <ul>
	 * <li>name = {@link InsertPageTagTest#PAGE_PATH}</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultWithPath() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefault");

		String styleClass = null;
		String name = PAGE_PATH;
		String expectedOutput = "<div class=\"maxdocsInsertedPage\">" + CONTENT + "</div>";

		assertTrue("testTag must return true", testTag(styleClass, name, expectedOutput));
	}


	/**
	 * testWithStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>styleClass = "style"</li>
	 * <li>name = {@link InsertPageTagTest#PAGE_PATH}</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testWithStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testWithStyle");

		String styleClass = "myStyle";
		String name = PAGE_PATH;
		String expectedOutput = "<div class=\"myStyle\">" + CONTENT + "</div>";

		assertTrue("testTag must return true", testTag(styleClass, name, expectedOutput));
	}


	/**
	 * testEmpty:
	 * Check output with no parameters set
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testEmpty() throws JspException, UnsupportedEncodingException
	{
		log.trace("testEmpty");

		String expectedOutput = "";

		assertTrue("testTag must return true", testTag(null, null, expectedOutput));
	}


	private boolean testTag(String styleClass, String name, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		if (name != null)
		{
			EasyMock.expect(mockEngine.getHtmlPage(name)).andReturn(htmlPage);
		}
		else
		{
			EasyMock.expect(mockEngine.getHtmlPage("")).andReturn(null);
		}

		replayAllMocks();

		super.setCommonAttributes(null, styleClass, insertPageTag);

		if (StringUtils.isNotBlank(name))
		{
			insertPageTag.setName(name);
		}

		int tagReturnValue = insertPageTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output is different", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
