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
package org.maxdocs.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupPageTest
 * TODO, 30.12.2011: Documentation
 * 
 * @author Team maxdocs.org
 *
 */
public class MarkupPageTest
{
	private static Logger log = LoggerFactory.getLogger(MarkupPageTest.class);

	@Test
	public void testCopyContructor()
	{
		log.trace("testCopyContructor");
		String author = "author";
		String content = "Content";
		String contentType = "ContentType";
		Date currentVersionCreationDate = new Date();
		String editor = "editor";
		Date firstVersionCreationDate = new Date();
		String pageName = "PageName";
		String pagePath = "/PageName";
		Set<String> tags = new HashSet<String>();
		int version = 1;
		
		MarkupPage page1 = new MarkupPage();
		page1.setAuthor(author);
		page1.setContent(content);
		page1.setMarkupLanguage(contentType);
		page1.setCurrentVersionCreationDate(currentVersionCreationDate);
		page1.setEditor(editor);
		page1.setFirstVersionCreationDate(firstVersionCreationDate);
		page1.setPagePath(pagePath);
		page1.setTags(tags);
		page1.setVersion(version);
		
		MarkupPage page2 = new MarkupPage(page1);
		page2.setCurrentVersionCreationDate(currentVersionCreationDate);
		
		assertNotSame(page1, page2);
		assertEquals(author, page2.getAuthor());
		assertEquals(content, page2.getContent());
		assertEquals(contentType, page2.getMarkupLanguage());
		assertEquals(currentVersionCreationDate, page2.getCurrentVersionCreationDate());
		assertEquals(editor, page2.getEditor());
		assertEquals(firstVersionCreationDate, page2.getFirstVersionCreationDate());
		assertEquals(pageName, page2.getPageName());
		assertEquals(pagePath, page2.getPagePath());
		assertEquals(tags, page2.getTags());
		assertEquals(version, page2.getVersion());
	}
}
