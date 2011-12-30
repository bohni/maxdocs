package org.maxdocs.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MarkupPageTest
 * TODO, 30.12.2011: Documentation
 * 
 * @author Team maxdocs.org
 *
 */
public class MarkupPageTest
{
	private static Logger log = LoggerFactory.getLogger(MarkupPageTest.class);

	@Test
	public void testCopyContructor()
	{
		log.trace("testCopyContructor");
		String author = "author";
		String content = "Content";
		String contentType = "ContentType";
		Date currentVersionCreationDate = new Date();
		String editor = "editor";
		Date firstVersionCreationDate = new Date();
		String pageName = "PageName";
		String pagePath = "/PagName";
		List<String> tags = new ArrayList<String>();
		int version = 1;
		
		MarkupPage page1 = new MarkupPage();
		page1.setAuthor(author);
		page1.setContent(content);
		page1.setContentType(contentType);
		page1.setCurrentVersionCreationDate(currentVersionCreationDate);
		page1.setEditor(editor);
		page1.setFirstVersionCreationDate(firstVersionCreationDate);
		page1.setPageName(pageName);
		page1.setPagePath(pagePath);
		page1.setTags(tags);
		page1.setVersion(version);
		
		MarkupPage page2 = new MarkupPage(page1);
		page2.setCurrentVersionCreationDate(currentVersionCreationDate);
		
		assertNotSame(page1, page2);
		assertEquals(author, page2.getAuthor());
		assertEquals(content, page2.getContent());
		assertEquals(contentType, page2.getContentType());
		assertEquals(currentVersionCreationDate, page2.getCurrentVersionCreationDate());
		assertEquals(editor, page2.getEditor());
		assertEquals(firstVersionCreationDate, page2.getFirstVersionCreationDate());
		assertEquals(pageName, page2.getPageName());
		assertEquals(pagePath, page2.getPagePath());
		assertEquals(tags, page2.getTags());
		assertEquals(version, page2.getVersion());
	}
}
