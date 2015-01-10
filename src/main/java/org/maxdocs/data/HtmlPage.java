package org.maxdocs.data;

/**
 * HtmlPage:
 * An object containing the data of a page.
 * The content is already rendered in HTML.
 *
 * @author Team maxdocs.org
 */
public class HtmlPage extends AbstractPage
{
	private String content;


	/**
	 * Default constructor.
	 * Creates a {@link HtmlPage} object.
	 */

	public HtmlPage()
	{
		super();
	}


	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link HtmlPage} object with data from the given {@link MarkupPage}.
	 *
	 * @param markupPage a markup page 
	 */
	public HtmlPage(MarkupPage markupPage)
	{
		super(markupPage);
		setCurrentVersionCreationDate(markupPage.getCurrentVersionCreationDate());
	}


	/**
	 * getContent: Returns the content.
	 *
	 * @return the content
	 */
	public String getContent()
	{
		return this.content;
	}


	/**
	 * setContent: Sets the content.
	 *
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}
}
