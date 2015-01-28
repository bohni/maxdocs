package org.maxdocs.taglib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractMaxDocsTagTEI:
 * Abstract super class for MaxDocs taglib tag extra info classes.
 * Provides validation of the plain attribute.
 * 
 * @author Team maxdocs.org
 */
public abstract class AbstractMaxDocsTagTEI extends TagExtraInfo
{
	private static Logger log = LoggerFactory.getLogger(AbstractMaxDocsTagTEI.class);


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#validate(javax.servlet.jsp.tagext.TagData)
	 */
	@Override
	public ValidationMessage[] validate(TagData data)
	{
		log.trace("validate(TagData)");
		List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();

		Object o = data.getAttribute("plain");
		if (o != null && o != TagData.REQUEST_TIME_VALUE)
		{
			if (!((String) o).equalsIgnoreCase("true") && !((String) o).equalsIgnoreCase("false"))
			{
				msgs.add(new ValidationMessage(data.getId(), "Invalid boolean value."));
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
