/**
 * Copyright (c) 2011-2012, Team maxdocs.org
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
package org.maxdocs.taglib;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * AbstractMaxDocsTagSupport:
 * Abstract super class for MaxDocs taglib tag classes.
 * 
 * Provides styleClass and plain attributes.
 *
 * @author Team maxdocs.org
 */
public abstract class AbstractMaxDocsTagSupport extends TagSupport
{
	private String styleClass;

	private boolean plain = false;

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
