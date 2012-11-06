/**
 * Copyright (c) 2011-2012, Team maxdocs.org
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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.maxdocs.MaxDocsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessagesTag
 * TODO, 05.11.2012: Documentation
 * 
 * @author Team maxdocs.org
 */
public class MessagesTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(MessagesTag.class);


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException
	{
		log.trace("doStartTag()");
		try
		{
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

			StringBuilder messagesText = new StringBuilder();

			Object o = request.getAttribute(MaxDocsConstants.MAXDOCS_MESSAGES);
			if (o instanceof List<?>)
			{
				List<String> messages = (List<String>) o;
				if (messages.size() > 0)
				{
					messagesText.append("<ul class=\"" + getStyleClass() + "\">");
				}
				for (String message : messages)
				{
					messagesText.append("<li>" + message + "</li>");
				}
				if(messages.size() > 0)
				{
					messagesText.append("</ul>");
				}
			}

			pageContext.getOut().write(messagesText.toString());

		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
}
