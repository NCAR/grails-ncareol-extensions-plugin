/*
 * This file copied from Grails 2.3.x.
 *
 * repo: git@github.com:grails/grails-core.git
 * branch: 2.3.x
 * commit: 5d892e865ada2c4188b6319e3cb4fca4846164ef
 *
 * file: grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/ApplicationTagLib.groovy
 *
 * web: https://github.com/grails/grails-core/blob/2.3.x/grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/ApplicationTagLib.groovy
 * web: https://github.com/grails/grails-core/blob/eaf8da18796c99e2df634859cfd4069ed41ef23d/grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/ApplicationTagLib.groovy
 *
 *
 * File reduced to the link closure, which was renamed "rawlink"
 * and modified to stop (re)encoding the link,
 * by John Allison <jja@sinequanon.net>, Sep 2016.
 *
 */

/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ucar.eol.tags

import grails.artefact.Artefact
import org.springframework.beans.factory.annotation.Autowired

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
//import org.codehaus.groovy.grails.web.mapping.LinkGenerator


/**
 * Link tags, copied from Grails source,
 * with new 'eol:rawlink' tag.
 *
 * @author Graeme Rocher
 * @author John Allison
 */
@Artefact("TagLibrary")
class LinkTagLib {

    /**
     * Tags are on the 'eol' namespace.
     */
    static namespace = 'eol'

    @Autowired
    LinkGenerator linkGenerator

    /**
     * General linking to controllers, actions etc.
     * Uses the raw encoding for the final created link, where Grails encodes as HTML.
     * While HTML spec mentions the SGML requirement to entity-encoded the & separator,
     * sometimes we want it left alone/raw.
     * The attributes (query parameter keys and values) are already encoded by
     * java.net.URLEncoder via the grails UrlCreator via the grails LinkGenerator.
     *
     * Examples:<br/>
     * <code><pre>
     * &lt;g:rawlink action="myaction"&gt;link 1&lt;/gr:link&gt;<br/>
     * &lt;g:rawlink controller="myctrl" action="myaction"&gt;link 2&lt;/gr:link&gt;<br/>
     * </pre></code>
     *
     * @attr controller The name of the controller to use in the link, if not specified the current controller will be linked
     * @attr action The name of the action to use in the link, if not specified the default action will be linked
     * @attr uri relative URI
     * @attr url A map containing the action,controller,id etc.
     * @attr base Sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behaviour of the absolute property, if both are specified.
     * @attr absolute If set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:&lt;port&gt; if no value in Config and not running in production.
     * @attr id The id to use in the link
     * @attr fragment The link fragment (often called anchor tag) to use
     * @attr params A map containing URL query parameters
     * @attr mapping The named URL mapping to use to rewrite the link
     * @attr event Webflow _eventId parameter
     * @attr elementId DOM element id
     */
    Closure rawlink = { attrs, body ->

        def writer = getOut()
        def elementId = attrs.remove('elementId')
        def linkAttrs
        if (attrs.params instanceof Map && attrs.params.containsKey('attrs')) {
            linkAttrs = attrs.params.remove('attrs').clone()
        }
        else {
            linkAttrs = [:]
        }
        writer <<  '<a href=\"'

        // The original encodeAsHTML() encodes the & separator for the
        // URL query parameters which is AWFUL!
        // The keys and values are already encoded by java.net.URLEncoder
        // via the Grails UrlCreator called by the Grails LinkGenerator.
        // -jja
        writer << createLink(attrs).encodeAsRaw()

        writer << '"'
        if (elementId) {
            writer << " id=\"${elementId}\""
        }
        def remainingKeys = attrs.keySet() - LinkGenerator.LINK_ATTRIBUTES
        for (key in remainingKeys) {
            writer << " " << key << "=\"" << attrs[key]?.encodeAsHTML() << "\""
        }
        for (entry in linkAttrs) {
            writer << " " << entry.key << "=\"" << entry.value?.encodeAsHTML() << "\""
        }
        writer << '>'
        writer << body()
        writer << '</a>'
    }

}
