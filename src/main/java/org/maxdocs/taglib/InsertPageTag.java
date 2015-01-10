/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InsertPageTag:
 * Tag, that displays the content of the given page within a div container.
 * 
 * @author Team maxdocs.org
 */
public class InsertPageTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(InsertPageTag.class);

	private String name = "";


	/**
	 * Default constructor.
	 * Creates an {@link InsertPageTag} object.
	 */
	public InsertPageTag()
	{
		super("maxdocsInsertedPage");
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
			HtmlPage htmlPage = engine.getHtmlPage(name);

			if (htmlPage != null)
			{
				pageContext.getOut().write(
					"<div class=\"" + getStyleClass() + "\">" + htmlPage.getContent() + "</div>");
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}


	/**
	 * getName() returns the name
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * setName() sets the name
	 * If name does not start with '/', the '/' will be added.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		if (StringUtils.startsWith(name, "/"))
		{
			this.name = name;
		}
		else
		{
			this.name = "/" + name;
		}
	}
}
