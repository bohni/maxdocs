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
package org.maxdocs.storage;

import java.util.List;

import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;

/**
 * Storage:
 * Interface for storage providers of MaxDocs.
 *
 * @author Team maxdocs.org
 */
public interface Storage
{
	/**
	 * delete:
	 * Deletes the requested page.
	 * @param pagePath the requested page
	 * @return <code>true</code> if and only if the page is successfully deleted; <code>false</code> otherwise
	 */
	public boolean delete(String pagePath);


	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if the pagePath exists; <code>false</code> otherwise
	 */
	public boolean exists(String pagePath);


	/**
	 * getTagCloud:
	 * Returns a List with tags cloud entries.
	 * 
	 * @return a list with tag cloud entries
	 */
	public List<TagCloudEntry> getTagCloudEntries();


	/**
	 * load:
	 * Creates a MarkupPage object of the given page.
	 * 
	 * @param pagePath the requested page
	 * @return the MarkupPage object of the requested page
	 */
	public MarkupPage load(String pagePath);


	/**
	 * load:
	 * Creates a MarkupPage object of the given page.
	 * 
	 * @param pagePath the requested page
	 * @param version the requested version
	 * @return the MarkupPage object of the requested page
	 */
	public MarkupPage load(String pagePath, int version);


	/**
	 * rename:
	 * Renames the page with the given pagePath
	 * 
	 * @param pagePath the complete current path of the page
	 * @param newPagePath the complete new path of the page
	 * @return <code>true</code> if and only if the page is successfully renamed; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 */
	public boolean rename(String pagePath, String newPagePath) throws ConcurrentEditException;


	/**
	 * save:
	 * Persists the MarkupPage object in the storage.
	 * If the page exists, the current version will be saved in the versions folder. 
	 * 
	 * @param newPage the page to save
	 * @return <code>true</code> if and only if the page is successfully deleted; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 */
	public boolean save(MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException;
}
