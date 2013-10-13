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

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.MaxDocsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * BreadcrumbsTagTest:
 * Unit test for @{link {@link BreadcrumbsTag}.
 * 
 * @author Team maxdocs.org
 */
public class BreadcrumbsTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(BreadcrumbsTagTest.class);

	private static final String PAGE_PATH = "/Page1";

	private static final String PAGE_NAME = "Page1";

	private BreadcrumbsTag breadcrumbsTag;


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
		breadcrumbsTag = new BreadcrumbsTag();
		breadcrumbsTag.setPageContext(mockPageContext);

		// test data
		CircularFifoBuffer breadcrumbs = new CircularFifoBuffer(2);
		breadcrumbs.add(PAGE_PATH);
		mockPageContext.getSession().setAttribute(MaxDocsConstants.MAXDOCS_BREADCRUMBS, breadcrumbs);
	}


	/**
	 * testDoStartTagDefault:
	 * Check default output
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagDefault");

		String styleClass = null;
		String expectedOutput = "<ul class=\"maxdocsBreadcrumbs\"><li><a href=\"/" + PAGE_NAME
			+ "\" title=\"/" + PAGE_NAME + "\">" + PAGE_NAME + "</a></li></ul>";

		assertTrue("testTag must return true", testTag(styleClass, expectedOutput));
	}


	/**
	 * testDoStartTagWithStyle:
	 * Check output with parameters set
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
		log.trace("testDoStartTagWithStyle");

		String styleClass = "style";
		String expectedOutput = "<ul class=\"" + styleClass + "\"><li><a href=\"/" + PAGE_NAME
			+ "\" title=\"/" + PAGE_NAME + "\">" + PAGE_NAME + "</a></li></ul>";

		assertTrue("testTag must return true", testTag(styleClass, expectedOutput));
	}


	private boolean testTag(String styleClass, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		replayAllMocks();

		super.setCommonAttributes(null, styleClass, breadcrumbsTag);

		int tagReturnValue = breadcrumbsTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);
		
		verifyAllMocks();

		return true;
	}
}
