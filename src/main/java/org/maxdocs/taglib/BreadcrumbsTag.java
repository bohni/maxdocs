/**
 * Copyright (c) 2011-2012, Team maxdocs.org
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
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BreadcrumbsTag:
 * Tag, that displays breadcrumbs of the session.
 *
 * @author Team maxdocs.org
 *
 */
public class BreadcrumbsTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(BreadcrumbsTag.class);

	/**
	 * Default constructor.
	 * Creates a {@link BreadcrumbsTag} object.
	 */
	public BreadcrumbsTag()
	{
		super("maxdocsBreadcrumbs");
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
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

			// TODO: why can't this be read from the session? Why returns pageContext.getSession() always null?
			CircularFifoBuffer breadcrumbsMap = (CircularFifoBuffer) pageContext.getRequest().getAttribute(
				MaxDocsConstants.MAXDOCS_BREADCRUMBS);
			if (breadcrumbsMap != null)
			{
				StringBuffer breadcrumbs = new StringBuffer();

				@SuppressWarnings("rawtypes")
				Iterator iter = breadcrumbsMap.iterator();

				while (iter.hasNext())
				{
					if (breadcrumbs.length() > 0)
					{
						breadcrumbs.append(" &gt; ");
					}
					String breadcrumb = (String) iter.next();
					breadcrumbs.append("<a href=\"" + request.getContextPath() + breadcrumb + "\" title=\""
						+ breadcrumb + "\">" + StringUtils.substringAfterLast(breadcrumb, "/") + "</a>");
				}

				if (isPlain())
				{
					pageContext.getOut().write(breadcrumbs.toString());
				}
				else
				{
					pageContext.getOut().write(
						"<span class=\"" + getStyleClass() + "\">" + breadcrumbs.toString() + "</span>");
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
