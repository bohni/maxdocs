package org.maxdocs.engine;

import java.util.Map;

import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;

/**
 * MaxDocs:
 * Interface for the engine of MaxDocs.
 * 
 * @author Team maxdocs.org
 * 
 */
public interface MaxDocs
{

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public HtmlPage getHtmlPage(String pagePath);

	/**
	 * getHtmlPage() creates an object containing all data of the requested page.
	 * 
	 * @param pagePath the complete path of the page
	 * @return an object containing all data of the requested page
	 */
	public MarkupPage getMarkupPage(String pagePath);

	/**
	 * exists:
	 * Checks if a page exists with the given pagePath.
	 * 
	 * @param pagePath the requested page
	 * @return <code>true</code> if the pagePath exists
	 */
	public boolean exists(String pagePath);

	/**
	 * getTagCloud:
	 * Creates a map with the tags of the tag cloud.
	 * Keys of the map are the tags.
	 * Value of the map is the weight of the tag as Integer.
	 * 
	 * @return a map with the tag cloud
	 */
	public Map<String, Integer> getTagCloud();


	/**
	 * save:
	 * Saves the given markup pages.
	 * 
	 * @param oldPage if not null it will be saved to the versions folder
	 * @param newPage will be saved to the content folder
	 * @return <code>true</code> if saving succeeds
	 */
	public boolean save(MarkupPage oldPage, MarkupPage newPage);
}
