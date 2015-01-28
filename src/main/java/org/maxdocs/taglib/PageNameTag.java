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
 * PageNameTag:
 * Tag, that displays the page name within a h1 container.
 * 
 * @author Team maxdocs.org
 */
public class PageNameTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageNameTag.class);


	/**
	 * Constructor.
	 */
	public PageNameTag()
	{
		super("maxdocsPageName");
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

			String pageName;
			if (htmlPage != null)
			{
				pageName = htmlPage.getPageName();
			}
			else
			{
				pageName = StringUtils.substringAfterLast(pagePath, "/");
			}
			if (isPlain())
			{
				pageContext.getOut().write(pageName);
			}
			else
			{
				pageContext.getOut().write("<h1 class=\"" + getStyleClass() + "\">" + pageName + "</h1>");
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
}
