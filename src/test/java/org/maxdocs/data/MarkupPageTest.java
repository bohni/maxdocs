package org.maxdocs.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
		String markupLanguage = "ContentType";
		Date currentVersionCreationDate = new Date();
		String editor = "editor";
		Date firstVersionCreationDate = new Date();
		String pageName = "PageName";
		String pagePath = "/PageName";
		Set<String> tags = new HashSet<String>();
		int version = 1;
		
		MarkupPage page1 = new MarkupPage();
		page1.setAuthor(author);
		page1.setContent(content);
		page1.setMarkupLanguage(markupLanguage);
		page1.setCurrentVersionCreationDate(currentVersionCreationDate);
		page1.setEditor(editor);
		page1.setFirstVersionCreationDate(firstVersionCreationDate);
		page1.setPagePath(pagePath);
		page1.setTags(tags);
		page1.setVersion(version);
		
		MarkupPage page2 = new MarkupPage(page1);
		page2.setCurrentVersionCreationDate(currentVersionCreationDate);
		
		assertNotSame("page1 und page2 sind das gleiche Objekt", page1, page2);
		assertEquals("author falsch", author, page2.getAuthor());
		assertEquals("content falsch", content, page2.getContent());
		assertEquals("markupLanguage falsch", markupLanguage, page2.getMarkupLanguage());
		assertEquals("currentVersionCreationDate falsch", currentVersionCreationDate, page2.getCurrentVersionCreationDate());
		assertEquals("editor falsch", editor, page2.getEditor());
		assertEquals("firstVersionCreationDate falsch", firstVersionCreationDate, page2.getFirstVersionCreationDate());
		assertEquals("pageName falsch", pageName, page2.getPageName());
		assertEquals("pagePath falsch", pagePath, page2.getPagePath());
		assertEquals("tags falsch", tags, page2.getTags());
		assertEquals("version falsch", version, page2.getVersion());
		
		MarkupPage page3 = new MarkupPage(null);
		assertTrue("", page3.getCurrentVersionCreationDate() != null);
	}
}
