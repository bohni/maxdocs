/**
 * 
 */
package org.maxdocs.exceptions;

/**
 * ConcurrentEditException:
 * TODO: Documentation
 *
 * @author Team maxdocs.org
 *
 */
public class ConcurrentEditException extends Exception
{
	private static final long serialVersionUID = -6198131730630854821L;

	/**
	 * Default constructor.
	 * Creates a {@link ConcurrentEditException} object.
	 *
	 */
	public ConcurrentEditException()
	{
		super("There was a concurrent edit on the page!");
	}
}
