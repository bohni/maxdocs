package org.maxdocs.taglib;

import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * AbstractTagTest
 * TODO, 16.01.2013: Documentation
 * 
 * @author Team maxdocs.org
 */
public abstract class AbstractTagTest
{
	static Logger log = LoggerFactory.getLogger(AbstractTagTest.class);

	protected MaxDocs mockEngine;
	private MockServletContext mockServletContext;
	protected MockPageContext mockPageContext;
	private WebApplicationContext mockWebApplicationContext;


	/**
	 * setUp:
	 * Setup the fixture:
	 * Create the needed mock objects for the tests
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp()
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

		// Create the mocked MaxDocs engine
		mockEngine = EasyMock.createMock(MaxDocs.class);
		mockServletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, mockEngine);

		// Whenever you make a call to the doStartTag() method on the custom tag it calls getServletContext()
		// on the WebApplicationContext. So to avoid having to put this expect statement in every test
		// I've included it in the setUp()
		EasyMock.expect(mockWebApplicationContext.getServletContext()).andReturn(mockServletContext)
			.anyTimes();
	}


	protected void replayAllMocks()
	{
		EasyMock.replay(mockWebApplicationContext, mockEngine);
	}


	protected void verifyAllMocks()
	{
		EasyMock.verify(mockWebApplicationContext, mockEngine);
	}


	/**
	 * setCommonAttributes:
	 * Set common attributes of tags
	 * 
	 * @param plain
	 * @param styleClass
	 * @param tag
	 */
	protected void setCommonAttributes(Boolean plain, String styleClass, AbstractMaxDocsTagSupport tag)
	{
		if (plain != null)
		{
			tag.setPlain(plain.booleanValue());
		}
		if (StringUtils.isNotBlank(styleClass))
		{
			tag.setStyleClass(styleClass);
		}
	}


	abstract protected boolean testTag(Object[] params, String expectedOutput) throws JspException,
		UnsupportedEncodingException;
}
