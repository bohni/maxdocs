/**
 * 
 */
package org.maxdocs.exceptions;

/**
 * EditWithoutChangesException:
 * TODO: Documentation
 *
 * @author Stefan Bohn
 *
 */
public class EditWithoutChangesException extends Exception
{
	/**
	 * Default constructor.
	 * Creates a {@link EditWithoutChangesException} object with the given parameters.
	 *
	 */
	public EditWithoutChangesException()
	{
		super("There where no changes made with this edit...");
	}
}
