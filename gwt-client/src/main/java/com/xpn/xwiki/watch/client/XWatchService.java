/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * <p/>
 * This is free software;you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation;either version2.1of
 * the License,or(at your option)any later version.
 * <p/>
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software;if not,write to the Free
 * Software Foundation,Inc.,51 Franklin St,Fifth Floor,Boston,MA
 * 02110-1301 USA,or see the FSF site:http://www.fsf.org.
 */
package com.xpn.xwiki.watch.client;

import java.util.List;
import com.xpn.xwiki.gwt.api.client.XWikiGWTException;
import com.xpn.xwiki.gwt.api.client.XWikiService;

/**
 * Service to expose Watch specific functions, extending the default XWikiService, to preserve the default functions.
 */
public interface XWatchService extends XWikiService
{
    public List getArticles(String sql, int nb, int start) throws XWikiGWTException;
    
    /**
     * Returns the configuration documents for the instance of Watch in the specified space: the feeds, groups and 
     * the keywords.
     * 
     * @param watchSpace the Watch space
     * @return the list of configuration documents in the specified space
     * @throws XWikiGWTException
     */
    public List getConfigDocuments(String watchSpace) throws XWikiGWTException;
}
