/**
 * Copyright (c) 2011-2013, Team maxdocs.org
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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