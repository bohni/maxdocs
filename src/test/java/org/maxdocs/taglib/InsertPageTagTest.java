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

		assertTrue("testTag must return true", testTag(new Object[] { styleClass, name }, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[] { styleClass, name }, expectedOutput));
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

		assertTrue("testTag must return true", testTag(new Object[2], expectedOutput));
	}


	protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		if (params[1] != null)
		{
			EasyMock.expect(mockEngine.getHtmlPage((String) params[1])).andReturn(htmlPage);
		}
		else
		{
			EasyMock.expect(mockEngine.getHtmlPage("")).andReturn(null);
		}

		replayAllMocks();

		super.setCommonAttributes(null, (String) params[0], insertPageTag);

		if (StringUtils.isNotBlank((String) params[1]))
		{
			insertPageTag.setName((String) params[1]);
		}

		int tagReturnValue = insertPageTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output is different", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
