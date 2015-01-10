package org.maxdocs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.maxdocs.data.MarkupPageTest;
import org.maxdocs.engine.MaxDocsImplTest;
import org.maxdocs.parser.MarkupParserTest;
import org.maxdocs.storage.FileStorageTest;
import org.maxdocs.taglib.TaglibSuite;

/**
 * TaglibSuite:
 * Test suit for running all taglib test classes.
 *
 * @author Team maxdocs.org
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	MarkupPageTest.class,
	MaxDocsImplTest.class,
	MarkupParserTest.class,
	FileStorageTest.class,
	TaglibSuite.class
})
public class MaxDocsTestSuite
{
	// the class remains completely empty,
	// being used only as a holder for the above annotations
}
