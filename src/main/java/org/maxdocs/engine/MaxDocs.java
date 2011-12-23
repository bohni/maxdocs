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
package org.maxdocs.engine;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MaxDocs:
 * Main engine of MaxDocs.
 *
 * @author Team jspserver.net
 */
public class MaxDocs
{
	private static Logger log = LoggerFactory.getLogger(MaxDocs.class);

	private Storage storage;
	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 *
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public HtmlPage getHtmlPage(String pagePath)
	{
		log.trace("getHtmlPage({})", pagePath);
		// // TODO 13.12.2011: pageData ermitteln
		HtmlPage htmlPage = new HtmlPage();
		htmlPage.setContent("");

		if(StringUtils.equals(pagePath, "/LeftMenu"))
		{
			htmlPage.setAuthor("John Doe Senior");
			htmlPage.setEditor("John Doe");
			htmlPage.setContent("<a href=\"#\">Link 1</a><br/><a href=\"#\">Link 2</a><br/><a href=\"#\">Link 3</a><br/><a href=\"#\">Link 4</a>");
			htmlPage.setContentType("text/textile");
			Calendar date = GregorianCalendar.getInstance();
			date.set(2011, 8, 16, 14, 27);
			htmlPage.setFirstVersionCreationDate(date.getTime());
			htmlPage.setCurrentVersionCreationDate(new Date());
			htmlPage.setPagePath(pagePath);
			htmlPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
			htmlPage.setVersion(2);
		}
		else
		{
			htmlPage.setAuthor("John Doe Senior");
			htmlPage.setEditor("John Doe");
			htmlPage.setContent("<p>This is the Content...</p><ul><li>List 1</li><li>List 2</li></ul><p><strong>Bold Text</strong></p>");
			htmlPage.setContentType("text/textile");
			Calendar date = GregorianCalendar.getInstance();
			date.set(2011, 8, 16, 14, 27);
			htmlPage.setFirstVersionCreationDate(date.getTime());
			htmlPage.setCurrentVersionCreationDate(new Date());
			htmlPage.setPagePath(pagePath);
			htmlPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
			htmlPage.setVersion(2);
		}

		return htmlPage;
	}

	/**
	 * getTagCloud:
	 * Creates a map with the tags of the tag cloud.
	 * Keys of the map are the tags
	 * Value of the map is the weight of the tag as Integer
	 * 
	 * @return a map with the tag cloud.
	 */
	public Map<String, Integer> getTagCloud()
	{
		Map<String, Integer> tagCloud = new HashMap<String, Integer>();
		tagCloud.put("wiki", Integer.valueOf(1));
		tagCloud.put("blog", Integer.valueOf(2));
		tagCloud.put("servlet", Integer.valueOf(1));
		tagCloud.put("linux", Integer.valueOf(3));
		tagCloud.put("tomcat", Integer.valueOf(5));
		tagCloud.put("java", Integer.valueOf(2));
		tagCloud.put("spring", Integer.valueOf(7));
		tagCloud.put("maxdocs", Integer.valueOf(6));
		return tagCloud;
	}

	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if the pagePath exists
	 */
	public boolean exists(String pagePath)
	{
		// TODO, 16.12.2011:
		return true;
	}

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 *
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public MarkupPage getMarkupPage(String pagePath)
	{
		log.trace("getMarkupPage({})", pagePath);

		MarkupPage markupPage = new MarkupPage();
		markupPage.setAuthor("John Doe Senior");
		markupPage.setEditor("John Doe");
		markupPage.setContent("! " + pagePath.substring(1) + "\n\nThis is the Content...\n\n* List 1\n* List 2\n\n__Bold Text__");
		markupPage.setContentType("text/textile");
		Calendar date = GregorianCalendar.getInstance();
		date.set(2011, 8, 16, 14, 27);
		markupPage.setFirstVersionCreationDate(date.getTime());
		markupPage.setCurrentVersionCreationDate(new Date());
		markupPage.setPagePath(pagePath);
		markupPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
		markupPage.setVersion(2);
		return markupPage;
	}

	/**
	 * getStorage: Returns the storage.
	 * 
	 * @return the storage
	 */
	public Storage getStorage()
	{
		return storage;
	}

	/**
	 * setStorage: Sets the storage.
	 * 
	 * @param storage the storage to set
	 */
	public void setStorage(Storage storage)
	{
		this.storage = storage;
	}
}