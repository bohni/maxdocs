package org.maxdocs.engine;

import java.util.List;
import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.data.PageLight;
import org.maxdocs.data.TagCloudEntry;
import org.maxdocs.exceptions.ConcurrentEditException;
import org.maxdocs.exceptions.EditWithoutChangesException;
import org.maxdocs.exceptions.PageAlreadyExistsException;

/**
 * MaxDocs:
 * Interface for the engine of MaxDocs.
 * 
 * @author Team maxdocs.org
 */
public interface MaxDocs
{
	/**
	 * delete:
	 * Deletes the page with the given path.
	 * 
	 * @param pagePath the complete path of the page
	 * @return <code>true</code> if and only if the file is successfully deleted; <code>false</code> otherwise
	 */
	boolean delete(String pagePath);


	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the complete path of the page
	 * @return <code>true</code> if the pagePath exists; <code>false</code> otherwise
	 */
	boolean exists(String pagePath);


	/**
	 * getDefaultMarkupLangage:
	 * Returns the default markup language.
	 * 
	 * @return the default markup language
	 */
	String getDefaultMarkupLangage();


	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	HtmlPage getHtmlPage(String pagePath);


	/**
	 * getMarkupLangages:
	 * Returns a map with all supported markup languages.
	 * key is service name.
	 * value is display name.
	 * 
	 * @return all supported markup languages as map.
	 */
	Map<String, String> getMarkupLangages();


	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	MarkupPage getMarkupPage(String pagePath);


	/**
	 * getTagCloud:
	 * Returns a map with the tags of the tag cloud.
	 * Keys of the map are the tags.
	 * Value of the map is the weight of the tag as Integer.
	 * 
	 * @return a map with the tag cloud
	 */
	Map<String, Integer> getTagCloud();


	/**
	 * getPagesForTag:
	 * Returns a list with all page paths to the given tag
	 * 
	 * @param tag a tag
	 * @return the list with page paths or null, if tag is unkown
	 */
	List<String> getPagesForTag(String tag);


	/**
	 * getVersions:
	 * Returns a list with all versions of the given page
	 * 
	 * @param pagePath the complete path of the page
	 * @return list containing {@link PageLight} objects for every version
	 */
	List<PageLight> getVersions(String pagePath);


	/**
	 * rename:
	 * Renames the page with the given pagePath
	 * 
	 * @param pagePath the complete current path of the page
	 * @param newPage a page with the new page path, current version, author and editor set
	 * @return <code>true</code> if and only if the file is successfully renamed; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 * @throws PageAlreadyExistsException
	 */
	boolean rename(String pagePath, MarkupPage newPage) throws ConcurrentEditException,
		EditWithoutChangesException, PageAlreadyExistsException;


	/**
	 * save:
	 * Saves the given markup page.
	 * 
	 * @param markupPage will be saved to the content folder
	 * @return <code>true</code> if and only if the page is successfully saved; <code>false</code> otherwise
	 * @throws ConcurrentEditException
	 * @throws EditWithoutChangesException
	 */
	boolean save(MarkupPage markupPage) throws ConcurrentEditException, EditWithoutChangesException;
}
