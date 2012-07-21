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

import static org.junit.Assert.assertTrue;

import java.io.File;

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
	private Storage storage;	

	@Before
	public void setUp()
	{
		String tempDir = System.getProperty("java.io.tmpdir");
		String fileSeperator = System.getProperty("file.separator");
		if(tempDir != null)
		{
			String contentDir = tempDir + fileSeperator + "content";
			File f = new File(contentDir);
			if(f.exists())
			{
				remove(f);
			}
			storage = new FileStorage(contentDir);	
		}

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

	@Test
	public void testSave()
	{
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
			// TODO Auto-generated catch block
			// log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (EditWithoutChangesException e)
		{
			// TODO Auto-generated catch block
			// log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

}
