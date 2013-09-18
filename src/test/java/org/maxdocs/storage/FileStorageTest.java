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
package org.maxdocs.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileStorageTest
 * Test for {@link FileStorage}.
 * 
 * @author Team maxdocs.org
 */
public class FileStorageTest
{
	private static Logger log = LoggerFactory.getLogger(FileStorageTest.class);
	private FileStorage storage;
	private File tempDir;


	/**
	 * setUp:
	 * Prepare Filestorage with temporary directory for file operations
	 * 
	 * @see Before
	 * @thorws IllegalArgumentException
	 * @thorws UnsupportedOperationException
	 * @throws IOException
	 * @throws SecurityException
	 */
	@Before
	public void setUp() throws IOException
	{
		log.trace("setUp()");
		tempDir = Files.createTempDirectory("maxdocs").toFile();
		storage = new FileStorage(tempDir.getAbsolutePath());
	}


	/**
	 * shutdown:
	 * 
	 * @see After
	 * @throws SecurityException
	 */
	@After
	public void shutdown()
	{
		log.trace("shutdown()");
		remove(tempDir);
	}


	/**
	 * remove:
	 * Removes the given file or directory recursively.
	 * 
	 * @param file the file or directory to delete.
	 * @throws SecurityException
	 */
	private void remove(File file)
	{
		log.trace("remove()");
		if (file == null)
		{
			return;
		}
		if (file.isDirectory())
		{
			for (File child : file.listFiles())
			{
				remove(child);
			}
		}
		if (!file.delete())
		{
			log.warn("File {} could not be deleted.", file.getAbsolutePath());
		}
	}


	/**
	 * testSave: Test for saving a page
	 */
	@Test
	public void testSave()
	{
		log.trace("testSave");
		MarkupPage page = new MarkupPage();
		page.setAuthor("Author");
		page.setMarkupLanguage("mediawiki");
		page.setContent("Content");
		page.setPagePath("/Main");

		try
		{
			boolean success = storage.save(page);
			assertTrue("save failed", success);
		}
		catch (ConcurrentEditException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		catch (EditWithoutChangesException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}


	/**
	 * testPageToString: Test to transform a page to a String for saving
	 */
	@Test
	public void testPageToString()
	{
		log.trace("testPageToString");
		MarkupPage page = new MarkupPage();
		Date date = new Date();
		page.setAuthor("Author");
		page.setContent("Content");
		page.setCurrentVersionCreationDate(date);
		page.setFirstVersionCreationDate(date);
		page.setMarkupLanguage("mediawiki");
		page.setPagePath("/Main");
		page.addTag("Tag1");
		page.addTag("Tag2");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
		String expected = "pagePath=/Main\nauthor=Author\neditor=null\ncreationDateFirst=" + sdf.format(date)
			+ "\ncreationDateThis=" + sdf.format(date)
			+ "\ncontentType=mediawiki\nversion=0\ntags=Tag2, Tag1\n\nContent";
		assertEquals("Output not equal!", expected, storage.pageToString(page));
	}


	/**
	 * testPageLoad:
	 * Test loading of the current version of a page
	 */
	@Test
	public void testPageLoad()
	{
		log.trace("testPageLoad");
		String author = "Author";
		String content = "Content";
		Date date = new Date();
		String markupLanguage = "mediawiki";
		String pagePath = "/Main";
		String tag1 = "Tag1";
		String tag2 = "Tag2";

		MarkupPage page = new MarkupPage();
		page.setAuthor(author);
		page.setContent(content);
		page.setCurrentVersionCreationDate(date);
		page.setFirstVersionCreationDate(date);
		page.setMarkupLanguage(markupLanguage);
		page.setPagePath(pagePath);
		page.addTag(tag1);
		page.addTag(tag2);

		try
		{
			boolean success = storage.save(page);
			assertTrue("save failed", success);
			MarkupPage loaded = storage.load(pagePath);
			assertEquals("author not equal", author, loaded.getAuthor());
			assertEquals("markupLanguage not equal", markupLanguage, loaded.getMarkupLanguage());
			assertEquals("content not equal", content + System.getProperty("line.separator"),
				loaded.getContent());
			assertEquals("pagePath not equal", pagePath, loaded.getPagePath());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			assertEquals("lastChangeDate not equal", sdf.format(date),
				sdf.format(loaded.getCurrentVersionCreationDate()));
			assertEquals("creationDate not equal", sdf.format(date),
				sdf.format(loaded.getFirstVersionCreationDate()));
			assertEquals("Tags.size not euqal", 2, loaded.getTags().size());
			assertTrue("tag1 not in Tags", loaded.getTags().contains(tag1));
			assertTrue("tag2 not in Tags", loaded.getTags().contains(tag2));
		}
		catch (ConcurrentEditException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		catch (EditWithoutChangesException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
}
