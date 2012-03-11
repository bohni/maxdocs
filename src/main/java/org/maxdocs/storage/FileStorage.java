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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileStorage
 * This storage persists the data in files on a hard disk.
 * <br />
 * Files are saved with as &lt;number&gt;.txt where number is an increasing number.<br />
 * Old versions are saved with filename &lt;storagePath&gt;/versions/&lt;number&gt;/&lt;version&gt;.txt.
 * 
 * @author Team maxdocs.org
 *
 */
public class FileStorage implements Storage
{
	private static Logger log = LoggerFactory.getLogger(FileStorage.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
	private Map<String, String> files;
	private Map<String, List<String>> links2me;
	private String storagePath;
	private Map<String, TagCloudEntry> tagMap;
	private String versionPath;
	/**
	 * Default constructor.
	 * Creates a FileStorage object.
	 * 
	 * The storagePath is set to "content/".
	 * The versionsFolder is set to "content/versions/".
	 *
	 */
	public FileStorage()
	{
		this("content/");
	}


	/**
	 * Minimal constructor. Contains required fields.
	 * Creates a FileStorage object with the given parameters.
	 * 
	 * The versionsFolder is set to "&lt;storagePath&gt;/versions/".
	 *
	 * @param storagePath the path to the folder for storing the page source files.
	 */
	public FileStorage(String storagePath)
	{
		this("content/", "versions/");
	}


	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link FileStorage} object with the given parameters.
	 *
	 * The versionsFolder is set to "&lt;storagePath&gt;/&lt;versionsFolder&gt;/".
	 * 
	 * @param storagePath the path to the folder for storing the page source files.
	 * @param versionsFolder the name of the sub folder for storing old versions of the page source files.
	 */
	public FileStorage(String storagePath, String versionsFolder)
	{
		log.trace("FileStorage({}, {})", storagePath, versionsFolder);

		String fileSeparator = System.getProperty("file.separator");

		if (StringUtils.isBlank(storagePath))
		{
			log.warn("Parameter storagePath is not set. Using default value 'content/'");
			this.storagePath = "content/";
		}

		if (StringUtils.endsWith(storagePath, fileSeparator))
		{
			this.storagePath = storagePath;
		}
		else
		{
			this.storagePath = storagePath + fileSeparator;
		}

		File storage = new File(this.storagePath);
		log.debug("Using content folder '{}'", storage.getAbsolutePath());

		if (!storage.exists())
		{
			if (storage.mkdirs())
			{
				log.info("Created content folder '{}'", storage.getAbsolutePath());
			}
			else
			{
				throw new RuntimeException("Error creating content folder.");
			}
		}
		if (StringUtils.startsWith(versionsFolder, fileSeparator))
		{
			this.versionPath = this.storagePath + versionsFolder.substring(1);
		}
		else
		{
			this.versionPath = this.storagePath + versionsFolder;
		}
		if (!StringUtils.endsWith(this.versionPath, fileSeparator))
		{
			this.versionPath = this.versionPath + fileSeparator;
		}

		File versions = new File(this.versionPath);
		log.debug("Using versions folder '{}'", versions.getAbsolutePath());
		if (!versions.exists())
		{
			if (versions.mkdirs())
			{
				log.info("Created versions folder '{}'", versions.getAbsolutePath());
			}
			else
			{
				throw new RuntimeException("Error creating versions folder.");
			}
		}
		buildIndexes();
	}


	/**
	 * buildIndexes:
	 * (Re-)builds the indexes:
	 * <ul>
	 * <li>files - stores to a page path the filename on disk</li>
	 * <li>link2me - stores to a page all pages that link to it</li>
	 * <li>tagMap - stores to each tag its count</li>
	 * </ul>
	 *
	 * @param storage
	 */
	private void buildIndexes()
	{
		File storage = new File(storagePath);
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
						files.put(StringUtils.substringAfterLast(line, "="), child.getName());
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
	}


	/**
	 * updateTagMap:
	 * TODO: Documentation
	 *
	 * @param pagePath
	 * @param tagsString
	 */
	private void updateTagMap(String pagePath, String tagsString)
	{
		// First all tags of the page are deleted
		List<String> tagsToRemove = new ArrayList<String>();
		for (String tag : tagMap.keySet())
		{
			TagCloudEntry tagCloudEntry = tagMap.get(tag);
			if(tagCloudEntry.getPages().contains(pagePath))
			{
				tagCloudEntry.getPages().remove(pagePath);
				if(tagCloudEntry.getCount() == 0)
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


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String pagePath)
	{
		log.trace("delete()");

		boolean success = true;
		if (exists(pagePath))
		{
			String fileName = storagePath + files.get(pagePath);

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
					// delete history
					String fileVersionPath = versionPath + "/" + pageNumber;
					f = new File(fileVersionPath);
					if (f.exists())
					{
						if (!deleteRecursive(f))
						{
							log.error("File '{}' deleted, but version folder '{}' could not be deleted. Manual deletion necessary.", fileName, fileVersionPath);
						}
					}
				}
			}
		}
		return success;
	}


	/**
	 * deleteRecursive:
	 * Deletes the file or directory denoted by this abstract pathname.
	 * Works recursively to delete non empty directories as well.
	 * @param path file or directory to be deleted
	 * @return <code>true</code> if and only if the file or directory is successfully deleted; <code>false</code> otherwise
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


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		return files.containsKey(pagePath);
	}


	/**
	 * getNextPageNumber:
	 * Return the next free number for saving a page.
	 *
	 * @return the next free number
	 */
	private synchronized int getNextPageNumber()
	{
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
		return max;
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
	 * @see org.maxdocs.storage.Storage#load(java.lang.String)
	 */
	@Override
	public MarkupPage load(String pagePath)
	{
		log.trace("load({})", pagePath);
		String pathname = storagePath + files.get(pagePath);
		MarkupPage markupPage = null;
		File file = new File(pathname);
		if (file.exists())
		{
			markupPage = new MarkupPage();
			markupPage.setPagePath(pagePath);
			markupPage.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(new FileInputStream(file), "UTF-8");
				String line;
				StringBuilder content = new StringBuilder();
				int count = 0;
				while (scanner.hasNextLine())
				{
					line = scanner.nextLine();
					if (StringUtils.startsWith(line, "author"))
					{
						markupPage.setAuthor(StringUtils.substringAfterLast(line, "="));
					}
					else if (StringUtils.startsWith(line, "contentType"))
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
					else if (StringUtils.startsWith(line, "version"))
					{
						String version = StringUtils.substringAfterLast(line, "=").trim();
						markupPage.setVersion(Integer.parseInt(version));
					}
					else if (StringUtils.startsWith(line, "tags"))
					{
						String tags = StringUtils.substringAfterLast(line, "=");
						Set<String> taglist = Collections.synchronizedSet(new HashSet<String>());
						String[] stringarr =StringUtils.splitByWholeSeparator(tags, ", ");
						CollectionUtils.addAll(taglist, stringarr);
						markupPage.setTags(taglist);
					}
					else if (StringUtils.isBlank(line) && count == 0)
					{
						count = 1;
					}
					else if (count > 0)
					{
						content.append(line + System.getProperty("line.separator"));
					}
				}
				markupPage.setContent(content.toString());
			}
			catch (FileNotFoundException e)
			{
				log.error(e.getMessage(), e);
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
		}
		return markupPage;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#load(java.lang.String, int)
	 */
	@Override
	public MarkupPage load(String pagePath, int version)
	{
		log.trace("load({}, {})", pagePath, version);
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#rename(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean rename(String pagePath, String newPagePath) throws ConcurrentEditException
	{
		MarkupPage page = load(pagePath);
		page.setPagePath(newPagePath);
		page.setPageName(StringUtils.substringAfterLast(pagePath, "/"));
		boolean success = false; 
		try
		{
			success = save(page);
			if(success)
			{
				files.put(newPagePath, files.get(pagePath));
				files.remove(pagePath);
			}
		}
		catch (EditWithoutChangesException e)
		{
			log.error("This exception should never been thrown, because rename does a change.");
		}
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
			filenameNew = storagePath + files.get(oldPage.getPagePath());
		}
		else
		{
			int max = getNextPageNumber();
			filenameNew = storagePath + max + ".txt";
		}

		if (success)
		{
			newPage.setVersion(newPage.getVersion() + 1);
			success = writePage(newPage, filenameNew);
			if (success)
			{
				files.put(newPage.getPagePath(), StringUtils.substringAfterLast(filenameNew, storagePath));
			}
		}

		return success;
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
		boolean success = false;
		String lineSeperator = "\n";
		Writer writer = null;
		try
		{
			writer = new OutputStreamWriter(new FileOutputStream(new File(filename)), "UTF-8");
			writer.append("pagePath=" + page.getPagePath() + lineSeperator);
			writer.append("author=" + page.getAuthor() + lineSeperator);
			writer.append("editor=" + page.getEditor() + lineSeperator);
			writer.append("creationDateFirst=" + sdf.format(page.getFirstVersionCreationDate())
				+ lineSeperator);
			writer.append("creationDateThis=" + sdf.format(page.getCurrentVersionCreationDate())
				+ lineSeperator);
			writer.append("contentType=" + page.getMarkupLanguage() + lineSeperator);
			writer.append("version=" + page.getVersion() + lineSeperator);
			writer.append("tags=" + page.getTagsAsString()  + lineSeperator);
			updateTagMap(page.getPagePath(), page.getTagsAsString());
			writer.append(lineSeperator);
			writer.append(page.getContent());

			writer.close();
			success = true;
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			success = false;
			throw new RuntimeException("Error while saving files!"); //TODO: checked exceptions?
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
		return success;
	}
}