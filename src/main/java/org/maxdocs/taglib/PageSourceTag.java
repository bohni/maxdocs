package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageContentTag:
 * Tag, that displays the content within a div container.
 * 
 * @author Team maxdocs.org
 */
public class PageSourceTag extends TagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageSourceTag.class);


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
			String pageName = (String) pageContext.getRequest().getAttribute(
				MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MarkupPage markupPage = engine.getMarkupPage(pageName);

			if (markupPage != null)
			{
				pageContext.getOut().write(markupPage.getContent());
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
}
