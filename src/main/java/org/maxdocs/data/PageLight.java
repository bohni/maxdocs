package org.maxdocs.data;


/**
 * PageLight
 * An object that holds necessary data for displaying the version history
 * of a page.
 * 
 * @author Team maxdocs.org
 *
 */
public class PageLight extends AbstractPage
{
	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link PageLight} object with the given parameters.
	 *
	 * @param page
	 */
	public PageLight(MarkupPage page)
	{
		super(page);
		setCurrentVersionCreationDate(page.getCurrentVersionCreationDate());
	}
}
