package org.maxdocs.storage;

import java.util.List;

import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.PageLight;
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.maxdocs.exceptions.PageAlreadyExistsException;

/**
 * Storage:
 * Interface for storage providers of MaxDocs.
 * 
 * @author Team maxdocs.org
 */
public interface Storage
{
	/**
	 * delete:
	 * Deletes the requested page.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if and only if the page is successfully deleted; <code>false</code> otherwise
	 */
	boolean delete(String pagePath);


	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if the pagePath exists; <code>false</code> otherwise
	 */
	boolean exists(String pagePath);


	/**
	 * getTagCloud:
	 * Returns a List with tags cloud entries.
	 * 
	 * @return a list with tag cloud entries
	 */
	List<TagCloudEntry> getTagCloudEntries();


	/**
	 * getVersions:
	 * Returns a list of PageLight-Objects that contains all versions of the given pagePath
	 *
	 * @param pagePath
	 * @return list of PageLight-Objects 
	 */
	List<PageLight> getVersions(String pagePath);


	/**
	 * load:
	 * Creates a MarkupPage object of the given page.
	 * 
	 * @param pagePath the requested page
	 * @return the MarkupPage object of the requested page
	 */
	MarkupPage load(String pagePath);


	/**
	 * load:
	 * Creates a MarkupPage object of the given page.
	 * 
	 * @param pagePath the requested page
	 * @param version the requested version
	 * @return the MarkupPage object of the requested page
	 */
	MarkupPage load(String pagePath, int version);


	/**
	 * rename:
	 * Renames the page with the given pagePath
	 * 
	 * @param pagePath the complete current path of the page
	 * @param newPage a page with the new page path
	 * @return <code>true</code> if and only if the page is successfully renamed; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 * @throws PageAlreadyExistsException
	 */
	boolean rename(String pagePath, MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException, PageAlreadyExistsException;


	/**
	 * save:
	 * Persists the MarkupPage object in the storage.
	 * If the page exists, the current version will be saved in the versions folder.
	 * 
	 * @param newPage the page to save
	 * @return <code>true</code> if and only if the page is successfully deleted; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 */
	boolean save(MarkupPage newPage) throws ConcurrentEditException, EditWithoutChangesException;
}
