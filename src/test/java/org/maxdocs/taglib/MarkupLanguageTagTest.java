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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * MarkupLanguageTagTest:
 * Unit test for {@link MarkupLanguageTag}.
 * 
 * @author Team maxdocs.org
 */
public class MarkupLanguageTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(MarkupLanguageTagTest.class);

	private HtmlPage htmlPage;

	private final String pagePath = "/Main";

	private final String markupMediaWiki[] = new String[] { "MediaWiki", "mediawiki" };

	private MarkupLanguageTag markupLanguageTag;


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

		// Prepare tag
		markupLanguageTag = new MarkupLanguageTag();
		markupLanguageTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setMarkupLanguage(markupMediaWiki[0]);
		Map<String, String> languages = new HashMap<String, String>();
		languages.put(markupMediaWiki[0], markupMediaWiki[1]);
		languages.put("Creole", "creole");
		languages.put("JspWiki", "jspwiki");

		// Prepare mocks
		EasyMock.expect(mockEngine.getMarkupLangages()).andReturn(languages);
		EasyMock.expect(mockEngine.getDefaultMarkupLangage()).andReturn(markupMediaWiki[0]);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
	}


	/**
	 * testDefaultOutput:
	 * Check output with no parameters set
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultOutput() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefaultOutput");

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<span class=\"maxdocsMarkupLanguage\">" + markupMediaWiki[0] + "</span>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "output" }, expectedOutput));
	}


	/**
	 * testDefaultInput:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>type = "input"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultInput() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefaultInput");

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<select class=\"maxdocsMarkupLanguage\" name=\"markupLanguage\" size=\"1\">"
			+ "<option value=\"Creole\" >Creole</option>"
			+ "<option value=\"MediaWiki\" selected=\"selected\">MediaWiki</option>"
			+ "<option value=\"JspWiki\" >JspWiki</option>" + "</select>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "input" }, expectedOutput));
	}


	/**
	 * testOutputPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>plain = "true"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testOutputPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testOutputPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String expectedOutput = markupMediaWiki[0];

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "output" }, expectedOutput));
	}


	/**
	 * testDefaultInputPlain:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>plain = "true"</code></li>
	 * <li><code>type = "input"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultInputPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefaultInputPlain");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String expectedOutput = "<select class=\"maxdocsMarkupLanguage\" name=\"markupLanguage\" size=\"1\">"
			+ "<option value=\"Creole\" >Creole</option>"
			+ "<option value=\"MediaWiki\" selected=\"selected\">MediaWiki</option>"
			+ "<option value=\"JspWiki\" >JspWiki</option>" + "</select>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "input" }, expectedOutput));
	}


	/**
	 * testOutputStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>styleClass = "style"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testOutputStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testOutputStyle");

		Boolean plain = null;
		String styleClass = "style";
		String expectedOutput = "<span class=\"" + styleClass + "\">" + markupMediaWiki[0] + "</span>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "output" }, expectedOutput));
	}


	/**
	 * testDefaultInputStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>styleClass = "style"</code></li>
	 * <li><code>type = "input"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultInputStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefaultInputStyle");

		Boolean plain = null;
		String styleClass = "style";
		String expectedOutput = "<select class=\"" + styleClass + "\" name=\"markupLanguage\" size=\"1\">"
			+ "<option value=\"Creole\" >Creole</option>"
			+ "<option value=\"MediaWiki\" selected=\"selected\">MediaWiki</option>"
			+ "<option value=\"JspWiki\" >JspWiki</option>" + "</select>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "input" }, expectedOutput));
	}


	/**
	 * testOutputPlainAndStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>plain = "true"</code></li>
	 * <li><code>styleClass = "style"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testOutputPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testOutputPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String expectedOutput = markupMediaWiki[0];

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "output" }, expectedOutput));
	}


	/**
	 * testDefaultInputPlainAndStyle:
	 * Check output with following parameters set
	 * <ul>
	 * <li><code>plain = "true"</code></li>
	 * <li><code>styleClass = "style"</code></li>
	 * <li><code>type = "input"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDefaultInputPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDefaultInputPlainAndStyle");

		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String expectedOutput = "<select class=\"" + styleClass + "\" name=\"markupLanguage\" size=\"1\">"
			+ "<option value=\"Creole\" >Creole</option>"
			+ "<option value=\"MediaWiki\" selected=\"selected\">MediaWiki</option>"
			+ "<option value=\"JspWiki\" >JspWiki</option>" + "</select>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, "input" }, expectedOutput));
	}


	/**
	 * testOutputPageNotExists:
	 * Check output for non existing page with no parameters set
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testOutputPageNotExists() throws JspException, UnsupportedEncodingException
	{
		log.trace("testOutputPageNotExists");

		String expectedOutput = "<span class=\"maxdocsMarkupLanguage\">" + markupMediaWiki[0] + "</span>";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(null);
		replayAllMocks();

		int tagReturnValue = markupLanguageTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be empty", expectedOutput, output);

		verifyAllMocks();
	}


	/**
	 * testInputPageNotExists:
	 * Check output for non existing page with following parameters set
	 * <ul>
	 * <li><code>type = "input"</code></li>
	 * <li><code>size = "2"</code></li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testInputPageNotExists() throws JspException, UnsupportedEncodingException
	{
		log.trace("testInputPageNotExists");

		String expectedOutput = "<select class=\"maxdocsMarkupLanguage\" name=\"markupLanguage\" size=\"2\">"
			+ "<option value=\"Creole\" >Creole</option>"
			+ "<option value=\"MediaWiki\" selected=\"selected\">MediaWiki</option>"
			+ "<option value=\"JspWiki\" >JspWiki</option>"
			+ "</select>";

		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(null);
		replayAllMocks();

		markupLanguageTag.setType("input");
		markupLanguageTag.setSize(2);
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
	protected boolean testTag(Object[] params, String expectedOutput)
		throws JspException, UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(pagePath)).andReturn(htmlPage);
		replayAllMocks();

		super.setCommonAttributes((Boolean) params[0], (String) params[1], markupLanguageTag);
		if (StringUtils.isNotBlank((String) params[2]))
		{
			markupLanguageTag.setType((String) params[2]);
		}

		int tagReturnValue = markupLanguageTag.doStartTag();
		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
