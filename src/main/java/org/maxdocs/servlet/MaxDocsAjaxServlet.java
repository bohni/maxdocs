package org.maxdocs.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.MaxDocsConstants;
import org.maxdocs.engine.MaxDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;

/**
 * MaxDocsServlet:
 * Main servlet of MaxDocs.
 *
 * @author Team maxdocs.org
 */
public class MaxDocsAjaxServlet extends HttpServlet
{
	private static final String ACTION_ALL_TAGS = "/getAllTags";
	private static Logger log = LoggerFactory.getLogger(MaxDocsAjaxServlet.class);


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

		String ajaxAction = request.getPathInfo();
		log.debug("ajaxAction={}", ajaxAction);
		if (StringUtils.equalsIgnoreCase(ACTION_ALL_TAGS, ajaxAction))
		{
			PrintWriter out = response.getWriter();
			out.print(actionGetAllTags(request.getParameter("term")));
		}

		response.setCharacterEncoding("UTF-8");
	}


	/**
	 * actionGetAllTags:
	 */
	private String actionGetAllTags(String pattern)
	{
		log.trace("actionGetAllTags()");
		MaxDocs maxDocs = (MaxDocs) getServletContext().getAttribute(MaxDocsConstants.MAXDOCS_ENGINE);
		Set<String> allTags =  maxDocs.getTagCloud().keySet();
		List<String> selectedTags = new ArrayList<String>(); 
		for (String tag: allTags)
		{
			if(StringUtils.contains(tag.toLowerCase(), pattern.toLowerCase()))
			{
				selectedTags.add(tag);
			}
		}
		String[] tags = selectedTags.toArray(new String[0]); 
		Gson gson = new Gson();
		String strTags = gson.toJson(tags);
		return strTags;
	}
}