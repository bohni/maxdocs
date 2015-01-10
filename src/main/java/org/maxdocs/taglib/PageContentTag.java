package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageContentTag:
 * Tag, that displays the content within a div container.
 * 
 * @author Team maxdocs.org
 */
public class PageContentTag extends TagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageContentTag.class);

	private String styleClass = "maxdocsContent";


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
				pageContext.getOut().write(
					"<div class=\"" + styleClass + "\">" + htmlPage.getContent() + "</div>");
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}


	/* (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException
	{
		log.trace("doEndTag()");
		return EVAL_PAGE;
	}


	/**
	 * getStyleClass: Returns the styleClass.
	 * 
	 * @return the styleClass
	 */
	public String getStyleClass()
	{
		return styleClass;
	}


	/**
	 * setStyleClass: Sets the styleClass.
	 * 
	 * @param styleClass
	 *        the styleClass to set
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
}
