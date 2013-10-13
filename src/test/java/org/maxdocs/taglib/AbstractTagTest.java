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
 *
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
	 * 
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
}
