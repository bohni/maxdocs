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

import java.io.File;
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
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.Message;
import org.maxdocs.data.Severity;
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
	private static final String TEMPLATES_ROOT = "/WEB-INF/templates/";

	private static final String ACTION_DELETE = "delete";
	private static final String ACTION_DO_LOGIN = "dologin";
	private static final String ACTION_DO_LOGOUT = "dologout";
	private static final String ACTION_EDIT = "edit";
	private static final String ACTION_INFO = "info";
	private static final String ACTION_LOGIN = "login";
	private static final String ACTION_RENAME = "rename";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_SHOW = "show";
	private static final String ACTION_SOURCE = "source";

	private static final int BREADCRUMBS_COUNT = 5;

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
			pagePath += DEFAULT_PAGE_NAME; //TODO: default name from configuration?
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
			delete(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_DO_LOGIN))
		{
			doLogin(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_DO_LOGOUT))
		{
			doLogout(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_EDIT))
		{
			edit(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_INFO))
		{
			info(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_LOGIN))
		{
			login(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_RENAME))
		{
			rename(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SAVE))
		{
			save(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SHOW))
		{
			show(request, response);
		}
		else if (StringUtils.equalsIgnoreCase(action, ACTION_SOURCE))
		{
			source(request, response);
		}
		else
		{
			show(request, response);
		}
		response.setCharacterEncoding("UTF-8");
	}


	/**
	 * buildBreadcrumbs:
	 * Build the breadcrumbs
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 */
	private void buildBreadcrumbs(HttpServletRequest request)
	{
		log.trace("buildBreadcrumbs(HttpServletRequest)");
		// get Breadcrumbs from session 
		CircularFifoBuffer breadcrumbs = (CircularFifoBuffer) request.getSession().getAttribute(
			MaxDocsConstants.MAXDOCS_BREADCRUMBS);
		if (breadcrumbs == null)
		{
			ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
			Integer count = (Integer) context.getBean("breadcrumbsCount");
			if (count == null)
			{
				count = Integer.valueOf(BREADCRUMBS_COUNT);
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
	}


	/**
	 * delete:
	 * Delete a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("delete(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();

		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:delete", messages))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			if (!maxDocs.delete(pagePath))
			{
				messages.add(new Message("Page '" + pagePath + "' could not be deleted!", Severity.ERROR));
			}
		}
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		show(request, response);
	}


	/**
	 * doLogin:
	 * Does the login with Apache Shiro
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void doLogin(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("doLogin(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated())
		{
			String username = (String) request.getParameter("username");
			String password = (String) request.getParameter("password");

			UsernamePasswordToken token = new UsernamePasswordToken(username, password);

			//this is all you have to do to support 'remember me' (no config - built in!):
			boolean rememberme = false;
			String strRememberme = (String) request.getParameter("rememberMe");
			if (StringUtils.isNotBlank(strRememberme))
			{
				rememberme = Boolean.parseBoolean(strRememberme);
			}
			token.setRememberMe(rememberme);
			List<Message> messages = getMessages(request);
			try
			{
				currentUser.login(token);
				log.debug("User {} successfully logged in", username);
				messages.add(new Message("Successfully logged in!", Severity.ERROR));
				// TODO: Where to go?
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
				show(request, response);
			}
			catch (UnknownAccountException uae)
			{
				//username wasn't in the system, show them an error message?
				log.debug("User {} not logged in: UnknownAccountException", username);
				messages.add(new Message("Username and password do not match!", Severity.ERROR));
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
				login(request, response);
			}
			catch (IncorrectCredentialsException ice)
			{
				//password didn't match, try again?
				log.debug("User {} not logged in: IncorrectCredentialsException", username);
				messages.add(new Message("Username and password do not match!", Severity.ERROR));
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
				login(request, response);
			}
			catch (LockedAccountException lae)
			{
				//account for that username is locked - can't login.  Show them a message?
				log.debug("User {} not logged in: LockedAccountException", username);
				messages.add(new Message("Username and password do not match!", Severity.ERROR));
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
				login(request, response);
			}
			catch (AuthenticationException ae)
			{
				//unexpected condition - error?
				log.error("User {} not logged in: AuthenticationException - {}", username, ae.getMessage());
				messages.add(new Message("An unkonwn error occurred! Try again later!", Severity.ERROR));
				request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
				login(request, response);
			}
		}

	}


	/**
	 * doLogout:
	 * Performs the logout
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void doLogout(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("logout(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("Successfully logged out!", Severity.ERROR));
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		show(request, response);
	}


	/**
	 * edit:
	 * Edit a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void edit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("edit(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:edit", messages))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			MarkupPage markupPage = maxDocs.getMarkupPage(pagePath);
			request.setAttribute(MaxDocsConstants.MAXDOCS_MARKUP_PAGE, markupPage);
			request.getRequestDispatcher(TEMPLATES_ROOT + getTemplate() + "/edit.jsp").forward(request,
				response);
		}
		else
		{
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			show(request, response);
		}
	}


	/**
	 * info:
	 * Shows info of the page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void info(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("info(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:info", messages))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			request.setAttribute("VERSIONS", maxDocs.getVersions(pagePath));
			MarkupPage markupPage = maxDocs.getMarkupPage(pagePath);
			request.setAttribute(MaxDocsConstants.MAXDOCS_MARKUP_PAGE, markupPage);
			request.getRequestDispatcher(TEMPLATES_ROOT + getTemplate() + "/info.jsp").forward(request,
				response);
		}
		else
		{
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			show(request, response);
		}
	}


	/**
	 * login:
	 * Shows the login page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void login(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("login(HttpServletRequest, HttpServletResponse");
		request.getRequestDispatcher(TEMPLATES_ROOT + getTemplate() + "/login.jsp")
			.forward(request, response);
	}


	/**
	 * rename:
	 * Rename a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void rename(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("rename(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = getUsername(currentUser);
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:rename", messages))
		{
			String pagePath = (String) request.getAttribute(MaxDocsConstants.MAXDOCS_PAGE_PATH);
			MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
			// TODO: maxDocs.rename(pagePath, newPagePath);
		}
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		show(request, response);
	}


	/**
	 * save:
	 * Save a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void save(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("save(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		String username = getUsername(currentUser);
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:save", messages))// TODO: which permissions allow saving?
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
			try
			{
				boolean success = maxDocs.save(newPage);
				if (!success)
				{
					messages.add(new Message("Uppps! An unknown error occured!", Severity.ERROR));
				}
			}
			catch (ConcurrentEditException e)
			{
				messages.add(new Message("There where other changes saved while editing this page!",
					Severity.ERROR));
				// TODO: Merge anbieten
			}
			catch (EditWithoutChangesException e)
			{
				messages.add(new Message("No changes where made. No new version created.", Severity.INFO));
			}

		}
		request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
		show(request, response);
	}


	/**
	 * show:
	 * Show a page
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void show(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("show(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:view", messages))
		{
			request.getRequestDispatcher(TEMPLATES_ROOT + getTemplate() + "/show.jsp").forward(request,
				response);
		}
		else
		{
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			login(request, response);
		}
	}


	/**
	 * source:
	 * Show the page source
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @param response an HttpServletResponse object that contains the response the servlet sends to the
	 *        client
	 * @throws ServletException - if the target resource throws this exception
	 * @throws IOException - if the target resource throws this exception
	 */
	private void source(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.trace("source(HttpServletRequest, HttpServletResponse");
		Subject currentUser = SecurityUtils.getSubject();
		List<Message> messages = getMessages(request);
		if (checkPermission(currentUser, "page:viewSource", messages))
		{
			request.getRequestDispatcher(TEMPLATES_ROOT + getTemplate() + "/source.jsp").forward(request,
				response);
		}
		else
		{
			request.setAttribute(MaxDocsConstants.MAXDOCS_MESSAGES, messages);
			show(request, response);
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

		File templateDir = new File(getServletContext().getRealPath(TEMPLATES_ROOT + templateName));
		if (!templateDir.exists())
		{
			templateName = "default";
		}
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
	private boolean checkPermission(Subject currentUser, String permission, List<Message> messages)
	{
		log.trace("checkPermission(Subject, String)");

		Subject user = currentUser;
		String username = (String) currentUser.getPrincipal();

		if (StringUtils.isBlank(username))
		{
			// Nicht angemeldet, also anonymous verwenden!
			PrincipalCollection principals = new SimplePrincipalCollection("anonymous", "maxdocsRealm");
			user = new Subject.Builder().principals(principals).buildSubject();
		}

		if (user.isPermitted(permission))
		{
			return true;
		}
		else
		{
			log.debug("Missing {} permission for user {}", permission, username);
			if (username == null)
			{
				messages
					.add(new Message("No " + permission + " permission for unkown users", Severity.ERROR));

			}
			else
			{
				messages.add(new Message("Missing " + permission + " permission for user " + username,
					Severity.ERROR));
			}
		}
		return false;
	}


	/**
	 * getMessages:
	 * Returns the messages list from teh request.
	 * 
	 * @param request an HttpServletRequest object that contains the request the client has made of the
	 *        servlet
	 * @return the message list
	 */
	private List<Message> getMessages(HttpServletRequest request)
	{
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) request.getAttribute(MaxDocsConstants.MAXDOCS_MESSAGES);
		if (messages == null)
		{
			messages = new ArrayList<>();
		}
		return messages;
	}


	/**
	 * getCurrentUser:
	 * Returns the name of the current user or "Anonymous" if no Principal is set in the given Subject.
	 * 
	 * @param currentUser
	 * @return the username
	 */
	private String getUsername(Subject currentUser)
	{
		String username = (String) currentUser.getPrincipal();
		if (StringUtils.isBlank(username))
		{
			username = "Anonymous"; //TODO: i18n, IP address?
		}
		return username;
	}

}
