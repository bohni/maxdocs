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

		assertTrue("testTag must return true", testTag(new Object[]{styleClass}, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[]{styleClass}, expectedOutput));
	}


	protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		replayAllMocks();

		super.setCommonAttributes(null, (String)params[0], breadcrumbsTag);

		int tagReturnValue = breadcrumbsTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);
		
		verifyAllMocks();

		return true;
	}
}
