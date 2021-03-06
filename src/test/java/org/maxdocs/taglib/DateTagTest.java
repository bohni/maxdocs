package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
public class DateTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(DateTagTest.class);

	private static final String PAGE_PATH = "/Main";

	private static final String DATE_STRING = "2012-12-17 14:15";

	private Calendar date;

	private HtmlPage htmlPage;

	private DateTag dateTag;


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
		dateTag = new DateTag();
		dateTag.setPageContext(mockPageContext);

		// Test data
		htmlPage = new HtmlPage();

		date = GregorianCalendar.getInstance();
		date.clear();
		date.set(Calendar.YEAR, 2012);
		date.set(Calendar.MONTH, 11);
		date.set(Calendar.DATE, 17);
		date.set(Calendar.HOUR, 14);
		date.set(Calendar.MINUTE, 15);
		date.set(Calendar.SECOND, 37);

		htmlPage.setFirstVersionCreationDate(date.getTime());
		htmlPage.setCurrentVersionCreationDate(date.getTime());

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, PAGE_PATH);
	}


	/**
	 * testCreationDefault:
	 * Check output with following parameters set
	 * <ul>
	 * <li>type = "creationDate"</li>
	 * </ul>
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testCreationDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testCreationDefault");

		Boolean plain = null;
		String styleClass = null;
		String type = "creation";
		String format = null;

		String expectedOutput = "<span class=\"maxdocsDate\">" + DATE_STRING + "</span>";

		assertTrue("testTag must return true", testTag(new Object[]{plain, styleClass, type, format}, expectedOutput));
	}


	@Test
	public void testCreationPlain() throws JspException, UnsupportedEncodingException
	{
		log.trace("testCreationDefault");

		Boolean plain = Boolean.TRUE;
		String styleClass = null;
		String type = "creation";
		String format = null;

		String expectedOutput = DATE_STRING;

		assertTrue("testTag must return true", testTag(new Object[]{plain, styleClass, type, format}, expectedOutput));
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

		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(null);

		replayAllMocks();

		int tagReturnValue = dateTag.doStartTag();
		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();

		String expectedOutput = "";

		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);
		assertEquals("Output should be empty", expectedOutput, output);

		verifyAllMocks();
	}


	protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException
	{
		EasyMock.expect(mockEngine.getHtmlPage(PAGE_PATH)).andReturn(htmlPage);
		replayAllMocks();

		super.setCommonAttributes((Boolean)params[0], (String)params[1], dateTag);
		if (StringUtils.isNotBlank((String)params[2]))
		{
			dateTag.setType((String)params[2]);
		}

		if (StringUtils.isNotBlank((String)params[3]))
		{
			dateTag.setFormat((String)params[3]);
		}

		int tagReturnValue = dateTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
