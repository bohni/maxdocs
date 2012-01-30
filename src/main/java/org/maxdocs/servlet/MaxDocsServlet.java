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
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
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
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
			.getWebApplicationContext(servletContext);
		servletContext
			.setAttribute(MaxDocsConstants.MAXDOCS_ENGINE, webApplicationContext.getBean("maxDocs"));
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		IOException
	{
		doService(request, response);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		IOException
	{
		doService(request, response);
	}


	protected void doService(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
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

		// PagePath
		String pagePath = pathInfo;
		if (StringUtils.equals(pagePath, "/"))
		{
			pagePath += DEFAULT_PAGE_NAME;
		}
		log.debug("pagePath={}", pagePath);
		request.setAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH, pagePath);

		// breadcrumbs
		buildBreadcrumbs(request);

		// Action
		String action = request.getParameter(PARAMETER_NAME_ACTION);
		if (StringUtils.isBlank(action))
		{
			action = ACTION_SHOW;
		}
		log.debug("action={}", action);

		if (StringUtils.equalsIgnoreCase(action, ACTION_DELETE))
		{
			actionDelete(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_EDIT))
		{
			actionEdit(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_RENAME))
		{
			actionRename(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SAVE))
		{
			actionSave(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SHOW))
		{
			actionShow(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SOURCE))
		{
			actionSource(request, response);
		}
		response.setCharacterEncoding("UTF-8");
	}


	/**
	 * buildBreadcrumbs:
	 * Build the breadcrumbs
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 */
	private void buildBreadcrumbs(HttpServletRequest request)
	{
		// get Breadcrumbs from session 
		CircularFifoBuffer breadcrumbs = (CircularFifoBuffer) request.getSession().getAttribute(
			MaxDocsConstants.MAXDOCS_BREADCRUMBS);
		if (breadcrumbs == null)
		{
			breadcrumbs = new CircularFifoBuffer(5); // TODO: Length configurable?
		}
		String lastPagePath = "";
		@SuppressWarnings("rawtypes")
		Iterator iterator = breadcrumbs.iterator();
		while (iterator.hasNext())
		{
			lastPagePath = (String) iterator.next();
		}
		if (!StringUtils.equals(lastPagePath,
			(String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH)))
		{
			breadcrumbs.add((String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH));
		}
		// store breadcrumbs in session ...
		request.getSession().setAttribute(MaxDocsConstants.MAXDOCS_BREADCRUMBS, breadcrumbs);
		// ... and request (for BreadcrumbsTag) TODO: why can't it be
		request.setAttribute(MaxDocsConstants.MAXDOCS_BREADCRUMBS, breadcrumbs);
	}


	/**
	 * actionDelete:
	 * Delete a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionDelete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
		MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		//TODO: maxDocs.delete(pagePath,  username);
		actionShow(request, response);
	}


	/**
	 * actionEdit:
	 * Edit a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionEdit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
		MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		MarkupPage markupPage = maxDocs.getMarkupPage(pagePath);
		request.setAttribute(MaxDocsConstants.MAXDOCS_MARKUP_PAGE, markupPage);
		request.getRequestDispatcher("/WEB-INF/templates/" +  getTemplate() + "/edit.jsp").forward(request, response);
	}


	/**
	 * actionRename:
	 * Rename a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionRename(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
		MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		//TODO: maxDocs.rename(pagePath, username);
		actionShow(request, response);
	}


	/**
	 * actionSave:
	 * Save a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionSave(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
		MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		MarkupPage newPage = new MarkupPage();
		newPage.setAuthor(username);
		newPage.setContent(request.getParameter(PARAMETER_NAME_CONTENT));
		newPage.setEditor(username);
		newPage.setMarkupLanguage(request.getParameter(PARAMETER_NAME_MARKUP));
		newPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
		newPage.setPagePath(pagePath);
		// TODO: newPage.setTags(tags);
		if (StringUtils.isNotBlank(request.getParameter(PARAMETER_NAME_VERSION)))
		{
			newPage.setVersion(Integer.parseInt(request.getParameter(PARAMETER_NAME_VERSION)));
		}
		boolean success = false;
		try
		{
			success = maxDocs.save(newPage);
		}
		catch (ConcurrentEditException e)
		{
			// TODO: show error message
			log.debug("Concurrent changes...");
		}
		catch (EditWithoutChangesException e)
		{
			// TODO: show error message
			log.debug("No changes...");
		}

		if (!success)
		{
			// TODO: show error message
			log.error("Other error");
		}
		actionShow(request, response);
	}


	/**
	 * actionShow:
	 * Show a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionShow(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		request.getRequestDispatcher("/WEB-INF/templates/" +  getTemplate() + "/show.jsp").forward(request, response);
	}


	/**
	 * actionSource:
	 * Show the page source
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionSource(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		request.getRequestDispatcher("/WEB-INF/templates/" +  getTemplate() + "/source.jsp").forward(request, response);
	}


	/**
	 * getTemplate:
	 * returns the template name
	 * 
	 * @return the configured template name
	 */
	private String getTemplate()
	{
		// TODO: get template name from configuration 
		String templateName = "default";
		log.debug("templateName={}", templateName);
		return templateName;
	}
}
