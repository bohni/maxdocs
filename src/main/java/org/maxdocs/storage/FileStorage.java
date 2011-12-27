package org.maxdocs.storage;

import java.io.File;

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
		if(StringUtils.endsWith(storagePath, "/"))
		{
			this.storagePath = storagePath; 
		}
		else
		{
			this.storagePath = storagePath + "/";
		}
		File file = new File(this.storagePath);
		if(!file.exists())
		{
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
		if(pagePath.startsWith("/"))
		{
			file = new File(storagePath + pagePath.substring(1) + ".txt");
		}
		else
		{
			file = new File(storagePath + pagePath + ".txt");
		}
		if(file.exists())
		{
			if(file.isFile())
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
		// TODO Auto-generated method stub
		return null;
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
