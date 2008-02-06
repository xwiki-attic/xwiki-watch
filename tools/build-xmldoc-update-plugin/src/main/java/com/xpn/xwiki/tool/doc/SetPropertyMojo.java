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
package com.xpn.xwiki.tool.doc;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

/**
 * Set/Change a string property in a XWiki Object
 * 
 * @version $Id: $
 * @goal setproperty
 * @phase package
 */
public class SetPropertyMojo extends AbstractDocumentMojo
{

    /**
     * The class name of the object to write the property value to
     * 
     * @parameter expression="XWiki.XWikiPreferences"
     * @required
     */
    private String className;

    /**
     * The object id to write the property value to
     * 
     * @parameter expression="0"
     * @required
     */
    private int objectId;

    /**
     * The property name
     * 
     * @parameter
     * @required
     */
    private String propertyName;

    /**
     * The property value to write
     * 
     * @parameter
     * @required
     */
    private String value;

    /**
     * Append the value to the existing one
     * 
     * @parameter expression="false"
     */
    private boolean append;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        XWikiDocument doc = loadFromXML(sourceDocument);
        BaseObject bo = doc.getObject(className, objectId);
        if (append) {
            value += bo.getStringValue(propertyName);
        }

        bo.setStringValue(propertyName, value);

        File outputFile =
            new File(getSpaceDirectory(outputDirectory, sourceDocument), sourceDocument.getName());

        writeToXML(doc, outputFile);
    }
}