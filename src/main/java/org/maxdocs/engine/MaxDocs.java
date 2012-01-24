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
package org.maxdocs.engine;

import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;

/**
 * MaxDocs:
 * Interface for the engine of MaxDocs.
 * 
 * @author Team maxdocs.org
 * 
 */
public interface MaxDocs
{

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public HtmlPage getHtmlPage(String pagePath);

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public MarkupPage getMarkupPage(String pagePath);

	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if the pagePath exists
	 */
	public boolean exists(String pagePath);

	/**
	 * getTagCloud:
	 * Returns a map with the tags of the tag cloud.
	 * Keys of the map are the tags.
	 * Value of the map is the weight of the tag as Integer.
	 * 
	 * @return a map with the tag cloud
	 */
	public Map<String, Integer> getTagCloud();


	/**
	 * save:
	 * Saves the given markup page.
	 * 
	 * @param markupPage will be saved to the content folder
	 * @return <code>true</code> if saving succeeds
	 */
	public boolean save(MarkupPage markupPage) throws ConcurrentEditException, EditWithoutChangesException;

	/**
	 * getMarkupLangages:
	 * Returns a map with all supported markup languages.
	 * 
	 * key is service name.
	 * value is display name.
	 *
	 * @return all supported markup languages as map.
	 */
	public Map<String, String> getMarkupLangages();

	/**
	 * getDefaultMarkupLangage:
	 * Returns the default markup language.
	 * 
	 * @return the default markup language
	 */
	public String getDefaultMarkupLangage();
}
