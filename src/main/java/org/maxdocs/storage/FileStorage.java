package org.maxdocs.storage;

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
		this.storagePath = storagePath; 
	}

	/* (non-Javadoc)
	 * @see org.maxdocs.storage.Storage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String pagePath)
	{
		// TODO Auto-generated method stub
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
	// private static Logger log = LoggerFactory.getLogger(FileStorage.class);

	/**
	 * getStoragePath: Returns the storagePath.
	 * 
	 * @return the storagePath
	 */
	public String getStoragePath()
	{
		return storagePath;
	}

	/**
	 * setStoragePath: Sets the storagePath.
	 * 
	 * @param storagePath the storagePath to set
	 */
	public void setStoragePath(String storagePath)
	{
		this.storagePath = storagePath; 
	}
}
