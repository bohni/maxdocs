package org.maxdocs.exceptions;

/**
 * PageAlreadyExistsException:
 * TODO, 02.02.2013: Documentation
 * 
 * @author Team maxdocs.org
 *
 */
public class PageAlreadyExistsException extends Exception
{

	/**
	 * Default constructor.
	 * Creates a {@link PageAlreadyExistsException} object.
	 */
	public PageAlreadyExistsException()
	{
		super("The page already exists");
	}
	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link PageAlreadyExistsException} object with the given parameters.
	 *
	 * @param message
	 */
	public PageAlreadyExistsException(String message)
	{
			super(message);
	}
}
