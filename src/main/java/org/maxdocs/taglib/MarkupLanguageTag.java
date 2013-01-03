/**
 * Copyright (c) 2011-2013, Team maxdocs.org
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
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupLanguageTag:
 * Tag, that displays the contentType within a span container.
 *
 * @author Team maxdocs.org
 *
 */
public class MarkupLanguageTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(MarkupLanguageTag.class);

	private String type = "output";

	private int size = 1;


	/**
	 * Constructor.
	 *
	 */
	public MarkupLanguageTag()
	{
		super("maxdocsMarkupLanguage");
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
			String pageName = (String) pageContext.getRequest().getAttribute(
				MaxDocsConstants.MAXDOCS_PAGE_PATH);
			HtmlPage htmlPage = engine.getHtmlPage(pageName);
			Map<String, String> markupLanguages = engine.getMarkupLangages();

			String markupLanguage = engine.getDefaultMarkupLangage();
			if (htmlPage != null)
			{
				markupLanguage = htmlPage.getMarkupLanguage();
			}
			if (StringUtils.equalsIgnoreCase(getType(), "output"))
			{
				if (isPlain())
				{
					pageContext.getOut().write(markupLanguage);
				}
				else
				{
					pageContext.getOut().write(
						"<span class=\"" + getStyleClass() + "\">" + markupLanguage + "</span>");
				}
			}
			else
			{
				pageContext.getOut().write(
					"<select class=\"" + getStyleClass() + "\" name=\"markupLanguage\" size=\"" + size
						+ "\">");
				for (String key : markupLanguages.keySet())
				{
					pageContext.getOut().write("<option value=\"" + key + "\" ");
					if (StringUtils.equals(markupLanguage, key))
					{
						pageContext.getOut().write("selected=\"selected\"");
					}
					pageContext.getOut().write(">" + key + "</option>");
				}

				pageContext.getOut().write("</select>");
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}


	/**
	 * getType: Returns the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}


	/**
	 * setType: Sets the type.
	 *
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}


	/**
	 * getSize: Returns the size.
	 *
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}


	/**
	 * setSize: Sets the size.
	 *
	 * @param size the size to set
	 */
	public void setSize(int size)
	{
		this.size = size;
	}

}
