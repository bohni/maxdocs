/**
 * 
 */
package org.maxdocs.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:
 * Tag, that displays a tag cloud within a div container.
 * 
 * @author Team maxdocs.org
 */
public class TagCloudTag extends TagSupport
{
	private static Logger log = LoggerFactory.getLogger(TagCloudTag.class);

	private String styleClass = "maxdocsTagCloud";

	private int size = 0;


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
			Map<String, Integer> tagCloudMap = engine.getTagCloud();

			if (tagCloudMap != null)
			{
				StringBuffer tagCloud = new StringBuffer();
				List<String> list = new ArrayList<String>();
				if (size == 0)
				{
					list.addAll(tagCloudMap.keySet());
				}
				else
				{
					int count = 0;
					for (Iterator<String> iterator = tagCloudMap.keySet().iterator(); iterator.hasNext()
						&& count < size;)
					{
						String string = (String) iterator.next();
						count++;
						list.add(string);
					}
				}
				Collections.sort(list, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2)
					{
						return o1.compareToIgnoreCase(o2);
					}
				});

				for (Iterator<String> iterator = list.iterator(); iterator.hasNext();)
				{
					String tagName = iterator.next();
					tagCloud.append("<li class=\"tagcloud" + tagCloudMap.get(tagName) + "\">");
					tagCloud.append("<a href=\"?action=listtag&selectedtag=" + tagName + "\">" + tagName + "</a>");
					tagCloud.append("</li> ");
				}

				pageContext.getOut().write("<ul class=\"" + styleClass + "\">" + tagCloud + "</ul>");
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}


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
	 *        the styleClass to set
	 */
	public void setStyleClass(final String styleClass)
	{
		this.styleClass = styleClass;
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
