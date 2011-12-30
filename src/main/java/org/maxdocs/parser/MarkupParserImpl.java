/**
 * Copyright (c) 2010-2011, Team maxdocs.org
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
package org.maxdocs.parser;

import java.io.StringWriter;

import net.java.textilej.parser.builder.HtmlDocumentBuilder;

import org.maxdocs.MaxDocsConstants;
import org.maxdocs.data.HtmlPage;
import org.maxdocs.data.MarkupPage;
import org.maxdocs.parser.markup.mediawiki.MediaWikiDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupParserImpl:
 *
 * @author Matthias Mezger
 */
public class MarkupParserImpl implements MarkupParser
{
	private static Logger log = LoggerFactory.getLogger(MarkupParserImpl.class);

	/* (non-Javadoc)
	 * @see org.maxdocs.parser.MarkupParser#parseToHtml(org.maxdocs.data.MarkupPage)
	 */
	@Override
	public HtmlPage parseToHtml(MarkupPage markupPage)
	{
		log.trace("parseToHtml", markupPage.getPagePath());
		HtmlPage htmlPage = new HtmlPage(markupPage);
		
		if(MaxDocsConstants.MARKUP_CONTENT_TYPE_MEDIAWIKI.equals(markupPage.getContentType()))
		{
			net.java.textilej.parser.MarkupParser parser = new net.java.textilej.parser.MarkupParser();
			MediaWikiDialect dialect = new MediaWikiDialect();
			parser.setDialect(dialect);
			StringWriter out = new StringWriter();
			parser.setBuilder(new HtmlDocumentBuilder(out));
			parser.parse(markupPage.getContent(), false);
			htmlPage.setContent(out.toString());
		}
		else
		{
			htmlPage.setContent("<b>error: content-type "+markupPage.getContentType()+" not supported</b>");
		}

		return htmlPage;
	}
}
