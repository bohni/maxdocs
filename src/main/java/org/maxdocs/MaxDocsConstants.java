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
package org.maxdocs;

/**
 * MaxDocsConstants:
 * Class with different constants.
 * <ul>
 * <li>key values for storing objects in context/session/request.</li>
 * </ul>
 * 
 * @author Team maxdocs.org
 */
public final class MaxDocsConstants
{
	/**
	 * Key for storing the engine
	 */
	public static final String MAXDOCS_ENGINE = "MAXDOCS_ENGINE";

	/**
	 * Key for storing the page path in the request
	 */
	public static final String MAXDOCS_PAGE_PATH = "MAXDOCS_PAGE_PATH";

	public static final String MAXDOCS_BREADCRUMBS = "MAXDOCS_BREADCRUMBS";

	public static final String MAXDOCS_MARKUP_PAGE = "MAXDOCS_MARKUP_PAGE";
	
	private MaxDocsConstants()
	{
		// no instance needed
	}
}
