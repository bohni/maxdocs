package org.maxdocs.taglib;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageExistsTag
 * If the page does not exists, the body part will be rendered.
 * 
 * @author Team maxdocs.org
 */
public class NoSuchPageTag extends PageExistsTag
{
	private static Logger log = LoggerFactory.getLogger(NoSuchPageTag.class);


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException
	{
		log.trace("doStartTag()");
		return super.doStartTag() == SKIP_BODY ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
}
