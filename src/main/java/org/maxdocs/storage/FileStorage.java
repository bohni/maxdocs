package org.maxdocs.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.maxdocs.data.MarkupPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileStorage
 * TODO, 23.12.2011: Documentation
 * 
 * @author Stefan Bohn
 *
 */
public class FileStorage implements Storage
{
	private static Logger log = LoggerFactory.getLogger(FileStorage.class);
	private String storagePath;


	/**
	 * Default constructor.
	 * Creates a FileStorage object.
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
		File file = new File(this.storagePath);
		log.debug("Using content folder '{}'", file.getAbsolutePath());

		if (!file.exists())
		{
			if (file.mkdirs())
			{
				log.info("Created content folder '{}'", file.getAbsolutePath());
			}
			else
			{
				throw new RuntimeException("Error creating content folder.");
			}
		}
		File versions = new File(this.storagePath + "VERSIONS");
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
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		File file;
		if (pagePath.startsWith("/"))
		{
			file = new File(storagePath + pagePath.substring(1) + ".txt");
		}
		else
		{
			file = new File(storagePath + pagePath + ".txt");
		}
		if (file.exists())
		{
			if (file.isFile())
			{
				return true;
			}
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#load(java.lang.String)
	 */
	@Override
	public MarkupPage load(String pagePath)
	{
		String pathname = storagePath + pagePath.substring(1) + ".txt";
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
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
						Date date = sdf.parse(StringUtils.substringAfterLast(line, "="));
						markupPage.setFirstVersionCreationDate(date);
					}
					else if (StringUtils.startsWith(line, "creationDateThis"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
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
						StringTokenizer st = new StringTokenizer(StringUtils.substringAfterLast(line, "="), ",");
						while (st.hasMoreElements())
						{
							String tag = st.nextToken().trim();
							if(StringUtils.isNotBlank(tag))
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
	public boolean save(MarkupPage page, boolean isNew)
	{
		log.trace("save()");
		boolean success = false;
		String lineSeperator = "\n";
		Writer writer = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
			if (isNew)
			{
				writer = new OutputStreamWriter(new FileOutputStream(new File(storagePath + page.getPagePath().substring(1) + ".txt")), "UTF-8");
				writer = new FileWriter(new File(storagePath + page.getPagePath().substring(1) + ".txt"), false);
				writer.append("pagePath=" + page.getPagePath() + lineSeperator);
				writer.append("author=" + page.getAuthor() + lineSeperator);
				writer.append("editor=" + page.getEditor() + lineSeperator);
				writer.append("creationDateFirst=" + sdf.format(page.getFirstVersionCreationDate())+ lineSeperator);
				writer.append("creationDateThis=" + sdf.format(page.getCurrentVersionCreationDate())+ lineSeperator);
				writer.append("contentType=" + page.getContentType() + lineSeperator);
				writer.append("version=" + (page.getVersion() + 1) + lineSeperator);
				StringBuilder tags = new StringBuilder();
				for (String tag : page.getTags())
				{
					tags.append(tag + ", ");
				}
				writer.append("tags=" + StringUtils.substringBeforeLast(tags.toString(), ",") + lineSeperator);
				writer.append(lineSeperator);
				String content = new String(page.getContent().getBytes(Charset.forName("UTF-8")));
				writer.append(content);
			}
			else
			{
				// Versionierung
				writer = new OutputStreamWriter(new FileOutputStream(new File(storagePath + "VERSIONS" + page.getPagePath() + "."
					+ page.getVersion() + ".txt"), false), "UTF-8");
				writer.append("pagePath=" + page.getPagePath() + lineSeperator);
				writer.append("author=" + page.getAuthor() + lineSeperator);
				writer.append("editor=" + page.getEditor() + lineSeperator);
				writer.append("creationDateFirst=" + sdf.format(page.getFirstVersionCreationDate())+ lineSeperator);
				writer.append("creationDateThis=" + sdf.format(page.getCurrentVersionCreationDate())+ lineSeperator);
				writer.append("contentType=" + page.getContentType() + lineSeperator);
				writer.append("version=" + (page.getVersion()) + lineSeperator);
				StringBuilder tags = new StringBuilder();
				for (String tag : page.getTags())
				{
					tags.append(tag + ", ");
				}
				writer.append("tags=" + StringUtils.substringBeforeLast(tags.toString(), ",") + lineSeperator);
				writer.append(lineSeperator);
				String content = new String(page.getContent().getBytes(Charset.forName("UTF-8")));
				writer.append(content);
			}
			success = true;
		}
		catch (IOException e)
		{
			success = false;
			log.error(e.getMessage(), e);
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
	 * @see org.maxdocs.storage.Storage#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String pagePath)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
