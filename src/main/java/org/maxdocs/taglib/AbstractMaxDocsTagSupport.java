package org.maxdocs.taglib;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * AbstractMaxDocsTagSupport:
 * Abstract super class for MaxDocs taglib tag classes.
 * Provides styleClass and plain attributes.
 * 
 * @author Team maxdocs.org
 */
public abstract class AbstractMaxDocsTagSupport extends TagSupport
{
	private String styleClass;

	private boolean plain = false;


	/**
	 * Default constructor.
	 * Creates an {@link AbstractMaxDocsTagSupport} object.
	 */
	public AbstractMaxDocsTagSupport()
	{
		this("", false);
	}


	/**
	 * Minimal constructor. Contains required fields.
	 * Creates an {@link AbstractMaxDocsTagSupport} object with the given parameters.
	 * 
	 * @param styleClass
	 */
	public AbstractMaxDocsTagSupport(String styleClass)
	{
		this(styleClass, false);
	}


	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates an {@link AbstractMaxDocsTagSupport} object with the given parameters.
	 * 
	 * @param styleClass
	 * @param plain
	 */
	public AbstractMaxDocsTagSupport(String styleClass, boolean plain)
	{
		super();
		this.styleClass = styleClass;
		this.plain = plain;
	}


	/**
	 * getStyleClass() returns the styleClass
	 * 
	 * @return the styleClass
	 */
	public String getStyleClass()
	{
		return styleClass;
	}


	/**
	 * setStyleClass() sets the styleClass
	 * 
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}


	/**
	 * isPlain() returns the plain
	 * 
	 * @return the plain
	 */
	public boolean isPlain()
	{
		return plain;
	}


	/**
	 * setPlain() sets the plain
	 * 
	 * @param plain the plain to set
	 */
	public void setPlain(boolean plain)
	{
		this.plain = plain;
	}
}
