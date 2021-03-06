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
 * PageVersionTagTest:
 * Unit test for {@link PageVersionTag}.
 * 
 * @author Team maxdocs.org
 */
public class PageVersionTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(PageVersionTagTest.class);

	private final static String PAGE_PATH = "/Main";

	private static final int VERSION = 2;

	private HtmlPage htmlPage;

	private PageVersionTag pageVersionTag;


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
		pageVersionTag = new PageVersionTag();
		pageVersionTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();
		htmlPage.setVersion(VERSION);

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, PAGE_PATH);
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
		String expectedOutput = "<span class=\"maxdocsPageVersion\">" + VERSION + "</span>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, null }, expectedOutput));
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
	public void testDoStartTagWithStyle() throws JspException, UnsupportedEncodingException
	{
		Boolean plain = null;
		String styleClass = "style";
		String expectedOutput = "<span class=\"" + styleClass + "\">" + VERSION + "</span>";

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, null }, expectedOutput));
	}


	/**
	 * testDoStartTagWithPlain:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlain() throws JspException, UnsupportedEncodingException
	{
		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String expectedOutput = Integer.toString(VERSION);

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, null }, expectedOutput));
	}


	/**
	 * testDoStartTagWithPlainAndStyle:
	 * Check output with parameters set
	 * <ul>
	 * <li>plain = true</li>
	 * <li>styleClass = "style"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagWithPlainAndStyle() throws JspException, UnsupportedEncodingException
	{
		Boolean plain = Boolean.TRUE;
		String styleClass = "style";
		String expectedOutput = Integer.toString(VERSION);

		assertTrue("testTag must return true",
			testTag(new Object[] { plain, styleClass, null }, expectedOutput));
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

		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(null);
		replayAllMocks();

		int tagReturnValue = pageVersionTag.doStartTag();
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
	 * @param type not used
	 * @param expectedOutput
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(htmlPage);
		replayAllMocks();

		super.setCommonAttributes((Boolean) params[0], (String) params[1], pageVersionTag);

		int tagReturnValue = pageVersionTag.doStartTag();
		assertEquals("Tag should return 'SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
