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
package com.xpn.xwiki.watch.client.ui.utils;

import com.google.gwt.user.client.ui.HTML;

/**
 * Utility class to provide HTML elements for various types of messages. They will all be formatted in the same way
 * and will have associated styles so that the error/warning/info reporting is universal across the application.
 */
public class HTMLMessages
{
    public static HTML getInfoHTML(String message) 
    {
        //icon, border, font 5599FF
        //content: E8F1FF
        HTML infoHTML = new HTML();
        infoHTML.addStyleName("info");
        infoHTML.setHTML(message);
        return infoHTML;
    }
}
