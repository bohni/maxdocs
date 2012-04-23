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
