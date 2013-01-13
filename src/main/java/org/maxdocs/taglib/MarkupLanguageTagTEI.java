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
package org.maxdocs.taglib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupLanguageTagTEI:
 * Provides validation of the plain attribute.
 * 
 * @author Team maxdocs.org
 */
public class MarkupLanguageTagTEI extends AbstractMaxDocsTagTEI
{
	private static Logger log = LoggerFactory.getLogger(MarkupLanguageTagTEI.class);


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#validate(javax.servlet.jsp.tagext.TagData)
	 */
	@Override
	public ValidationMessage[] validate(TagData data)
	{
		log.trace("validate(TagData)");
		List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();

		ValidationMessage[] supermsgs = super.validate(data);
		if (supermsgs != null)
		{
			msgs.addAll(Arrays.asList(supermsgs));
		}

		Object o = data.getAttribute("type");
		if (o != null && o != TagData.REQUEST_TIME_VALUE)
		{
			if (!((String) o).equalsIgnoreCase("input") && !((String) o).equalsIgnoreCase("output"))
			{
				msgs.add(new ValidationMessage(data.getId(),
					"Invalid value for type. Only 'input' or 'output' supported."));
			}
		}

		o = data.getAttribute("size");
		if (o != null && o != TagData.REQUEST_TIME_VALUE)
		{
			try
			{
				Integer.parseInt((String) o);
			}
			catch (NumberFormatException e)
			{
				msgs.add(new ValidationMessage(data.getId(), "Invalid integer value."));
			}
		}

		if (msgs.size() == 0)
		{
			return null;
		}
		ValidationMessage[] msgArray = msgs.toArray(new ValidationMessage[msgs.size()]);
		return msgArray; // NOPMD local used for debugging
	}
}
