/**
 * Copyright (c) 2010-2011, Team jspserver.net
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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.maxdocs.util.UrlEncodedQueryString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageContentTag:
 * Tag, that displays the content within a div container.
 *
 * @author Team jspserver.net
 *
 */
public class PageContentTag extends TagSupport
{
	private static Logger log = LoggerFactory.getLogger(PageContentTag.class);

	private String styleClass = "maxdocsContent";

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
	 *            the styleClass to set
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
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
			MaxDocs engine = (MaxDocs) pageContext.getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			String pagePath = (String) pageContext.getRequest().getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			HtmlPage htmlPage = engine.getHtmlPage(pagePath);

			if(htmlPage != null)
			{
				pageContext.getOut().write("<div class=\"" + styleClass + "\">" + htmlPage.getContent() + "</div>");
			}
			else
			{
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				String query = request.getQueryString();
				String contextPath = request.getContextPath();
				UrlEncodedQueryString queryString = UrlEncodedQueryString.parse(query);
				queryString.set("action", "edit");
				pageContext.getOut().write("<div class=\"" + styleClass + "\"><p>Die Seite <strong>" + pagePath + "</strong> existiert nicht. <a href=\""+ contextPath + pagePath + "?" + queryString.toString() +"\">Erstelle</a> sie doch einfach.</p></div>");
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
}
