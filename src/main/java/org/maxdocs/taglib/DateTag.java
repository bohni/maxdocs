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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateTag:
 * Tag, that displays the date within a span container.
 *
 * @author Team jspserver.net
 *
 */
public class DateTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(DateTag.class);

	private String format;

	private String type = "lastChange";

	/**
	 * Constructor.
	 *
	 */
	public DateTag()
	{
		super();
		setStyleClass("maxdocsDate");
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
			Engine engine = (Engine) pageContext.getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			String pageName = (String) pageContext.getRequest().getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			HtmlPage htmlPage = engine.getHtmlPage(pageName);
			if(StringUtils.isBlank(format))
			{
				format = ((SimpleDateFormat)DateFormat.getDateTimeInstance(
						DateFormat.SHORT, DateFormat.SHORT, pageContext.getRequest().getLocale())).toPattern();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			String date;
			if(StringUtils.equals(type, "creation"))
			{
				date = sdf.format(htmlPage.getFirstVersionCreationDate());
			}
			else
			{
				date = sdf.format(htmlPage.getCurrentVersionCreationDate());
			}

			if (isPlain())
			{
				pageContext.getOut().write(date);
			}
			else
			{
				pageContext.getOut().write(
						"<span class=\"" + getStyleClass() + "\">" + date + "</span>");
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
