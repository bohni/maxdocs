/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthorTag:
 * Tag, that displays the author or editor within a span container.
 * 
 * @author Team maxdocs.org
 */
public class AuthorTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(AuthorTag.class);

	private String type = "author";


	/**
	 * Default constructor.
	 * Creates an {@link AuthorTag} object.
	 */
	public AuthorTag()
	{
		super("maxdocsAuthor");
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

			if (htmlPage != null)
			{

				String author;
				if (StringUtils.equals(getType(), "author"))
				{
					author = htmlPage.getAuthor();
				}
				else if (StringUtils.equals(getType(), "editor"))
				{
					author = htmlPage.getEditor();
				}
				else
				{
					author = "unsupported type '" + getType() + "'";
				}

				if (isPlain())
				{
					pageContext.getOut().write(author);
				}
				else
				{
					// TODO: if(engine.pageExists(author))
					// {
					author = "<a href=\"#\">" + author + "</a>";
					// }
					pageContext.getOut().write(
						"<span class=\"" + getStyleClass() + "\">" + author + "</span>");
				}
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
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
}
