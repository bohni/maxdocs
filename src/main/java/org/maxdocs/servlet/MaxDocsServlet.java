/**
 * Copyright (c) 2011-2013, Team maxdocs.org
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.engine.MaxDocs;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
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
	private static final String ACTION_DO_LOGIN = "dologin";
	private static final String ACTION_DO_LOGOUT = "dologout";
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_LOGIN = "login";
	private static final String ACTION_RENAME = "rename";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_SHOW = "show";
	private static final String ACTION_SOURCE = "source";

	private static final String DEFAULT_PAGE_NAME = "Main";

	private static final String PARAMETER_NAME_ACTION = "action";
	private static final String PARAMETER_NAME_CONTENT = "content";
	private static final String PARAMETER_NAME_VERSION = "version";
	private static final String PARAMETER_NAME_MARKUP = "markupLanguage";
	private static final String PARAMETER_NAME_TAGS = "tags";

	private static Logger log = LoggerFactory.getLogger(MaxDocsServlet.class);

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException
	{
		log.trace("init()");
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
		else if (StringUtils.equalsIgnoreCase(action, ACTION_DO_LOGIN))
		{
			actionDoLogin(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_DO_LOGOUT))
		{
			actionDoLogout(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_EDIT))
		{
			actionEdit(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_LOGIN))
		{
			actionLogin(request, response);
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
		else
		{
			actionShow(request, response);
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
		log.trace("buildBreadcrumbs(HttpServletRequest)");
		// get Breadcrumbs from session 
		CircularFifoBuffer breadcrumbs = (CircularFifoBuffer) request.getSession().getAttribute(
			MaxDocsConstants.MAXDOCS_BREADCRUMBS);
		if (breadcrumbs == null)
		{
			ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			Integer count= (Integer) context.getBean("breadcrumbsCount");
			if(count == null)
			{
				count = Integer.valueOf(5);
			}
			breadcrumbs = new CircularFifoBuffer(count.intValue());
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
		// ... and request (for BreadcrumbsTag) TODO: why can't it be read in taglib?
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
		log.trace("actionDelete(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();

		List<String> messages = getMessages(request);
		if (checkPermission(currentUser, "page:delete"))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			if (!maxDocs.delete(pagePath))
			{
				messages.add("Page '" + pagePath + "' could not be deleted!");
			}
		}
		else
		{
			log.debug("Missing delete permission");
			if (username == null)
			{
				messages.add("No delete permission for unkown users");

			}
			else
			{
				messages.add("Missing delete permission for user " + username);
			}
		}
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		actionShow(request, response);
	}


	/**
	 * actionDoLogin:
	 * Does the login with Apache Shiro
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionDoLogin(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("actionDoLogin(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated())
		{
			String username = (String) request.getParameter("username");
			String password = (String) request.getParameter("password");

			UsernamePasswordToken token = new UsernamePasswordToken(username, password);

			//this is all you have to do to support 'remember me' (no config - built in!):
			String strRememberme = (String) request.getParameter("rememberMe");
			if (StringUtils.isNotBlank(strRememberme))
			{
				boolean rememberme = Boolean.parseBoolean(strRememberme);
				token.setRememberMe(rememberme);
			}
			List<String> messages = getMessages(request);
			try
			{
				currentUser.login(token);
				log.debug("User {} successfully logged in", username);
				messages.add("Successfully logged in!");
				// TODO: Where to go?
				actionShow(request, response);
			}
			catch (UnknownAccountException uae)
			{
				//username wasn't in the system, show them an error message?
				log.debug("User {} not logged in: UnknownAccountException", username);
				messages.add("Username and password do not match!");
				actionLogin(request, response);
			}
			catch (IncorrectCredentialsException ice)
			{
				//password didn't match, try again?
				log.debug("User {} not logged in: IncorrectCredentialsException", username);
				messages.add("Username and password do not match!");
				actionLogin(request, response);
			}
			catch (LockedAccountException lae)
			{
				//account for that username is locked - can't login.  Show them a message?
				log.debug("User {} not logged in: LockedAccountException", username);
				messages.add("Username and password do not match!");
				actionLogin(request, response);
			}
			catch (AuthenticationException ae)
			{
				//unexpected condition - error?
				log.error("User {} not logged in: AuthenticationException - {}", username, ae.getMessage());
				messages.add("An unkonwn error occurred! Try again later!");
				actionLogin(request, response);
			}
			finally
			{
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			}
		}

	}


	/**
	 * actionLogout:
	 * Performs the logout
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionDoLogout(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("actionLogout(HttpServletRequest, HttpServletResponse");
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
		log.trace("actionEdit(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		if (checkPermission(currentUser, "page:edit"))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			MarkupPage markupPage = maxDocs.getMarkupPage(pagePath);
			request.setAttribute(MaxDocsConstants.MAXDOCS_MARKUP_PAGE, markupPage);
			request.getRequestDispatcher("/WEB-INF/templates/" + getTemplate() + "/edit.jsp").forward(
				request, response);
		}
		else
		{
			log.debug("Missing edit permission");
			List<String> messages = getMessages(request);
			if (username == null)
			{
				messages.add("No edit permission for unkown users");

			}
			else
			{
				messages.add("Missing edit permission for user " + username);
			}
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			actionShow(request, response);
		}
	}


	/**
	 * actionLogin:
	 * Shows the login page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void actionLogin(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("actionLogin(HttpServletRequest, HttpServletResponse");
		request.getRequestDispatcher("/WEB-INF/templates/" + getTemplate() + "/login.jsp").forward(request,
			response);
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
		log.trace("actionRename(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		if (checkPermission(currentUser, "page:rename"))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			// TODO: maxDocs.rename(pagePath, newPagePath);
		}
		else
		{
			log.debug("Missing rename permission");
			List<String> messages = getMessages(request);
			if (username == null)
			{
				messages.add("No rename permission for unkown users");

			}
			else
			{
				messages.add("Missing rename permission for user " + username);
			}
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		}
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
		log.trace("actionSave(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		List<String> messages = getMessages(request);
		if (checkPermission(currentUser, "page:save"))// TODO: which permissions allow saving?
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			MarkupPage newPage = new MarkupPage();
			newPage.setAuthor(username);
			newPage.setContent(request.getParameter(PARAMETER_NAME_CONTENT));
			newPage.setEditor(username);
			newPage.setMarkupLanguage(request.getParameter(PARAMETER_NAME_MARKUP));
			newPage.setPagePath(pagePath);
			if (StringUtils.isNotBlank(request.getParameter(PARAMETER_NAME_TAGS)))
			{
				String tags = request.getParameter(PARAMETER_NAME_TAGS);
				String[] tagArray = StringUtils.splitByWholeSeparator(tags, ", ");
				Set<String> tagList = Collections.synchronizedSet(new HashSet<String>());
				for (String tag : tagArray)
				{
					tagList.add(tag.trim());
				}
				newPage.setTags(tagList);
			}
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
		}
		else
		{
			log.debug("Missing save permission");
			if (username == null)
			{
				messages.add("No save permission for unkown users");

			}
			else
			{
				messages.add("Missing save permission for user " + username);
			}
		}
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
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
		log.trace("actionShow(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		// TODO, 03.02.2012: check user role
		request.getRequestDispatcher("/WEB-INF/templates/" + getTemplate() + "/show.jsp").forward(request,
			response);
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
		log.trace("actionSource(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		if (checkPermission(currentUser, "page:viewSource"))
		{
			request.getRequestDispatcher("/WEB-INF/templates/" + getTemplate() + "/source.jsp").forward(request,
				response);
		}
		else
		{
			log.debug("Missing viewSource permission");
			List<String> messages = getMessages(request);
			if (username == null)
			{
				messages.add("No viewSource permission for unkown users");

			}
			else
			{
				messages.add("Missing viewSource permission for user " + username);
			}
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			actionShow(request, response);
		}
	}

	/**
	 * getTemplate:
	 * returns the template name
	 * 
	 * @return the configured template name
	 */
	private String getTemplate()
	{
		log.trace("getTemplate()");
		ApplicationContext context = WebApplicationContextUtils
			.getRequiredWebApplicationContext(getServletContext());
		String templateName = (String) context.getBean("templateName");
		log.debug("templateName={}", templateName);
		return templateName;
	}


	/**
	 * checkPermission:
	 * Checks, if currentUser is permitted for permission.
	 * 
	 * @param currentUser
	 * @param permission
	 * @return <code>true</code>, if user is permitted
	 */
	private boolean checkPermission(Subject currentUser, String permission)
	{
		log.trace("checkPermission(Subject, String)");
		if (currentUser.isPermitted(permission))
		{
			return true;
		}
		return false;
	}

	
	/**
	 * getMessages:
	 * Returns the messages list from teh request.
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the servlet
	 * @return the message list
	 */
	private List<String> getMessages(HttpServletRequest request)
	{
		List<String> messages = (List<String>) request.getAttribute(MaxDocsConstants.MAXDOCS_MESSAGES);
		if (messages == null)
		{
			messages = new ArrayList<String>();
		}
		return messages;
	}
}
