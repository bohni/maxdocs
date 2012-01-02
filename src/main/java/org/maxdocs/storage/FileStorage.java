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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.data.MarkupPage;
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
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static final String VERSION_PATH = "versions";
	private static Logger log = LoggerFactory.getLogger(FileStorage.class);
	private String storagePath;
	private Map<String, String> files;
	/**
	 * Default constructor.
	 * Creates a FileStorage object.
	 * 
	 * The storagePath is set to "content/".
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
	 * @param storagePath
	 */
	public FileStorage(String storagePath)
	{
		log.trace("FileStorage({})", storagePath);

		if (StringUtils.endsWith(storagePath, "/"))
		{
			this.storagePath = storagePath;
		}
		else
		{
			this.storagePath = storagePath + "/";
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

		File versions = new File(this.storagePath + VERSION_PATH);
		log.debug("Using content folder '{}'", storage.getAbsolutePath());
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
		files = new HashMap<String, String>();
		for (File child : storage.listFiles())
		{
			if (child.getName().equals(".") || child.getName().equals("..") || child.isDirectory())
			{
				continue; // Ignore self and parent aliases
			}
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(new FileInputStream(child), "UTF-8");
				String line;
				while (scanner.hasNextLine())
				{
					line = scanner.nextLine();
					if (StringUtils.startsWith(line, "pagePath"))
					{
						files.put(StringUtils.substringAfterLast(line, "="), child.getName());
						break;
					}
				}
			}
			catch (FileNotFoundException e)
			{
				log.error("Error while reading files in internal map.", e);
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


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		return files.containsKey(pagePath);
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#load(java.lang.String)
	 */
	@Override
	public MarkupPage load(String pagePath)
	{
		String pathname = storagePath + files.get(pagePath);
		MarkupPage markupPage = new MarkupPage();
		File file = new File(pathname);
		if (file.exists())
		{
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
						markupPage.setContentType(StringUtils.substringAfterLast(line, "="));
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
						StringTokenizer st = new StringTokenizer(StringUtils.substringAfterLast(line, "="),
							",");
						while (st.hasMoreElements())
						{
							String tag = st.nextToken().trim();
							if (StringUtils.isNotBlank(tag))
							{
								markupPage.addTag(tag);
							}
						}
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
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#save(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public boolean save(MarkupPage oldPage, MarkupPage newPage)
	{
		if(oldPage == null)
		{
			throw new IllegalArgumentException("save(oldPage, newPage): oldPage is null!"); //TODO: checked exceptions?
		}
		if(newPage == null)
		{
			throw new IllegalArgumentException("save(oldPage, newPage): newPage is null!"); //TODO: checked exceptions?
		}
		log.trace("save({}, {})", oldPage.getPagePath(), newPage.getPagePath());
		boolean success = false;
		String lineSeperator = "\n";
		Writer writer = null;
		try
		{
			StringBuilder tags;
			
			String pageNumber = StringUtils.substringBefore(files.get(oldPage.getPagePath()), ".");
			String path = storagePath + VERSION_PATH + "/" + pageNumber;
			File folder = new File(path);
			if(!folder.exists())
			{
				folder.mkdirs();
			}
			// oldPage
			writer = new OutputStreamWriter(new FileOutputStream(new File(path + "/" + oldPage.getVersion() + ".txt"), false), "UTF-8");
			writer.append("pagePath=" + oldPage.getPagePath() + lineSeperator);
			writer.append("author=" + oldPage.getAuthor() + lineSeperator);
			writer.append("editor=" + oldPage.getEditor() + lineSeperator);
			writer.append("creationDateFirst=" + sdf.format(oldPage.getFirstVersionCreationDate())
				+ lineSeperator);
			writer.append("creationDateThis=" + sdf.format(oldPage.getCurrentVersionCreationDate())
				+ lineSeperator);
			writer.append("contentType=" + oldPage.getContentType() + lineSeperator);
			writer.append("version=" + (oldPage.getVersion()) + lineSeperator);
			tags = new StringBuilder();
			for (String tag : oldPage.getTags())
			{
				tags.append(tag + ", ");
			}
			writer.append("tags=" + StringUtils.substringBeforeLast(tags.toString(), ",") + lineSeperator);
			writer.append(lineSeperator);
			writer.append(oldPage.getContent());
			
			writer.close();

			// newPage
			writer = new OutputStreamWriter(new FileOutputStream(new File(storagePath
				+ files.get(newPage.getPagePath()))), "UTF-8");
			writer.append("pagePath=" + newPage.getPagePath() + lineSeperator);
			writer.append("author=" + newPage.getAuthor() + lineSeperator);
			writer.append("editor=" + newPage.getEditor() + lineSeperator);
			writer.append("creationDateFirst=" + sdf.format(newPage.getFirstVersionCreationDate())
				+ lineSeperator);
			writer.append("creationDateThis=" + sdf.format(newPage.getCurrentVersionCreationDate())
				+ lineSeperator);
			writer.append("contentType=" + newPage.getContentType() + lineSeperator);
			writer.append("version=" + (newPage.getVersion()) + lineSeperator);
			tags = new StringBuilder();
			for (String tag : newPage.getTags())
			{
				tags.append(tag + ", ");
			}
			writer.append("tags=" + StringUtils.substringBeforeLast(tags.toString(), ",") + lineSeperator);
			writer.append(lineSeperator);
			writer.append(newPage.getContent());

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


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#save(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public boolean save(MarkupPage newPage)
	{
		if(newPage == null)
		{
			throw new IllegalArgumentException("save(newPage): newPage is null!"); //TODO: checked exceptions?
		}
		log.trace("save({})", newPage.getPagePath());
		boolean success = false;
		String lineSeperator = "\n";
		Writer writer = null;
		if(files.containsKey(newPage.getPagePath()))
		{
			// Error: Page is not new
			throw new RuntimeException("newPage already exists!"); //TODO: checked exceptions?
		}
		else
		{
			try
			{
				// TODO: lfdNr ermitteln zum Speichern
				int max = 0;
				for (String key : files.keySet())
				{
					int number = Integer.parseInt(StringUtils.substringBeforeLast(files.get(key), "."));
					if(number > max)
					{
						max = number;
					}
				}
				max++;
				String filename = storagePath + max + ".txt";
				writer = new OutputStreamWriter(new FileOutputStream(new File(filename)), "UTF-8");
				writer.append("pagePath=" + newPage.getPagePath() + lineSeperator);
				writer.append("author=" + newPage.getAuthor() + lineSeperator);
				writer.append("editor=" + newPage.getEditor() + lineSeperator);
				writer.append("creationDateFirst=" + sdf.format(newPage.getFirstVersionCreationDate())
					+ lineSeperator);
				writer.append("creationDateThis=" + sdf.format(newPage.getCurrentVersionCreationDate())
					+ lineSeperator);
				writer.append("contentType=" + newPage.getContentType() + lineSeperator);
				writer.append("version=" + (newPage.getVersion()) + lineSeperator);
				StringBuilder tags = new StringBuilder();
				for (String tag : newPage.getTags())
				{
					tags.append(tag + ", ");
				}
				writer.append("tags=" + StringUtils.substringBeforeLast(tags.toString(), ",") + lineSeperator);
				writer.append(lineSeperator);
				writer.append(newPage.getContent());
				
				files.put(newPage.getPagePath(), max + ".txt");

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
		}
		return success;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String pagePath)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
