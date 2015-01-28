package org.maxdocs.taglib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateTagTEI:
 * Provides validation of the plain, format and type attributes.
 * 
 * @author Team maxdocs.org
 */
public class DateTagTEI extends AbstractMaxDocsTagTEI
{
	private static Logger log = LoggerFactory.getLogger(DateTagTEI.class);


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
		Object o = data.getAttribute("format");
		if (o != null && o != TagData.REQUEST_TIME_VALUE)
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat((String) o);
				sdf.format(new Date());
			}
			catch (IllegalArgumentException e)
			{
				msgs.add(new ValidationMessage(data.getId(), "Invalid date format."));
			}
		}

		o = data.getAttribute("type");
		if (o != null && o != TagData.REQUEST_TIME_VALUE)
		{
			if (!((String) o).equalsIgnoreCase("creation") && !((String) o).equalsIgnoreCase("lastchange"))
			{
				msgs.add(new ValidationMessage(data.getId(),
					"Invalid value for type. Only 'creation' or 'lastChange' supported."));
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
