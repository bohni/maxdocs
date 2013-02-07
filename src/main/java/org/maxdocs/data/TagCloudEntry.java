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
package org.maxdocs.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TagCloudEntry
 * A TagCloudEntry stores all attributes to a single tag.
 * 
 * @author Team maxdocs.org
 */
public class TagCloudEntry
{
	private String tagName;

	private List<String> pages = Collections.synchronizedList(new ArrayList<String>());


	/**
	 * Minimal constructor. Contains required fields.
	 * Creates an {@link TagCloudEntry} object with the given parameters.
	 * 
	 * @param tagName the name of the tag
	 */
	public TagCloudEntry(String tagName)
	{
		this.tagName = tagName;
	}


	/**
	 * addPage:
	 * Adds the pagePath to this tag
	 * 
	 * @param pagePath the pagePath to add
	 */
	public void addPage(String pagePath)
	{
		pages.add(pagePath);
		Collections.sort(pages);
	}


	/**
	 * getCount: Returns the count.
	 * 
	 * @return the count
	 */
	public int getCount()
	{
		return getPages().size();
	}


	/**
	 * getTagName: Returns the tagName.
	 * 
	 * @return the tagName
	 */
	public String getTagName()
	{
		return tagName;
	}


	/**
	 * getPages: Returns the pages.
	 * 
	 * @return the pages
	 */
	public List<String> getPages()
	{
		return pages;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[" + tagName + ":" + pages.size() + ":" + pages + "]";

	}
}
