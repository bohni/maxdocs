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
package org.maxdocs.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * MaxDocsServlet:
 * Main servlet of MaxDocs.
 *
 * @author Team jspserver.net
 */
public class MaxDocsServlet extends FrameworkServlet
{
	private static final String DEFAULT_PAGE_NAME = "Main";
	private static final String PARAMETER_NAME_ACTION = "action";
	private static final String DEFAULT_ACTION = "show";

	private static Logger log = LoggerFactory.getLogger(MaxDocsServlet.class);


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.FrameworkServlet#doService(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		log.trace("doService({})", request.getRequestURL());

		String contextPath = request.getContextPath();
		String requestUri = request.getRequestURI();
		String queryString = request.getQueryString();
		String pathInfo = request.getPathInfo();
		String servletPath = request.getServletPath();
		log.debug("ContextPath={}", contextPath);
		log.debug("RequestURI={}", requestUri);
		log.debug("QueryString={}", queryString);
		log.debug("ServletPath={}", servletPath);
		log.debug("PathInfo={}", pathInfo);

		String pagePath = pathInfo;
		if(StringUtils.equals(pagePath, "/"))
		{
			pagePath+=DEFAULT_PAGE_NAME;
		}

		String action = request.getParameter(PARAMETER_NAME_ACTION);
		if(StringUtils.isBlank(action))
		{
			action=DEFAULT_ACTION;
		}

		log.info("hier Seiteninhalt \"{}\" Action \"{}\" ermitteln und weiterleiten an home.jsp", pagePath, action);

		request.setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);

		String templateName = "default";

		if(StringUtils.equalsIgnoreCase(action, "edit"))
		{
			request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/edit.jsp").forward(request, response);

		}
		request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/show.jsp").forward(request, response);
	}
}
