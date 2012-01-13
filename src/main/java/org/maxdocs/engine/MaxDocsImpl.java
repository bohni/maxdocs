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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.parser.MarkupParser;
import org.maxdocs.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MaxDocs:
 * Main engine of MaxDocs.
 * 
 * @author Team maxdocs.org
 */
public class MaxDocsImpl implements MaxDocs
{
	private static Logger log = LoggerFactory.getLogger(MaxDocsImpl.class);

	private Storage storage;
	private MarkupParser parser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maxdocs.engine.MaxDocs#getHtmlPage(java.lang.String)
	 */
	@Override
	public HtmlPage getHtmlPage(String pagePath)
	{
		log.trace("getHtmlPage({})", pagePath);
		
		if(!exists(pagePath))
		{
			return null;
		}

		MarkupPage markupPage = storage.load(pagePath);
		HtmlPage htmlPage = parser.parseToHtml(markupPage);
		
		return htmlPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maxdocs.engine.MaxDocs#getTagCloud()
	 */
	@Override
	public Map<String, Integer> getTagCloud()
	{
		log.trace("getTagCloud()");
		List<TagCloudEntry> tagCloudEntries = storage.getTagCloudEntries();
		
		
		int max = 0;
		int min = Integer.MAX_VALUE;
		
		for (Iterator<TagCloudEntry> iterator = tagCloudEntries.iterator(); iterator.hasNext();)
		{
			TagCloudEntry tagCloudEntry = (TagCloudEntry) iterator.next();
			if(tagCloudEntry.getCount()> max)
			{
				max = tagCloudEntry.getCount();
			}
			if(tagCloudEntry.getCount()< min)
			{
				min = tagCloudEntry.getCount();
			}
		}
		
		int step = (max-min)/7 + 1;

		Map<String, Integer> tagCloud = new HashMap<String, Integer>();

		for (Iterator<TagCloudEntry> iterator = tagCloudEntries.iterator(); iterator.hasNext();)
		{
			TagCloudEntry tagCloudEntry = (TagCloudEntry) iterator.next();
			int weight = tagCloudEntry.getCount()/step;
			tagCloud.put(tagCloudEntry.getTagName() + "(" + tagCloudEntry.getCount() + ")", weight > 0 ? weight : 1);
		}

		return tagCloud;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maxdocs.engine.MaxDocs#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		return storage.exists(pagePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maxdocs.engine.MaxDocs#getMarkupPage(java.lang.String)
	 */
	@Override
	public MarkupPage getMarkupPage(String pagePath)
	{
		log.trace("getMarkupPage({})", pagePath);

		if(!exists(pagePath))
		{
			return null;
		}

		MarkupPage markupPage = storage.load(pagePath);
		return markupPage;
	}

	/* (non-Javadoc)
	 * @see org.maxdocs.engine.MaxDocs#save(org.maxdocs.data.MarkupPage, org.maxdocs.data.MarkupPage)
	 */
	public boolean save(MarkupPage oldPage, MarkupPage newPage)
	{
		log.trace("save()");
		if(oldPage != null)
		{
			if(oldPage.getVersion() == newPage.getVersion())
			{
				newPage.setVersion(oldPage.getVersion() + 1);
				return storage.save(oldPage, newPage);
			}
			else
			{
				return false; 
			}
		}
		else
		{
			return storage.save(newPage);
		}
	}

	
	
	
	/* (non-Javadoc)
	 * @see org.maxdocs.engine.MaxDocs#getMarkupLangages()
	 */
	@Override
	public Map<String, String> getMarkupLangages()
	{
		return parser.getMarkupLanguages();
	}

	/**
	 * setStorage: Sets the storage.
	 * 
	 * @param storage
	 *            the storage to set
	 */
	public void setStorage(Storage storage)
	{
		this.storage = storage;
	}

	/**
	 * setParser() sets the parser
	 *
	 * @param parser the parser to set
	 */
	public void setParser(MarkupParser parser)
	{
		this.parser = parser;
	}

	/* (non-Javadoc)
	 * @see org.maxdocs.engine.MaxDocs#getDefaultMarkupLangages()
	 */
	@Override
	public String getDefaultMarkupLangage()
	{
		return "MediaWiki";
	}
	
	
}
