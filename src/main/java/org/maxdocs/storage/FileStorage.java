package org.maxdocs.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Scanner;

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
		log.debug("Using content dir {}", file.getAbsolutePath());

		if (!file.exists())
		{
			log.info("Content dir does not exist. Creating {}", file.getAbsolutePath());
			file.mkdirs();
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
			try
			{
				Scanner scanner = new Scanner(new FileInputStream(file), "UTF-8");
				String nl = System.getProperty("line.separator");
				String line;
				StringBuilder content = new StringBuilder();
				int count = 0;
				while (scanner.hasNextLine())
				{
					line = scanner.nextLine() + nl;
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
						markupPage.setAuthor(StringUtils.substringAfterLast(line, "="));
					}
					else if (StringUtils.isBlank(line) && count == 0)
					{
						count = 1;
					}
					else if (count > 0)
					{
						content.append(line);
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
	public boolean save(MarkupPage page)
	{
		// TODO Auto-generated method stub
		return false;
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
