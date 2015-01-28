package org.maxdocs.data;

/**
 * Message:
 * A message
 * 
 * @author Team maxdocs.org
 */
public class Message
{

	private String message;

	private Severity severity;


	/**
	 * Minimal constructor. Contains required fields.
	 * Creates a {@link Message} object with the given message and {@link Severity#INFO}
	 * 
	 * @param msg the message
	 */
	public Message(String msg)
	{
		this(msg, Severity.INFO);
	}


	/**
	 * Full constructor. Contains required and optional fields.
	 * Creates a {@link Message} object with the given parameters.
	 * 
	 * @param msg the message
	 * @param severity the severity
	 */
	public Message(String msg, Severity severity)
	{
		this.message = msg;
		this.severity = severity;
	}


	/**
	 * getMessage: Returns the message.
	 * 
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}


	/**
	 * setMessage: Sets the message.
	 * 
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}


	/**
	 * getSeverity: Returns the severity.
	 * 
	 * @return the severity
	 */
	public Severity getSeverity()
	{
		return severity;
	}


	/**
	 * setSeverity: Sets the severity.
	 * 
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity)
	{
		this.severity = severity;
	}
}
