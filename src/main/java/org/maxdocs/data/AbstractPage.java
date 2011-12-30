package org.maxdocs.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * AbstractPage:
 *  TODO - Stefan, 20.12.2011: documentation
 *
 * @author Team maxdocs.org
 *
 */
public class AbstractPage
{
	private int version;

	private Date firstVersionCreationDate;

	private Date currentVersionCreationDate;

	private String author;

	private String editor;

	private String contentType;

	private String pageName;

	private String pagePath;

	private List<String> tags = new ArrayList<String>();

	public AbstractPage()
	{
		this.version = 1;
		this.currentVersionCreationDate = new Date();
		this.firstVersionCreationDate = new Date();
	}
	
	public AbstractPage(AbstractPage page)
	{
		this();
		if(page != null)
		{
			BeanUtils.copyProperties(page, this);
		}
		this.currentVersionCreationDate = new Date();
	}
	
	/**
	 * getVersion: Returns the version.
	 *
	 * @return the version
	 */
	public int getVersion()
	{
		return this.version;
	}

	/**
	 * setVersion: Sets the version.
	 *
	 * @param version the version to set
	 */
	public void setVersion(int version)
	{
		this.version = version;
	}

	/**
	 * getFirstVersionCreationDate: Returns the firstVersionCreationDate.
	 *
	 * @return the firstVersionCreationDate
	 */
	public Date getFirstVersionCreationDate()
	{
		return this.firstVersionCreationDate;
	}

	/**
	 * setFirstVersionCreationDate: Sets the firstVersionCreationDate.
	 *
	 * @param firstVersionCreationDate the firstVersionCreationDate to set
	 */
	public void setFirstVersionCreationDate(Date firstVersionCreationDate)
	{
		this.firstVersionCreationDate = firstVersionCreationDate;
	}

	/**
	 * getCurrentVersionCreationDate: Returns the currentVersionCreationDate.
	 *
	 * @return the currentVersionCreationDate
	 */
	public Date getCurrentVersionCreationDate()
	{
		return this.currentVersionCreationDate;
	}

	/**
	 * setCurrentVersionCreationDate: Sets the currentVersionCreationDate.
	 *
	 * @param currentVersionCreationDate the currentVersionCreationDate to set
	 */
	public void setCurrentVersionCreationDate(Date currentVersionCreationDate)
	{
		this.currentVersionCreationDate = currentVersionCreationDate;
	}

	/**
	 * getAuthor: Returns the author.
	 *
	 * @return the author
	 */
	public String getAuthor()
	{
		return this.author;
	}

	/**
	 * setAuthor: Sets the author.
	 *
	 * @param author the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * getEditor: Returns the editor.
	 *
	 * @return the editor
	 */
	public String getEditor()
	{
		return this.editor;
	}

	/**
	 * setEditor: Sets the editor.
	 *
	 * @param editor the editor to set
	 */
	public void setEditor(String editor)
	{
		this.editor = editor;
	}

	/**
	 * getContentType: Returns the contentType.
	 *
	 * @return the contentType
	 */
	public String getContentType()
	{
		return this.contentType;
	}

	/**
	 * setContentType: Sets the contentType.
	 *
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * getPageName: Returns the pageName.
	 *
	 * @return the pageName
	 */
	public String getPageName()
	{
		return this.pageName;
	}

	/**
	 * setPageName: Sets the pageName.
	 *
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}

	/**
	 * getPagePath: Returns the pagePath.
	 *
	 * @return the pagePath
	 */
	public String getPagePath()
	{
		return this.pagePath;
	}

	/**
	 * setPagePath: Sets the pagePath.
	 *
	 * @param pagePath the pagePath to set
	 */
	public void setPagePath(String pagePath)
	{
		this.pagePath = pagePath;
	}

	/**
	 * getTags: Returns the tags.
	 * 
	 * @return the tags
	 */
	public List<String> getTags()
	{
		return tags;
	}

	/**
	 * setTags: Sets the tags.
	 * 
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public void addTag(String tag)
	{
		getTags().add(tag);
	}
}
