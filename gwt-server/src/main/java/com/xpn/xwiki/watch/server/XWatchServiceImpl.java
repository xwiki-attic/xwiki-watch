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
package com.xpn.xwiki.watch.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.gwt.api.client.Document;
import com.xpn.xwiki.gwt.api.client.XWikiGWTException;
import com.xpn.xwiki.gwt.api.server.XWikiServiceImpl;
import com.xpn.xwiki.watch.client.XWatchService;

/**
 * Implementation of the {@link XWatchService} interface, to provide Watch specific functions to the Watch application.
 */
public class XWatchServiceImpl extends XWikiServiceImpl implements XWatchService
{
    protected static final Log WATCHLOG = LogFactory.getLog(XWatchServiceImpl.class);

    public List getArticles(String sql, int nb, int start) throws XWikiGWTException
    {
        try {
            XWikiContext context = getXWikiContext();
            return getArticles(sql, nb, start, context);
        } catch (Exception e) {
            throw getXWikiGWTException(e);
        }
    }

    private List getArticles(String sql, int nb, int start, XWikiContext context)
        throws XWikiGWTException
    {
        List docList = new ArrayList();
        try {
            long timeStart = Calendar.getInstance().getTimeInMillis();
            // removed distinct to have faster querying and because it's useless
            String objectsSql = "select obj.name from BaseObject as obj " + sql; 
            List list = context.getWiki().search(objectsSql, nb, start, context);
            if ((list == null) && (list.size() == 0)) {
                return docList;
            }
            for (int i = 0; i < list.size(); i++) {
                if (context.getWiki().getRightService().hasAccessLevel("view", context.getUser(), (String) list.get(i),
                    context) == true) {
                    XWikiDocument doc = context.getWiki().getDocument((String) list.get(i), context);
                    Document apidoc = newDocument(new Document(), doc, true, true, false, false, context);
                    docList.add(apidoc);
                }
            }
            return docList;
        } catch (Exception e) {
            throw getXWikiGWTException(e);
        }
    }
}
