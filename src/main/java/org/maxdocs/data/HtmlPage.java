/**
 * Copyright (c) 2010-2011, Team jspserver.net
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

import java.util.Date;

/**
 * HtmlPage:
 * An object containing the data of a page.
 * The content is already rendered in HTML.
 *
 * @author Team jspserver.net
 */
public class HtmlPage
{
	private int version;

	private Date firstVersionCreationDate;

	private Date currentVersionCreationDate;

	private String author;

	private String editor;

	private String contentType;

	private String content;

	private String pageName;

	private String pagePath;

	/**
	 * getVersion() returns the version
	 *
	 * @return the version
	 */
	public int getVersion()
	{
		return this.version;
	}

	/**
	 * setVersion() sets the version
	 *
	 * @param version the version to set
	 */
	public void setVersion(int version)
	{
		this.version = version;
	}

	/**
	 * getFirstVersionCreationDate() returns the firstVersionCreationDate
	 *
	 * @return the firstVersionCreationDate
	 */
	public Date getFirstVersionCreationDate()
	{
		return this.firstVersionCreationDate;
	}

	/**
	 * setFirstVersionCreationDate() sets the firstVersionCreationDate
	 *
	 * @param firstVersionCreationDate the firstVersionCreationDate to set
	 */
	public void setFirstVersionCreationDate(Date firstVersionCreationDate)
	{
		this.firstVersionCreationDate = firstVersionCreationDate;
	}

	/**
	 * getCurrentVersionCreationDate() returns the currentVersionCreationDate
	 *
	 * @return the currentVersionCreationDate
	 */
	public Date getCurrentVersionCreationDate()
	{
		return this.currentVersionCreationDate;
	}

	/**
	 * setCurrentVersionCreationDate() sets the currentVersionCreationDate
	 *
	 * @param currentVersionCreationDate the currentVersionCreationDate to set
	 */
	public void setCurrentVersionCreationDate(Date currentVersionCreationDate)
	{
		this.currentVersionCreationDate = currentVersionCreationDate;
	}

	/**
	 * getAuthor() returns the author
	 *
	 * @return the author
	 */
	public String getAuthor()
	{
		return this.author;
	}

	/**
	 * setAuthor() sets the author
	 *
	 * @param author the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * getEditor() returns the editor
	 *
	 * @return the editor
	 */
	public String getEditor()
	{
		return this.editor;
	}

	/**
	 * setEditor() sets the editor
	 *
	 * @param editor the editor to set
	 */
	public void setEditor(String editor)
	{
		this.editor = editor;
	}

	/**
	 * getContentType() returns the contentType
	 *
	 * @return the contentType
	 */
	public String getContentType()
	{
		return this.contentType;
	}

	/**
	 * setContentType() sets the contentType
	 *
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * getContent() returns the content
	 *
	 * @return the content
	 */
	public String getContent()
	{
		return this.content;
	}

	/**
	 * setContent() sets the content
	 *
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * getPageName() returns the pageName
	 *
	 * @return the pageName
	 */
	public String getPageName()
	{
		return this.pageName;
	}

	/**
	 * setPageName() sets the pageName
	 *
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}

	/**
	 * getPagePath() returns the pagePath
	 *
	 * @return the pagePath
	 */
	public String getPagePath()
	{
		return this.pagePath;
	}

	/**
	 * setPagePath() sets the pagePath
	 *
	 * @param pagePath the pagePath to set
	 */
	public void setPagePath(String pagePath)
	{
		this.pagePath = pagePath;
	}
}
