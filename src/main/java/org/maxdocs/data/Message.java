/**
 * Copyright (c) 2011-2013, Team maxdocs.org
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
