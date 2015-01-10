package org.maxdocs.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.TagCloudEntry;
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

	// test data
	private String author = "Author";
	private String content = "Content";
	private String markupLanguage = "mediawiki";
	private String[] tags = new String[] { "Tag1", "Tag2", "Tag3" };
	private Date date = new Date();


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
		remove(tempDir);
	}


	/**
	 * testBuildIndexes:
	 * Test building the indexes
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testBuildIndexes() throws ConcurrentEditException, EditWithoutChangesException
	{
		testSaveNormal();
		remove(new File(tempDir + "/1.txt"));
		storage.buildIndexes(true);
		assertFalse(storage.exists("/Main"));
	}


	/**
	 * testDeleteExisting:
	 * Tests deleting an exiting page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testDeleteExisting() throws ConcurrentEditException, EditWithoutChangesException
	{
		testSaveNormal();
		assertTrue("delete failed", storage.delete("/Main"));
	}


	/**
	 * testDeleteNonExisting:
	 * Tests deleting a non existing page
	 */
	@Test
	public void testDeleteNonExisting()
	{
		assertTrue("delete failed", storage.delete("/Main"));
	}


	/**
	 * testExists:
	 * Tests the existence of an existing page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testExistsDeleted() throws ConcurrentEditException, EditWithoutChangesException
	{
		testSaveNormal();
		remove(new File(tempDir + "/1.txt"));
		assertFalse("Page does exist", storage.exists("/Main"));
	}


	/**
	 * testExists:
	 * Tests the existence of an existing page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testExistsExisting() throws ConcurrentEditException, EditWithoutChangesException
	{
		testSaveNormal();
		assertTrue("Page does not exist", storage.exists("/Main"));
	}


	/**
	 * testExists:
	 * Tests the existence of an existing page
	 */
	@Test
	public void testExistsNonExisiting()
	{
		assertFalse("Page does exist", storage.exists("/Main"));
	}


	/**
	 * testTagCloudEntriesCount:
	 * Tests if the tag cloud has the correct numbers of entries
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testTagCloudEntriesCount() throws ConcurrentEditException, EditWithoutChangesException
	{
		MarkupPage page = createMarkupPage("/Main");
		storage.save(page);
		List<TagCloudEntry> tagCloudEntries = storage.getTagCloudEntries();
		assertEquals("Wrong count of entries", tags.length, tagCloudEntries.size());
	}


	/**
	 * getVersions:
	 * Tests whether all versions are correctly identified
	 */
	@Test
	public void testGetVersions()
	{}


	/**
	 * testPageLoadCurrent:
	 * Tests loading the current version of a page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testPageLoadCurrent() throws ConcurrentEditException, EditWithoutChangesException
	{
		String pagePath = "/Main";
		MarkupPage page = createMarkupPage(pagePath);

		assertTrue("save failed", storage.save(page));
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
		assertEquals("Tags.size not euqal", tags.length, loaded.getTags().size());
		for (int i = 0; i < tags.length; i++)
		{
			assertTrue(tags[i] + " not in Tags", loaded.getTags().contains(tags[i]));
		}
	}


	/**
	 * testPageLoadCurrent:
	 * Tests loading a previous version of a page
	 */
	@Test
	public void testPageLoadVersion()
	{}


	/**
	 * testPageToString:
	 * Tests transforming a page object into a string for storage
	 */
	@Test
	public void testPageToString()
	{
		MarkupPage page = createMarkupPage("/Main");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
		StringBuilder expected = new StringBuilder("pagePath=/Main");
		expected.append("\nauthor=" + author);
		expected.append("\neditor=null");
		expected.append("\ncreationDateFirst=" + sdf.format(date));
		expected.append("\ncreationDateThis=" + sdf.format(date));
		expected.append("\nmarkupLanguage=" + markupLanguage);
		expected.append("\nversion=0\ntags=");
		Set<String> sortedTags = new TreeSet<String>();
		for (int i = 0; i < tags.length; i++)
		{
			sortedTags.add(tags[i]);
		}
		expected.append(StringUtils.join(tags, ", "));
		expected.append("\n\n" + content);
		assertEquals("Output not equal!", expected.toString(), storage.pageToString(page));
	}


	/**
	 * testRename:
	 * Tests renaming a page
	 */
	@Test
	public void testRename()
	{}


	/**
	 * testSaveConcurrentEdit:
	 * Tests saving a page after concurrent edit
	 * 
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 */
	@Test(expected = ConcurrentEditException.class)
	public void testSaveConcurrentEdit() throws ConcurrentEditException, EditWithoutChangesException
	{
		MarkupPage page = createMarkupPage("/Main");

		storage.save(page);
		MarkupPage loaded = storage.load(page.getPagePath());
		loaded.setContent("newContent");
		storage.save(loaded);
		storage.save(loaded); // Code coverage FileStorage line 361 (Versions folder exists)
		loaded.setVersion(1); // Faking older version
		storage.save(loaded);

	}


	/**
	 * testSaveNoEdit:
	 * Tests saving a page without being edited
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test(expected = EditWithoutChangesException.class)
	public void testSaveNoEdit() throws EditWithoutChangesException, ConcurrentEditException
	{
		MarkupPage page = createMarkupPage("/Main");

		storage.save(page);
		MarkupPage loaded = storage.load(page.getPagePath());
		storage.save(loaded);

	}


	/**
	 * testSave:
	 * Tests saving a page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test
	public void testSaveNormal() throws ConcurrentEditException, EditWithoutChangesException
	{
		MarkupPage page = createMarkupPage("/Main");

		assertTrue("save failed", storage.save(page));
	}


	/**
	 * testSaveNull:
	 * Tests saving without a page
	 * 
	 * @throws EditWithoutChangesException
	 * @throws ConcurrentEditException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNull() throws ConcurrentEditException, EditWithoutChangesException
	{
		assertTrue("save failed", storage.save(null));
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
	 * createMarkupPage:
	 * Creates a MarkupPage object with given page path.
	 * 
	 * @param pagePath the page path to use
	 * @return the created object
	 */
	private MarkupPage createMarkupPage(String pagePath)
	{
		MarkupPage page = new MarkupPage();
		page.setPagePath(pagePath);
		page.setAuthor(author);
		page.setContent(content);
		page.setCurrentVersionCreationDate(date);
		page.setFirstVersionCreationDate(date);
		page.setMarkupLanguage(markupLanguage);
		for (int i = 0; i < tags.length; i++)
		{
			page.addTag(tags[i]);
		}

		return page;
	}
}
