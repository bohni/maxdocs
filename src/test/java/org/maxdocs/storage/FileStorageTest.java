package org.maxdocs.storage;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.maxdocs.data.MarkupPage;
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
	public void testCreatePage()
	{
		MarkupPage page = new MarkupPage();
		page.setAuthor("Author");
		page.setContent("Content");
		
	}

}
