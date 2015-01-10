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
 * Tag, that displays breadcrumbs stored in request as unordered list.
 * 
 * @author Team maxdocs.org
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

			CircularFifoBuffer breadcrumbsMap = (CircularFifoBuffer) pageContext.getSession().getAttribute(
				MaxDocsConstants.MAXDOCS_BREADCRUMBS);
			if (breadcrumbsMap != null)
			{
				StringBuffer breadcrumbs = new StringBuffer();

				breadcrumbs.append("<ul class=\"" + getStyleClass() + "\">");

				@SuppressWarnings("rawtypes")
				Iterator iter = breadcrumbsMap.iterator();
				while (iter.hasNext())
				{
					String breadcrumb = (String) iter.next();
					breadcrumbs.append("<li><a href=\"" + request.getContextPath() + breadcrumb
						+ "\" title=\"" + breadcrumb + "\">"
						+ StringUtils.substringAfterLast(breadcrumb, "/") + "</a></li>");
				}

				breadcrumbs.append("</ul>");
				pageContext.getOut().write(breadcrumbs.toString());
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
}
