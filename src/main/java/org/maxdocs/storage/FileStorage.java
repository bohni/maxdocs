package org.maxdocs.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.PageLight;
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.maxdocs.exceptions.PageAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileStorage
 * This storage persists the data in files on a hard disk. <br />
 * Files are saved with as <code>&lt;contentPath&gt;/&lt;number&gt;.txt</code> where number is an increasing
 * number.<br />
 * Old versions are saved with filename
 * <code>&lt;contentPath&gt;/&lt;versionFolder&gt;/&lt;number&gt;/&lt;version&gt;.txt</code>.
 * 
 * @author Team maxdocs.org
 */
public class FileStorage implements Storage
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEFAULT_VERSION_FOLDER = "versions";
	private static final String DEFAULT_CONTENT_PATH = "content" + FILE_SEPARATOR;
	private static Logger log = LoggerFactory.getLogger(FileStorage.class);
	private String contentPath;
	private String versionPath;
	private Map<String, String> files;
	private Map<String, List<String>> links2me;
	private Map<String, TagCloudEntry> tagMap;


	/**
	 * Default constructor.
	 * Creates a FileStorage object. <br />
	 * The contentPath is set to <code>content/</code>.<br />
	 * The versionPath is set to <code>content/versions/</code>.
	 */
	public FileStorage()
	{
		this(DEFAULT_CONTENT_PATH);
	}


	/**
	 * Minimal constructor. Contains required fields.
	 * Creates a FileStorage object with the given parameters. <br />
	 * The contentPath is set to <code>&lt;contentPath&gt;/</code>.<br />
	 * The versionPath is set to <code>&lt;contentPath&gt;/versions/</code>.
	 * 
	 * @param contentPath the path to the folder for storing the page source files.
	 */
	public FileStorage(String contentPath)
	{
		this(contentPath, DEFAULT_VERSION_FOLDER);
	}


	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link FileStorage} object with the given parameters. <br />
	 * The contentPath is set to <code>&lt;contentPath&gt;/</code>.<br />
	 * The versionPath is set to <code>&lt;contentPath&gt;/&lt;versionFolder&gt;/</code>.
	 * 
	 * @param contentPath the path to the folder for storing the page source files.
	 * @param versionFolder the name of the sub folder for storing old versions of the page source files.
	 */
	public FileStorage(String contentPath, String versionFolder)
	{
		log.trace("FileStorage({}, {})", contentPath, versionFolder);

		createContentPath(contentPath);

		createVersionPath(versionFolder);

		buildIndexes(false);
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String pagePath)
	{
		log.trace("delete({})", pagePath);

		boolean success = true;
		if (exists(pagePath))
		{
			String fileName = contentPath + files.get(pagePath);
			
			// delete current version
			File f = new File(fileName);
			if (f.exists())
			{
				success = f.delete();
				if (success)
				{
					log.debug("File '{}' deleted.", fileName);
					String pageNumber = StringUtils.substringBefore(files.get(pagePath), ".");
					files.remove(pagePath);
					updateTagMap(pagePath, "");
					// delete history
					String fileVersionPath = versionPath + "/" + pageNumber;
					f = new File(fileVersionPath);
					if (f.exists())
					{
						if (!deleteRecursive(f))
						{
							log.error(
								"File '{}' deleted, but version folder '{}' could not be deleted. Manual deletion necessary.",
								fileName, fileVersionPath);
						}
					}
				}
			}
		}
		return success;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		log.trace("exists({})", pagePath);

		boolean exists = false;
		if (files.containsKey(pagePath))
		{
			String pathname = contentPath + files.get(pagePath);
			File file = new File(pathname);
			if (file.exists())
			{
				exists = true;
			}
			else
			{
				// No longer available on HDD. Remove from mapping.
				files.remove(pagePath);
				updateTagMap(pagePath, "");
			}
		}
		return exists;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#getTagCloud()
	 */
	@Override
	public List<TagCloudEntry> getTagCloudEntries()
	{
		log.trace("getTagCloud()");
		List<TagCloudEntry> tagCloudEntries = Collections.synchronizedList(new ArrayList<TagCloudEntry>());
		tagCloudEntries.addAll(tagMap.values());
		Collections.sort(tagCloudEntries, new Comparator<TagCloudEntry>() {
			@Override
			public int compare(TagCloudEntry o1, TagCloudEntry o2)
			{
				return Integer.compare(o1.getCount(), o2.getCount());
			}
		});
		return tagCloudEntries;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#getVersions(java.lang.String)
	 */
	@Override
	public List<PageLight> getVersions(String pagePath)
	{
		log.trace("getVersions()");
		List<PageLight> list = new ArrayList<>();
		if(exists(pagePath))
		{
			MarkupPage page = load(pagePath);
			for (int i = page.getVersion(); i > 0; i--)
			{
				if(i == page.getVersion())
				{
					list.add(new PageLight(page));
				}
				else
				{
					list.add(new PageLight(load(pagePath, i)));
				}
			}
		}
		return list;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#load(java.lang.String)
	 */
	@Override
	public MarkupPage load(String pagePath)
	{
		log.trace("load({})", pagePath);
		String pathname = contentPath + files.get(pagePath);
		log.debug("pathname is {}", pathname);
		MarkupPage markupPage = null;
		try
		{
			InputStreamReader reader = new InputStreamReader(new FileInputStream(pathname), "UTF-8");
			markupPage = load(reader);
		}
		catch (UnsupportedEncodingException e)
		{
			log.error(e.getMessage(), e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e.getMessage(), e);
		}
		return markupPage;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#load(java.lang.String, int)
	 */
	@Override
	public MarkupPage load(String pagePath, int version)
	{
		log.trace("load({}, {})", pagePath, Integer.toString(version));
		// create pathname of version 
		StringBuffer pathname = new StringBuffer(contentPath);
		pathname.append(DEFAULT_VERSION_FOLDER);
		pathname.append(FILE_SEPARATOR);
		pathname.append(files.get(pagePath));
		pathname.insert(pathname.length() - 4, FILE_SEPARATOR + version);
		log.debug("pathname is {}", pathname);
		MarkupPage markupPage = null;
		markupPage = load(pathname.toString());
		return markupPage;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#rename(java.lang.String, org.maxdocs.data.MarkupPage)
	 */
	@Override
	public boolean rename(String oldPagePath, MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException, PageAlreadyExistsException
	{
		String newPagePath = newPage.getPagePath();
		log.trace("rename({}, {})", oldPagePath, newPagePath);
		
		if(files.containsKey(newPagePath))
		{
			throw new PageAlreadyExistsException("The page " + newPagePath + " already exists!");
		}
		
		MarkupPage page = load(oldPagePath);
		page.setPagePath(newPagePath);

		files.put(newPagePath, files.get(oldPagePath));
		
		boolean success = false;
		success = save(page);
		if (success)
		{
			files.remove(oldPagePath);
			updateTagMap(oldPagePath, "");
		}
		else
		{
			files.remove(newPagePath);
		}
		log.debug("renamed {} to {}", oldPagePath, newPagePath);
		return success;
	}



	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#save(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public boolean save(MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException
	{
		if (newPage == null)
		{
			throw new IllegalArgumentException("save(markupPage): markupPage is null!");
		}
		log.trace("save({})", newPage.getPagePath());

		boolean success = true;

		String filenameNew;
		MarkupPage oldPage = load(newPage.getPagePath());
		if (oldPage != null)
		{
			if (oldPage.getVersion() != newPage.getVersion())
			{
				throw new ConcurrentEditException();
			}

			if (StringUtils.equals(oldPage.getContent(), newPage.getContent())
				&& CollectionUtils.isEqualCollection(oldPage.getTags(), newPage.getTags())
				&& StringUtils.equals(oldPage.getPagePath(), newPage.getPagePath()))
			{
				throw new EditWithoutChangesException();
			}

			newPage.setAuthor(oldPage.getAuthor());
			newPage.setFirstVersionCreationDate(oldPage.getFirstVersionCreationDate());

			String pageNumber = StringUtils.substringBefore(files.get(oldPage.getPagePath()), ".");
			String path = versionPath + "/" + pageNumber;
			File folder = new File(path);
			if (!folder.exists())
			{
				folder.mkdirs();
			}
			success = writePage(oldPage, path + "/" + oldPage.getVersion() + ".txt");
			filenameNew = contentPath + files.get(oldPage.getPagePath());
		}
		else
		{
			int max = getNextPageNumber();
			filenameNew = contentPath + max + ".txt";
		}

		if (success) // saving old page
		{
			newPage.setVersion(newPage.getVersion() + 1);
			success = writePage(newPage, filenameNew);
			if (success) // saving new page
			{
				files.put(newPage.getPagePath(), StringUtils.substringAfterLast(filenameNew, contentPath));
			}
			else
			{
				// TODO: what must be done when the new page has not been saved
			}
		}
		else
		{
			// TODO: what must be done when the old page has not been saved
		}

		return success;
	}


	/**
	 * pageToString:
	 * Converts a MarkupPage object into a string
	 * 
	 * @param page the page to convert
	 * @return the page data as string
	 */
	protected String pageToString(MarkupPage page)
	{
		log.trace("pageToString({})", page.getPagePath());
		String lineSeperator = "\n";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
		StringBuilder content = new StringBuilder();
		content.append("pagePath=" + page.getPagePath() + lineSeperator);
		content.append("author=" + page.getAuthor() + lineSeperator);
		content.append("editor=" + page.getEditor() + lineSeperator);
		content.append("creationDateFirst=" + sdf.format(page.getFirstVersionCreationDate()) + lineSeperator);
		content
			.append("creationDateThis=" + sdf.format(page.getCurrentVersionCreationDate()) + lineSeperator);
		content.append("markupLanguage=" + page.getMarkupLanguage() + lineSeperator);
		content.append("version=" + page.getVersion() + lineSeperator);
		content.append("tags=" + page.getTagsAsString() + lineSeperator);
		updateTagMap(page.getPagePath(), page.getTagsAsString());
		content.append(lineSeperator);
		content.append(page.getContent());
		return content.toString();
	}


	/**
	 * createContentPath:
	 * Creates the folder to store current versions.
	 * 
	 * @param contentPath the path to the folder for storing the page source files.
	 */
	private void createContentPath(String contentPath)
	{
		this.contentPath = contentPath;

		if (StringUtils.isBlank(this.contentPath))
		{
			this.contentPath = DEFAULT_CONTENT_PATH;
			log.warn("Parameter contentPath is not set. Using default value '{}'", this.contentPath);
		}

		if (!StringUtils.endsWith(this.contentPath, FILE_SEPARATOR))
		{
			this.contentPath = this.contentPath + FILE_SEPARATOR;
		}

		File storage = new File(this.contentPath);
		log.info("Using content folder '{}'", storage.getAbsolutePath());
		createDir(storage);
	}


	/**
	 * createVersionPath:
	 * Creates the folder to store old versions.
	 * 
	 * @param versionFolder the name of the sub folder for storing old versions of the page source files.
	 */
	private void createVersionPath(String versionFolder)
	{
		String folder = versionFolder;
		if (StringUtils.isBlank(folder))
		{
			folder = DEFAULT_VERSION_FOLDER;
			log.warn("Parameter versionFolder is not set. Using default value '{}'", folder);
		}

		if (StringUtils.startsWith(folder, FILE_SEPARATOR))
		{
			this.versionPath = this.contentPath + folder.substring(1);
		}
		else
		{
			this.versionPath = this.contentPath + folder;
		}
		if (!StringUtils.endsWith(this.versionPath, FILE_SEPARATOR))
		{
			this.versionPath = this.versionPath + FILE_SEPARATOR;
		}

		File versions = new File(this.versionPath);
		log.info("Using versions folder '{}'", versions.getAbsolutePath());
		createDir(versions);
	}


	/**
	 * createDir:
	 * Creates the given directory, including any necessary but nonexistent parent directories.
	 * 
	 * @param directory the directory to create.
	 */
	private void createDir(File directory)
	{
		if (!directory.exists())
		{
			if (directory.mkdirs())
			{
				log.debug("Created folder '{}'", directory.getAbsolutePath());
			}
			else
			{
				throw new RuntimeException("Error creating folder '" + directory.getAbsolutePath() + "'."); // TODO: checked exceptions?
			}
		}
	}


	/**
	 * buildIndexes:
	 * (Re-)builds the indexes:
	 * <ul>
	 * <li>files - stores to a page path the filename on disk</li>
	 * <li>link2me - stores to a page all pages that link to it</li>
	 * <li>tagMap - stores to each tag its count</li>
	 * </ul>
	 * The indexes are cached on disk. If the cached indexes are not up to date, the parameter
	 * rebuild could be used to force a rebuild.
	 * 
	 * @param rebuild if set to <code>true</code>, the indexes are build from scratch.
	 */
	public void buildIndexes(boolean rebuild)
	{
		// TODO:
		// Indexes must be cached on disk. How?
		// Read from disk or build them
		// Possibility to force rebuild of indexes (after crash, bug ...).
		
		if(!rebuild)
		{
			// TODO: Load indexes from disk
		   if(false) // indexes exists
		   {
			   //load indexes
				files = new HashMap<String, String>();
				links2me = new HashMap<String, List<String>>();
				tagMap = new HashMap<String, TagCloudEntry>();
		   }
		   else
		   {
			   rebuild = true;
		   }
		}

		if(rebuild)
		{
			File storage = new File(contentPath);
			files = new HashMap<String, String>();
			links2me = new HashMap<String, List<String>>();
			tagMap = new HashMap<String, TagCloudEntry>();
			for (File child : storage.listFiles())
			{
				if (child.isDirectory())
				{
					continue; // Ignore all directories
				}
				Scanner scanner = null;
				try
				{
					scanner = new Scanner(new FileInputStream(child), "UTF-8");
					String line = null;
					String pagePath = null;
					String tagsString = null;
					while (scanner.hasNextLine())
					{
						line = scanner.nextLine();
						if (StringUtils.startsWith(line, "pagePath"))
						{
							pagePath = StringUtils.substringAfterLast(line, "=");
							files.put(pagePath, child.getName());
						}
						if (StringUtils.startsWith(line, "tags"))
						{
							tagsString = StringUtils.substringAfterLast(line, "=");
						}
					}
					if (tagsString != null)
					{
						updateTagMap(pagePath, tagsString);
					}
				}
				catch (FileNotFoundException e)
				{
					log.error("Error while reading files into internal map.", e);
				}
				finally
				{
					if (scanner != null)
					{
						scanner.close();
					}
				}
			}
			
			// TODO: save indexes to disk
		}
	}


	/**
	 * updateTagMap:
	 * All references from the given page to its former tags are deleted,
	 * if a tag has no more references, the tag is also deleted.
	 * Then the references are build again with the given new tags.
	 * 
	 * @param pagePath the pagePath to add references to tags
	 * @param tagsString the tags to reference to
	 */
	private void updateTagMap(String pagePath, String tagsString)
	{
		// First all tags of the page are deleted
		List<String> tagsToRemove = new ArrayList<String>();
		for (String tag : tagMap.keySet())
		{
			TagCloudEntry tagCloudEntry = tagMap.get(tag);
			if (tagCloudEntry.getPages().contains(pagePath))
			{
				tagCloudEntry.getPages().remove(pagePath);
				if (tagCloudEntry.getCount() == 0)
				{
					tagsToRemove.add(tag);
				}
			}
		}
		for (String tag : tagsToRemove)
		{
			tagMap.remove(tag);
		}
		// Then all new Tags are added
		List<String> newtags = new ArrayList<String>();
		CollectionUtils.addAll(newtags, StringUtils.splitByWholeSeparator(tagsString, ", "));
		for (String tag : newtags)
		{
			if (StringUtils.isNotBlank(tag))
			{
				if (tagMap.get(tag) == null)
				{
					TagCloudEntry entry = new TagCloudEntry(tag);
					tagMap.put(tag, entry);
				}
				TagCloudEntry entry = tagMap.get(tag);
				if (pagePath != null && !entry.getPages().contains(pagePath))
				{
					entry.addPage(pagePath);
				}
				tagMap.put(tag, entry);
			}
		}
	}


	/**
	 * deleteRecursive:
	 * Deletes the file or directory denoted by this abstract pathname.
	 * Works recursively to delete non empty directories as well.
	 * 
	 * @param path file or directory to be deleted
	 * @return <code>true</code> if and only if the file or directory is successfully deleted;
	 *         <code>false</code> otherwise
	 */
	private boolean deleteRecursive(File path)
	{
		boolean success = true;
		if (!path.exists())
		{
			return success;
		}
		if (path.isDirectory())
		{
			for (File f : path.listFiles())
			{
				success = success && deleteRecursive(f);
			}
		}
		return success && path.delete();

	}


	/**
	 * getNextPageNumber:
	 * Return the next free number for saving a page.
	 * 
	 * @return the next free number
	 */
	private synchronized int getNextPageNumber()
	{
		log.trace("getNextPageNumber()");
		int max = 0;
		for (String key : files.keySet())
		{
			int number = Integer.parseInt(StringUtils.substringBeforeLast(files.get(key), "."));
			if (number > max)
			{
				max = number;
			}
		}
		max++;
		log.debug("nextPageNumber: {}", max);
		return max;
	}


	MarkupPage load(Reader reader)
	{
		MarkupPage markupPage = new MarkupPage();
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(reader);
			String line;
			StringBuilder content = new StringBuilder();
			int count = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				if (StringUtils.startsWith(line, "author"))
				{
					markupPage.setAuthor(StringUtils.substringAfterLast(line, "="));
				}
				else if (StringUtils.startsWith(line, "markupLanguage"))
				{
					markupPage.setMarkupLanguage(StringUtils.substringAfterLast(line, "="));
				}
				else if (StringUtils.startsWith(line, "creationDateFirst"))
				{
					Date date = sdf.parse(StringUtils.substringAfterLast(line, "="));
					markupPage.setFirstVersionCreationDate(date);
				}
				else if (StringUtils.startsWith(line, "creationDateThis"))
				{
					Date date = sdf.parse(StringUtils.substringAfterLast(line, "="));
					markupPage.setCurrentVersionCreationDate(date);
				}
				else if (StringUtils.startsWith(line, "editor"))
				{
					markupPage.setEditor(StringUtils.substringAfterLast(line, "="));
				}
				else if (StringUtils.startsWith(line, "pagePath"))
				{
					markupPage.setPagePath(StringUtils.substringAfterLast(line, "="));
				}
				else if (StringUtils.startsWith(line, "version"))
				{
					String version = StringUtils.substringAfterLast(line, "=").trim();
					markupPage.setVersion(Integer.parseInt(version));
				}
				else if (StringUtils.startsWith(line, "tags"))
				{
					String tags = StringUtils.substringAfterLast(line, "=");
					Set<String> taglist = Collections.synchronizedSet(new HashSet<String>());
					String[] stringarr = StringUtils.splitByWholeSeparator(tags, ", ");
					CollectionUtils.addAll(taglist, stringarr);
					markupPage.setTags(taglist);
				}
				else if (StringUtils.isBlank(line) && count == 0)
				{
					count = 1;
				}
				else if (count > 0)
				{
					content.append(line + LINE_SEPARATOR);
				}
			}
			markupPage.setContent(content.toString());
			log.debug("page {}, Version {} loaded", markupPage.getPagePath(), Integer.toString(markupPage.getVersion()));
		}
		catch (ParseException e)
		{
			log.error(e.getMessage(), e);
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
		return markupPage;
	}


	/**
	 * writePage:
	 * Saves the given markup page to a file with the given filename.
	 * 
	 * @param page the markup page to save
	 * @param filename the filename
	 * @return <code>true</code>, if and only if the page is successfully saved; <code>false</code> otherwise
	 */
	private boolean writePage(MarkupPage page, String filename)
	{
		log.trace("writePage({}, {})", page.getPagePath(), filename);
		boolean success = false;
		Writer writer = null;
		try
		{
			writer = new OutputStreamWriter(new FileOutputStream(new File(filename)), "UTF-8");
			writer.append(pageToString(page));
			writer.close();
			success = true;
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			success = false;
			throw new RuntimeException("Error while saving file " + filename, e); //TODO: checked exceptions?
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}
			}
		}
		log.debug("writePage: page {} saved as {}", page.getPagePath(), filename);
		return success;
	}
	
	/**
	 * addReferer:
	 * Callback for adding referers to a page
	 * 
	 * @param pagePath path of the page
	 * @param refererPagePath path of the referer
	 */
	public void addReferer(String pagePath, String refererPagePath)
	{
		List<String> referers = links2me.get(pagePath);
		if(referers == null)
		{
			referers = new ArrayList<>();
		}
		if(!referers.contains(refererPagePath))
		{
			referers.add(refererPagePath);
		}
		links2me.put(pagePath, referers);
	}
}
