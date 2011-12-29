package org.maxdocs.engine;

import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;

/**
 * MaxDocs:
 * Interface for the engine of MaxDocs.
 * 
 * @author Team jspserver.net
 * 
 */
public interface MaxDocs
{

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath
	 *            the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public HtmlPage getHtmlPage(String pagePath);

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath
	 *            the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public MarkupPage getMarkupPage(String pagePath);

	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath
	 *            the requested page
	 * @return <code>true</code> if the pagePath exists
	 */
	public boolean exists(String pagePath);

	/**
	 * getTagCloud:
	 * Creates a map with the tags of the tag cloud.
	 * Keys of the map are the tags
	 * Value of the map is the weight of the tag as Integer
	 * 
	 * @return a map with the tag cloud.
	 */
	public Map<String, Integer> getTagCloud();


	/**
	 * save:
	 * Saves the given markup page.
	 * 
	 * @return <code>true</code> if saving succeeds.
	 */
	/**
	 * save:
	 * Saves the given markup page.
	 * 
	 * @param markupPage The markup page to save
	 * @param newPage If set to true, a new file is created.
	 * 				  If set to false, the previous version is saved to versions folder. 
	 * @return
	 */
	public boolean save(MarkupPage markupPage, boolean newPage);
}
