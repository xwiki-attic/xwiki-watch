/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.watch.it.selenium;

import java.lang.InterruptedException;

import com.xpn.xwiki.it.selenium.framework.AbstractXWikiTestCase;
import com.xpn.xwiki.it.selenium.framework.AlbatrossSkinExecutor;
import com.xpn.xwiki.it.selenium.framework.XWikiTestSuite;

import junit.framework.Test;

/**
 * Verify the GWT Reader loads in Watch
 * 
 * @version $Id: $
 */
public class WatchGWTLoadingTest extends AbstractXWikiTestCase
{
    public static Test suite()
    {
        XWikiTestSuite suite =
            new XWikiTestSuite("Verify the GWT Reader loads in Watch");
        suite.addTestSuite(WatchGWTLoadingTest.class, AlbatrossSkinExecutor.class);
        return suite;
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        loginAsAdmin();
    }

    public void testGWTLoading()
    {
        open("/xwiki/bin/view/Watch/Reader");
        // Wait for the reader page to load
        getSelenium().waitForPageToLoad("2000");
        // Now wait for the GWT reader to load.
        try
        {
            // this is a bit dirty, but I could not find an easy way
            // to ask Selenium to just wait, without conditions.
            // We could investigate the use of waitForCondition() with a JS function argument
            // althought it seems it will be a bit heavy to setup for this use case.
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            fail();
        }
        assertTextPresent("Welcome to XWiki Watch");
    }
}
