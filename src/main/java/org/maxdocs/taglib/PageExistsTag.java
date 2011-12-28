package org.maxdocs.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageExistsTag
 * If the page exists, the body part will be rendered.
 * 
 * @author Stefan Bohn
 *
 */
public class PageExistsTag extends BodyTagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageExistsTag.class);
	
	private String page;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException
	{
		log.trace("doStartTag()");
		MaxDocs engine = (MaxDocs) pageContext.getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		String pagePath;
		if(StringUtils.isBlank(page))
		{
			pagePath = (String) pageContext.getRequest().getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
		}
		else
		{
			pagePath = "/" + page;
		}

		if(engine.exists(pagePath))
		{
			return EVAL_BODY_INCLUDE;
		}

		return SKIP_BODY;
	}

	/**
	 * getPage: Returns the page.
	 * 
	 * @return the page
	 */
	public String getPage()
	{
		return page;
	}

	/**
	 * setPage: Sets the page.
	 * 
	 * @param page the page to set
	 */
	public void setPage(String page)
	{
		this.page = page;
	}
}
