package org.maxdocs.taglib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageNameTagTEI:
 * Provides validation of the plain attribute.
 * 
 * @author Team maxdocs.org
 */
public class PageNameTagTEI extends AbstractMaxDocsTagTEI
{
	private static Logger log = LoggerFactory.getLogger(PageNameTagTEI.class);


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

		if (msgs.size() == 0)
		{
			return null;
		}
		ValidationMessage[] msgArray = msgs.toArray(new ValidationMessage[msgs.size()]);
		return msgArray; // NOPMD local used for debugging
	}
}
