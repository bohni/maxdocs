package org.maxdocs.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.plugin.MaxDocsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MaxDocsFilter:
 * Filter for MaxDocs to differ between static (/internal) and dynamic (/content) content.
 * 
 * @author Team maxdocs.org
 */
public class MaxDocsFilter implements Filter
{
	private static Logger log = LoggerFactory.getLogger(MaxDocsFilter.class);
	
	private Map<String, MaxDocsPlugin> pluginList = new HashMap<>();


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		log.trace("destroy()");
	}


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
		throws IOException, ServletException
	{
		log.debug("doFilter({})", ((HttpServletRequest) request).getRequestURL());
		
		String uri = ((HttpServletRequest) request).getRequestURI();
		String contextPath = ((HttpServletRequest) request).getContextPath();
		String servletPath = ((HttpServletRequest) request).getServletPath();
		String pathInfo = ((HttpServletRequest) request).getPathInfo();
		log.debug("RequestURI="+uri+" ServletPath="+servletPath + " ContextPath=" + contextPath 
			+ " PathInfo=" + pathInfo);
		
		if (servletPath.startsWith("/internal")) 
		{
			if(servletPath.startsWith("/internal/plugin/"))
			{
				String pluginName= StringUtils.right(servletPath, servletPath.lastIndexOf("/"));
				if(pluginList.containsKey(pluginName))
				{
					log.debug("/internal/plugin/ -> to plugin");
					log.debug("Call Plugin '{}'", pluginName);
					MaxDocsPlugin plugin = pluginList.get(pluginName);
					plugin.forward(request, response);
				}
			}
			else
			{
				log.debug("/internal -> to default servlet");
				chain.doFilter(request, response); // Goes to default servlet.
			}
		} 
		else 
		{
			String newUri = "/content" + servletPath;
			log.debug("-> to new URI {}", newUri);
		    request.getRequestDispatcher(newUri).forward(request, response);
		}
	}


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		log.trace("init()");
	}
}
