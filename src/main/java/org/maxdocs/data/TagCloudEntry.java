package org.maxdocs.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TagCloudEntry
 * A TagCloudEntry stores all attributes to a single tag.
 * 
 * @author Team maxdocs.org
 *
 */
public class TagCloudEntry
{
	private String tagName;

	private List<String> pages = Collections.synchronizedList(new ArrayList<String>());

	/**
	 * Minimal constructor. Contains required fields.

	 * Creates an {@link TagCloudEntry} object with the given parameters.
	 *
	 * @param tagName the name of the tag
	 */
	public TagCloudEntry(String tagName)
	{
		this.tagName = tagName;
	}
	
	public void addPage(String pagePath)
	{
		pages.add(pagePath);
	}

	/**
	 * getCount: Returns the count.
	 * 
	 * @return the count
	 */
	public int getCount()
	{
		return getPages().size();
	}


	/**
	 * getTagName: Returns the tagName.
	 * 
	 * @return the tagName
	 */
	public String getTagName()
	{
		return tagName;
	}


	/**
	 * getPages: Returns the pages.
	 * 
	 * @return the pages
	 */
	public List<String> getPages()
	{
		return pages;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[" + tagName + ":" + pages.size() + ":" + pages;
		
	}
}
