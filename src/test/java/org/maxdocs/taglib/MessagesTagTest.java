package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.junit.Before;
import org.junit.Test;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.Message;
import org.maxdocs.data.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * MessagesTagTest
 * TODO, 06.11.2012: Documentation
 * 
 * @author Team maxdocs.org
 */
public class MessagesTagTest extends AbstractTagTest
{
	private static Logger log = LoggerFactory.getLogger(MessagesTagTest.class);

	private MessagesTag messagesTag;


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
		messagesTag = new MessagesTag();
		messagesTag.setPageContext(mockPageContext);
	}


	/**
	 * testDoStartTagMessagesEmpty:
	 * Check default output for empty messages list.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagMessagesEmpty() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagMessagesDefault");

		// Test data
		List<Message> messages = new ArrayList<>();

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "";

		assertTrue("testTag must return true", testTag(new Object[]{ plain, styleClass}, expectedOutput));
	}


	/**
	 * testDoStartTagMessagesDefault:
	 * Check default output.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagMessagesDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagMessagesDefault");

		// Test data
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("Message", Severity.ERROR));
		messages.add(new Message("Message", Severity.WARNING));
		messages.add(new Message("Message", Severity.INFO));
		messages.add(new Message("Message"));

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<ul class=\"maxdocsMessages\"><li class=\"error\">Message</li>"
			+ "<li class=\"warning\">Message</li><li class=\"info\">Message</li>"
			+ "<li class=\"info\">Message</li></ul>";

		assertTrue("testTag must return true", testTag(new Object[]{ plain, styleClass}, expectedOutput));
	}


	/**
	 * testDoStartTagEmptyMessages:
	 * Check default output for empty message list.
	 * 
	 * @throws JspException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testDoStartTagEmptyMessages() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagEmptyMessages");

		// Test data
		List<Message> messages = null;

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "";

		assertTrue("testTag must return true", testTag(new Object[] { plain, styleClass }, expectedOutput));
	}


	protected boolean testTag(Object[] params, String expectedOutput)
		throws JspException, UnsupportedEncodingException
	{
		replayAllMocks();

		super.setCommonAttributes((Boolean) params[0], (String) params[1], messagesTag);

		int tagReturnValue = messagesTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();

		return true;
	}
}
