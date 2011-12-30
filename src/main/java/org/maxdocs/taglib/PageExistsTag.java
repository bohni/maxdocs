/**
 * Copyright (c) 2010-2011, Team maxdocs.org
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
 * @author Team maxdocs.org
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
