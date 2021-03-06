package org.maxdocs.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.Message;
import org.maxdocs.data.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessagesTag
 * Tag, that displays messages of the request.
 * 
 * @author Team maxdocs.org
 */
public class MessagesTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(MessagesTag.class);

	private String error = "error";
	private String warning = "warning";
	private String info = "info";

	/**
	 * Default constructor.
	 * Creates a {@link MessagesTag} object.
	 */
	public MessagesTag()
	{
		super("maxdocsMessages");
	}


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
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
				@SuppressWarnings("unchecked")
				List<Message> messages = (List<Message>) o;
				if (messages.size() > 0)
				{
					messagesText.append("<ul class=\"" + getStyleClass() + "\">");
				}
				for (Message message : messages)
				{
					messagesText.append("<li" + getServerity(message.getSeverity()) + ">"
						+ message.getMessage() + "</li>");
				}
				if (messages.size() > 0)
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


	private String getServerity(Severity severity)
	{
		StringBuilder cssClass =  new StringBuilder(" class=\"");
		if(Severity.ERROR.equals(severity))
		{
			cssClass.append(getError());
		}
		if(Severity.WARNING.equals(severity))
		{
			cssClass.append(getWarning());
		}
		if(Severity.INFO.equals(severity))
		{
			cssClass.append(getInfo());
		}
		cssClass.append("\"");
		if(StringUtils.equals(" class=\"\"", cssClass.toString()))
		{
			cssClass = new StringBuilder();
		}
		return cssClass.toString();
	}


	/**
	 * getError: Returns the error.
	 * 
	 * @return the error
	 */
	public String getError()
	{
		return error;
	}


	/**
	 * setError: Sets the error.
	 * 
	 * @param error the error to set
	 */
	public void setError(String error)
	{
		this.error = error;
	}


	/**
	 * getWarning: Returns the warning.
	 * 
	 * @return the warning
	 */
	public String getWarning()
	{
		return warning;
	}


	/**
	 * setWarning: Sets the warning.
	 * 
	 * @param warning the warning to set
	 */
	public void setWarning(String warning)
	{
		this.warning = warning;
	}


	/**
	 * getInfo: Returns the info.
	 * 
	 * @return the info
	 */
	public String getInfo()
	{
		return info;
	}


	/**
	 * setInfo: Sets the info.
	 * 
	 * @param info the info to set
	 */
	public void setInfo(String info)
	{
		this.info = info;
	}
}
