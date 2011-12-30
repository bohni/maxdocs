package org.maxdocs.parser.markup.mediawiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MediaWikiDialect
 *
 * @author Matthias Mezger
 */
public class MediaWikiDialect extends net.java.textilej.parser.markup.mediawiki.MediaWikiDialect
{
	private static Logger log = LoggerFactory.getLogger(MediaWikiDialect.class);

	/* (non-Javadoc)
	 * @see net.java.textilej.parser.markup.mediawiki.MediaWikiDialect#toInternalHref(java.lang.String)
	 */
	@Override
	public String toInternalHref(String pageName)
	{
		log.trace("toInternalHref", pageName);
		if (pageName.startsWith(":")) //category
		{ 
			pageName = pageName.substring(1);
		}
		return pageName;
	}
	
}
