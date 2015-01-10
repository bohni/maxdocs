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
