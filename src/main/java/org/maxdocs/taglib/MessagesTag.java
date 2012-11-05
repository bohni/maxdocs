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
