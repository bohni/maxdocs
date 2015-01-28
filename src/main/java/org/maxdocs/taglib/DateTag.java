/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateTag:
 * Tag, that displays the date within a span container.
 * 
 * @author Team maxdocs.org
 */
public class DateTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(DateTag.class);

	private String format = "yyyy-MM-dd HH:mm";

	private String type = "lastChange";


	/**
	 * Constructor.
	 */
	public DateTag()
	{
		super("maxdocsDate");
	}


	/* (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException
	{
		log.trace("doStartTag()");
		try
		{
			MaxDocs engine = (MaxDocs) pageContext.getServletContext().getAttribute(
				MaxDocsConstants.MAXDOCS_ENGINE);
			String pagePath = (String) pageContext.getRequest().getAttribute(
				MaxDocsConstants.MAXDOCS_PAGE_PATH);
			HtmlPage htmlPage = engine.getHtmlPage(pagePath);

			if (htmlPage != null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(getFormat());

				String date;
				if (StringUtils.equals(getType(), "creation"))
				{
					date = sdf.format(htmlPage.getFirstVersionCreationDate());
				}
				else if(StringUtils.equals(getType(), "lastchange"))
				{
					date = sdf.format(htmlPage.getCurrentVersionCreationDate());
				}
				else
				{
					date = "unsupported type '" + getType() + "'";
				}


				if (isPlain())
				{
					pageContext.getOut().write(date);
				}
				else
				{
					pageContext.getOut().write("<span class=\"" + getStyleClass() + "\">" + date + "</span>");
				}
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}


	/**
	 * getFormat() returns the format
	 * 
	 * @return the format
	 */
	public String getFormat()
	{
		return format;
	}


	/**
	 * setFormat() sets the format
	 * 
	 * @param format the format to set
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}


	/**
	 * getType() returns the type
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}


	/**
	 * setType() sets the type
	 * 
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
