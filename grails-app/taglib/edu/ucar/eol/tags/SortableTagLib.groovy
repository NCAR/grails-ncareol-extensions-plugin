/*
 * This file copied from Grails 2.3.x.
 *
 * repo: git@github.com:grails/grails-core.git
 * branch: 2.3.x
 * commit: eaf8da18796c99e2df634859cfd4069ed41ef23d
 *
 * file: grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/RenderTagLib.groovy
 *
 * web: https://github.com/grails/grails-core/blob/2.3.x/grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/RenderTagLib.groovy
 * web: https://github.com/grails/grails-core/blob/eaf8da18796c99e2df634859cfd4069ed41ef23d/grails-plugin-gsp/src/main/groovy/org/codehaus/groovy/grails/plugins/web/taglib/RenderTagLib.groovy
 *
 *
 * File reduced to the sortableColumn closure, which was modified and extended
 * as detailed in its GroovyDoc comments, by John Allison <jja@sinequanon.net>,
 * Feb 2016.
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

/**
 * Tags to help rendering of views and layouts.
 *
 * @author Graeme Rocher
 * @author John Allison
 */
@Artefact("TagLibrary")
class SortableTagLib {

    static namespace = 'eol'

    /**
     * Renders a sortable column to support sorting in list views.<br/>
     *
     * Attribute title or titleKey is required. When both attributes are specified then titleKey takes precedence,
     * resulting in the title caption to be resolved against the message source. In case when the message could
     * not be resolved, the title will be used as title caption.<br/>
     *
     * Examples:<br/>
     *
     * &lt;g:sortableColumn property="title" title="Title" /&gt;<br/>
     * &lt;g:sortableColumn property="title" title="Title" style="width: 200px" /&gt;<br/>
     * &lt;g:sortableColumn property="title" titleKey="book.title" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" titleKey="book.releaseDate" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr property name of the property relating to the field
     * @attr defaultOrder default order for the property; choose between asc (default if not provided) and desc
     * @attr title title caption for the column
     * @attr titleKey title key to use for the column, resolved against the message source
     * @attr action the name of the action to use in the link, if not specified the list action will be linked
     * @attr params A map containing extra URL query parameters
     * @attr total total number of results, if present then it's used to calculate the new offset when changing the order of the current column
     * @attr class CSS class name
     */
    Closure sortableColumn = { attrs ->
        def writer = out
        if (!attrs.property) {
            throwTagError("Tag [sortableColumn] is missing required attribute [property]")
        }

        if (!attrs.title && !attrs.titleKey) {
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")
        }

        def property = attrs.remove("property")
        def action = attrs.action ? attrs.remove("action") : (actionName ?: "list")

        def defaultOrder = attrs.remove("defaultOrder")
        if (defaultOrder != "desc") defaultOrder = "asc"

        // current sorting property and order
        def sort = params.sort
        def order = params.order

        def linkParams = [:]
        if (params.id) linkParams.put("id", params.id)

        // propagate "max" from standard params, allowing for later override by attribute params
        if (params.max) linkParams.max = params.max

        // add sorting property and params to link params
        def paramsAttr = attrs.remove("params")
        if (paramsAttr) linkParams.putAll(paramsAttr)
        linkParams.remove('offset')
        linkParams.sort = property

        def total = attrs.int('total',0)
        attrs.remove('total')

        // determine and add sorting order for this column to link params
        attrs.class = (attrs.class ? "${attrs.class} sortable" : "sortable")
        if (property == sort) {
            attrs.class = attrs.class + " sorted " + order
            if (order == "asc") {
                linkParams.order = "desc"
            }
            else {
                linkParams.order = "asc"
            }

            // when flipping the order of the current column
            // we can set offset to the currently bottom row
            // except at offset 0, when we flip the complete list
            def offset = params.int('offset',0)
            if (total && offset) {
                offset = total - offset - params.int('max',0)
                linkParams.offset = (offset > 0) ? offset : 0
            }
        }
        else {
            linkParams.order = defaultOrder
        }

        // determine column title
        def title = attrs.remove("title")
        def titleKey = attrs.remove("titleKey")
        def mapping = attrs.remove('mapping')
        if (titleKey) {
            if (!title) title = titleKey
            def messageSource = grailsAttributes.messageSource
            def locale = RCU.getLocale(request)
            title = messageSource.getMessage(titleKey, null, title, locale)
        }

        writer << "<th "
        // process remaining attributes
        attrs.each { k, v ->
            writer << "${k}=\"${v?.encodeAsHTML()}\" "
        }
        writer << '>'
        def linkAttrs = [params: linkParams]
        if (mapping) {
            linkAttrs.mapping = mapping
        }

        linkAttrs.action = action
        
        writer << link(linkAttrs) {
            title
        }
        writer << '</th>'
    }

}
