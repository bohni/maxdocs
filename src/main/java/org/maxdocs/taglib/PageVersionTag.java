/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageVersionTag:
 * Tag, that displays the version of the page within a span container.
 * 
 * @author Team maxdocs.org
 */
public class PageVersionTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageVersionTag.class);


	/**
	 * Constructor.
	 */
	public PageVersionTag()
	{
		super("maxdocsPageVersion");
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
				String pageVersion = "" + htmlPage.getVersion();

				if (isPlain())
				{
					pageContext.getOut().write(pageVersion);
				}
				else
				{
					pageContext.getOut().write(
						"<span class=\"" + getStyleClass() + "\">" + pageVersion + "</span>");
				}
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
}
