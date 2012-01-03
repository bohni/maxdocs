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
package org.maxdocs.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * MaxDocsServlet:
 * Main servlet of MaxDocs.
 *
 * @author Team maxdocs.org
 */
public class MaxDocsServlet extends HttpServlet
{
	private static final String ACTION_DELETE = "delete";
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_RENAME = "rename";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_SHOW = "show";
	private static final String ACTION_SOURCE = "source";
	private static final String DEFAULT_PAGE_NAME = "Main";
	private static final String DEFAULT_TEMPLATE_NAME = "default";
	private static final String PARAMETER_NAME_ACTION = "action";
	private static final String PARAMETER_NAME_CONTENT = "content";
	private static final String PARAMETER_NAME_VERSION = "version";
	private static final String PARAMETER_NAME_MARKUP = "markupLanguage";

	private static Logger log = LoggerFactory.getLogger(MaxDocsServlet.class);

	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException
	{
		ServletContext servletContext = getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		servletContext.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, webApplicationContext.getBean("maxDocs"));
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
			doServicet(request, response);
	}



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doServicet(request, response);
	}



	protected void doServicet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		log.trace("doService({})", request.getRequestURL());

		String contextPath = request.getContextPath();
		log.debug("ContextPath={}", contextPath);
		String requestUri = request.getRequestURI();
		log.debug("RequestURI={}", requestUri);
		String queryString = request.getQueryString();
		log.debug("QueryString={}", queryString);
		String servletPath = request.getServletPath();
		log.debug("ServletPath={}", servletPath);
		String pathInfo = request.getPathInfo();
		log.debug("PathInfo={}", pathInfo);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if(StringUtils.isBlank(username)) // Spring liefert anonymousUser
		{
			username = "Anonymous";
		}

		// PagePath
		String pagePath = pathInfo;
		if(StringUtils.equals(pagePath, "/"))
		{
			pagePath+=DEFAULT_PAGE_NAME;
		}
		log.debug("pagePath={}", pagePath);
		request.setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);
		
		// Breadcrumbs
		CircularFifoBuffer breadcrumbs = (CircularFifoBuffer) request.getSession().getAttribute(
			MaxDocsConstants.MAXDOCS_BREADCRUMBS);
		if(breadcrumbs == null)
		{
			breadcrumbs = new CircularFifoBuffer(5); // TODO: Length configurable?
		}
		String lastPagePath = "";
		@SuppressWarnings("rawtypes")
		Iterator iterator = breadcrumbs.iterator();
		while(iterator.hasNext())
		{
			lastPagePath = (String) iterator.next();
		}
		if(!StringUtils.equals(lastPagePath, pagePath))
		{
			breadcrumbs.add(pagePath);
		}
		request.getSession().setAttribute(MaxDocsConstants.MAXDOCS_BREADCRUMBS, breadcrumbs);
		request.setAttribute(MaxDocsConstants.MAXDOCS_BREADCRUMBS, breadcrumbs);

		// TODO: determine template 
		String templateName = DEFAULT_TEMPLATE_NAME;
		log.debug("templateName={}", templateName);

		// Actions - TODO: Per doDelete() doPost() do Get() doPut()?
		String action = request.getParameter(PARAMETER_NAME_ACTION);
		if(StringUtils.isBlank(action))
		{
			action=ACTION_SHOW;
		}
		log.debug("action={}", action);
		if(StringUtils.equalsIgnoreCase(action, ACTION_DELETE))
		{
			// TODO
		}
		else if(StringUtils.equalsIgnoreCase(action, ACTION_EDIT))
		{
			MaxDocs maxDocs = (MaxDocs)getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			MarkupPage markupPage = maxDocs.getMarkupPage(pagePath);
			request.setAttribute(MaxDocsConstants.MAXDOCS_MARKUP_PAGE, markupPage);
			request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/edit.jsp").forward(request, response);
		}
		else if(StringUtils.equalsIgnoreCase(action, ACTION_RENAME))
		{
			// TODO
		}
		else if(StringUtils.equalsIgnoreCase(action, ACTION_SAVE))
		{
			MaxDocs maxDocs = (MaxDocs)getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);

			MarkupPage oldPage = maxDocs.getMarkupPage(pagePath);
			MarkupPage newPage = new MarkupPage(oldPage);
			if(oldPage == null)
			{
				newPage.setAuthor(username);
				newPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
				newPage.setPagePath(pagePath);
			}
			if(StringUtils.isNotBlank(request.getParameter(PARAMETER_NAME_VERSION)))
			{
				newPage.setVersion(Integer.parseInt(request.getParameter(PARAMETER_NAME_VERSION)));
			}
			newPage.setContent(request.getParameter(PARAMETER_NAME_CONTENT));
			newPage.setMarkupLanguage(request.getParameter(PARAMETER_NAME_MARKUP));
			newPage.setEditor(username);
			boolean success = maxDocs.save(oldPage, newPage);

			if(! success)
			{
				log.debug("Concurrent changes...");
				// TODO: concurrent changes - show error message
			}
			request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/show.jsp").forward(request, response);
		}
		else if(StringUtils.equalsIgnoreCase(action, ACTION_SHOW))
		{
			request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/show.jsp").forward(request, response);
		}
		else if(StringUtils.equalsIgnoreCase(action, ACTION_SOURCE))
		{
			request.getRequestDispatcher("/WEB-INF/templates/"+ templateName + "/source.jsp").forward(request, response);
		}
		response.setCharacterEncoding("UTF-8");
	}
}
