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
 *
 */
public class FileStorageTest
{
	private static Logger log = LoggerFactory.getLogger(FileStorageTest.class);
	private FileStorage storage;	
	private File tempDir;
	/**
	 * setUp:
	 * @see Before
	 * @throws IOException
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
	 * @see After
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
	 */
	private void remove(File file)
	{
		log.trace("remove()");
		if(file == null)
		{
			return;
		}
		if(file.isDirectory())
		{
			for (File child : file.listFiles())
			{
				remove(child);
			}
		}
		if(!file.delete())
		{
			log.warn("File {} could not be deleted.", file.getAbsolutePath());
		}
	}

	/**
	 * testSave: Test for saving a page
	 * 
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
		}
		catch (EditWithoutChangesException e)
		{
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * testPageToString: Test to transform a page to a String for saving
	 * 
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

}
