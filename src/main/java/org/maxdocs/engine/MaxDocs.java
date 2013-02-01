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
package org.maxdocs.engine;

import java.util.List;
import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.PageLight;
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
	 * delete:
	 * Deletes the page with the given path.
	 * 
	 * @param pagePath the complete path of the page
	 * @return <code>true</code> if and only if the file is successfully deleted; <code>false</code> otherwise
	 */
	boolean delete(String pagePath);


	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the complete path of the page
	 * @return <code>true</code> if the pagePath exists; <code>false</code> otherwise
	 */
	boolean exists(String pagePath);


	/**
	 * getDefaultMarkupLangage:
	 * Returns the default markup language.
	 * 
	 * @return the default markup language
	 */
	String getDefaultMarkupLangage();


	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	HtmlPage getHtmlPage(String pagePath);


	/**
	 * getMarkupLangages:
	 * Returns a map with all supported markup languages.
	 * 
	 * key is service name.
	 * value is display name.
	 *
	 * @return all supported markup languages as map.
	 */
	Map<String, String> getMarkupLangages();


	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	MarkupPage getMarkupPage(String pagePath);


	/**
	 * getTagCloud:
	 * Returns a map with the tags of the tag cloud.
	 * Keys of the map are the tags.
	 * Value of the map is the weight of the tag as Integer.
	 * 
	 * @return a map with the tag cloud
	 */
	Map<String, Integer> getTagCloud();

	/**
	 * getVersions:
	 * Returns a list with all versions of the given page
	 * 
	 * @param pagePath the complete path of the page
	 * @return list containing {@link PageLight} objects for every version
	 */
	List<PageLight> getVersions(String pagePath);
	/**
	 * rename:
	 * Renames the page with the given pagePath
	 * 
	 * @param pagePath the complete current path of the page
	 * @param newPage a page with the new page path, current version, author and editor set
	 * @return <code>true</code> if and only if the file is successfully renamed; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 */
	boolean rename(String pagePath, MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException;


	/**
	 * save:
	 * Saves the given markup page.
	 * 
	 * @param markupPage will be saved to the content folder
	 * @return <code>true</code> if and only if the page is successfully saved; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 */
	boolean save(MarkupPage markupPage) throws ConcurrentEditException, EditWithoutChangesException;
}
