package org.maxdocs.plugin;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * MaxDocsPlugin:
 * Interface for plugins of MaxDocs.
 *
 * @author Team maxdocs.org
 */
public interface MaxDocsPlugin
{

	/**
	 * forward:
	 * The plugin has to handle the forward
	 *
	 * @param request a ServletRequest object that represents the request the client makes of the servlet
	 * @param response a ServletResponse object that represents the response the servlet returns to the client
	 * @throws IOException If an input or output exception occurs
	 * @throws IllegalStateException If the response was committed before this method call
	 */
	void forward(ServletRequest request, ServletResponse response) throws IOException, IllegalStateException;
	
	/**
	 * getHtml:
	 * Returns the HTML snippet of the plugin to the given parameters.
	 * 
	 * @param params the parameter string from the plugin markup
	 * @return the generated HTML snippet
	 */
	String getHtml(String params, String cssStyle, String cssClass);
}
