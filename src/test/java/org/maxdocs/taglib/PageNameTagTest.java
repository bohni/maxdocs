package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

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

		assertTrue("testTag must return true", testTag(new Object[] { plain, styleClass }, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[] { plain, styleClass }, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[] { plain, styleClass }, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[] { plain, styleClass }, expectedOutput));
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
	protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(htmlPage);
		replayAllMocks();

		super.setCommonAttributes((Boolean) params[0], (String) params[1], pageNameTag);

		int tagReturnValue = pageNameTag.doStartTag();
		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
