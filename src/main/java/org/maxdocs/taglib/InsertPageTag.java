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
/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

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
 * @author Team jspserver.net
 *
 */
public class InsertPageTag extends TagSupport
{
	private static Logger log = LoggerFactory.getLogger(InsertPageTag.class);

	private String styleClass = "maxdocsInsertedPage";

	private String name = "";

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
	public void setStyleClass(final String styleClass)
	{
		this.styleClass = styleClass;
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
		if(StringUtils.startsWith("/", name))
		{
			this.name = name;
		}
		else
		{
			this.name = "/" + name;
		}
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
			HtmlPage htmlPage = engine.getHtmlPage(name);
			
			if(htmlPage != null)
			{
				pageContext.getOut().write("<div class=\"" + styleClass + "\">" + htmlPage.getContent() + "</div>");
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
		log.trace("doEndTag");
		return EVAL_PAGE;
	}


}
