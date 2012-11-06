package org.maxdocs.taglib;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * MessagesTagTest
 * TODO, 06.11.2012: Documentation
 * 
 * @author Team maxdocs.org
 */
public class MessagesTagTest
{
	private static Logger log = LoggerFactory.getLogger(MessagesTagTest.class);

	private MessagesTag messagesTag;

	private MockServletContext mockServletContext;

	private MockPageContext mockPageContext;

	private WebApplicationContext mockWebApplicationContext;


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception
	{
		log.trace("setUp");
		// Create the mock servlet context
		mockServletContext = new MockServletContext();

		// Create the mock Spring Context so that we can mock out the calls to getBean in the custom tag
		// Then add the Spring Context to the Servlet Context
		mockWebApplicationContext = EasyMock.createMock(WebApplicationContext.class);
		mockServletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
			mockWebApplicationContext);

		// Create the MockPageContext passing in the mock servlet context created above
		mockPageContext = new MockPageContext(mockServletContext);

		// Test data
		List<String> messages = new ArrayList<String>();
		messages.add("Message");

		mockPageContext.getRequest().setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		// Create an instance of the custom tag we want to test
		// set it's PageContext to the MockPageContext we created above
		messagesTag = new MessagesTag();
		messagesTag.setPageContext(mockPageContext);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}


	@Test
	public void testDoStartTagMessagesDefault() throws JspException, UnsupportedEncodingException
	{
		log.trace("testDoStartTagMessagesDefault");

		Boolean plain = null;
		String styleClass = null;
		String expectedOutput = "<ul class=\"maxdocsMessages\"><li>Message</li></ul>";

		testTag(plain, styleClass, expectedOutput);
	}


	private void testTag(Boolean plain, String styleClass, String expectedOutput)
		throws JspException, UnsupportedEncodingException
	{
		replayAllMocks();

		if (plain != null)
		{
			messagesTag.setPlain(plain.booleanValue());
		}
		if (StringUtils.isNotBlank(styleClass))
		{
			messagesTag.setStyleClass(styleClass);
		}

		int tagReturnValue = messagesTag.doStartTag();
		assertEquals("Tag should return 'TagSupport.SKIP_BODY'", TagSupport.SKIP_BODY, tagReturnValue);

		String output = ((MockHttpServletResponse) mockPageContext.getResponse()).getContentAsString();
		assertEquals("Output should be '" + expectedOutput + "'", expectedOutput, output);

		verifyAllMocks();
	}


	private void replayAllMocks()
	{
		EasyMock.replay(mockWebApplicationContext);
	}


	private void verifyAllMocks()
	{
		EasyMock.verify(mockWebApplicationContext);
	}
}
