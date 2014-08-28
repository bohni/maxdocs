/**
 * Copyright (c) 2011-2014, Team maxdocs.org
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
