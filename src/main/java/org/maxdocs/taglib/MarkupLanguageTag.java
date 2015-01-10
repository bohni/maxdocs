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
 */
public class MarkupLanguageTag extends AbstractMaxDocsTagSupport
{
	private static Logger log = LoggerFactory.getLogger(MarkupLanguageTag.class);

	private String type = "output";

	private int size = 1;


	/**
	 * Constructor.
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
					"<select class=\"" + getStyleClass() + "\" name=\"markupLanguage\" size=\"" + getSize()
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
