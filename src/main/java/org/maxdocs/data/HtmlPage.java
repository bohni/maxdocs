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
