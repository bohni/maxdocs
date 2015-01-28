package org.maxdocs.taglib;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * TaglibSuite:
 * Test suit for running all taglib test classes.
 *
 * @author Team maxdocs.org
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	AuthorTagTest.class,
	BreadcrumbsTagTest.class,
	DateTagTest.class,
	InsertPageTagTest.class,
	MarkupLanguageTagTest.class,
	MessagesTagTest.class,
	PageNameTagTest.class,
	PageVersionTagTest.class,
})
public class TaglibSuite
{
	// the class remains completely empty,
	// being used only as a holder for the above annotations
}
