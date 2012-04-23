package org.maxdocs.plugin;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractMaxdocsPlugin:
 * This is the abstract plugin, that every MaxDocs plugin has to extend.
 *
 * @author Team maxdocs.org
 */
public abstract class AbstractMaxdocsPlugin implements MaxDocsPlugin
{
	private static Logger log = LoggerFactory.getLogger(AbstractMaxdocsPlugin.class);

	/* (non-Javadoc)
	 * @see org.maxdocs.plugin.MaxDocsPlugin#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void forward(ServletRequest request, ServletResponse response) throws IOException, IllegalStateException
	{
		log.trace("forward(request, response)");
		HttpServletResponse res = (HttpServletResponse) response;
		log.debug("Status: {}", HttpServletResponse.SC_NOT_FOUND);
		res.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
