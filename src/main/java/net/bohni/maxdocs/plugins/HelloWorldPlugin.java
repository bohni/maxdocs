package net.bohni.maxdocs.plugins;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.plugin.MaxDocsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HelloWorldPlugin:
 * TODO, 02.11.2013: Documentation
 * 
 * @author Team maxdocs.org
 *
 */
public class HelloWorldPlugin implements MaxDocsPlugin
{
	private static Logger log = LoggerFactory.getLogger(HelloWorldPlugin.class);

	/* (non-Javadoc)
	 * @see org.maxdocs.plugin.MaxDocsPlugin#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void forward(ServletRequest request, ServletResponse response) throws IOException,
		IllegalStateException
	{
		log.trace("forward()");
		// TODO Auto-generated method stub
	}

	@Override
	public String getHtml(String params, String cssStyle, String cssClass)
	{
		log.trace("getHtml()");
		StringBuilder sb = new StringBuilder();
		sb.append("<div");
		if(StringUtils.isNotBlank(cssStyle))
		{
			sb.append(" style=\"");
			sb.append(cssStyle);
			sb.append("");
		}
		if(StringUtils.isNotBlank(cssClass))
		{
			sb.append(" class=\"");
			sb.append(cssClass);
			sb.append("");
		}
		sb.append(">");
		if(StringUtils.isBlank(params))
		{
			sb.append("Fehler: Parameter name muss gesetzt werden!");
		}
		else
		{
			String[] paramArray = params.split(" ");
			for (String param : paramArray)
			{
				if(StringUtils.startsWith(param, "name"));
				{
					sb.append("<p>Hello ");
					sb.append(StringUtils.right(param, StringUtils.indexOf(param, "=")));
					sb.append("!</p>");
				}
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
}
