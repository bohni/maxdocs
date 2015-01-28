package org.maxdocs.data;


/**
 * MarkupPage
 * An object containing the data of a page.
 * The content is in markup.
 * 
 * @author Team maxdocs.org
 *
 */
public class MarkupPage extends AbstractPage
{
	private String content;

	/**
	 * Default constructor.
	 * Creates a {@link MarkupPage} object.
	 */
	public MarkupPage()
	{
		super();
	}
	
	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link MarkupPage} object with the given parameters.
	 *
	 * @param page a markup page.
	 */
	public MarkupPage(MarkupPage page)
	{
		super(page);
		if(page != null)
		{
			this.content = page.content;
		}
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
