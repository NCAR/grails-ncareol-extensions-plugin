# grails-ncareol-extensions-plugin

**INACTIVE LEGACY REPOSITORY**

Grails plugin to include and load common Groovy and Grails extensions

Includes additional methods for String and Date via groovy-ncareol-extensions,
the Grails tag g:logMsg, fixed and extended sortableColumn and paginate tags,
plus some other Groovy & Grails utility/extension/testing classes.
Adds ActiveBackgroundService (which uses GPars) to make any service/bean
method into a background task.

## Versions

Version 1.17.1 (tag: v1.17.1) is hopefully the final version
of this plugin.  (v1.17.1 is the same code as v1.17.0,
with only the addition of these notes.)

See also our new similar plugin repository supporting Grails 4+:

 * https://github.com/NCAR/grails-ncar-extensions

## Dependencies

This plugin has a dependency for groovy-ncareol-extensions.
Most older versions of that repo have been included as tags
in the new repo (for Grails 4):

 * https://github.com/NCAR/groovy-ncar-extensions

Versions of this repo up to 1.16.0 can use the
groovy-ncar-extensions tag v1.5.1 (or an older corresponding
tag). The final version of this repo (1.17.0) depends on
groovy-ncareol-extensions 1.6.0, which is not available
in groovy-ncar-extensions. It is archived here in
`lib/groovy-ncar-extensions-1.6.0.zip`, which should be
found by the build process.

## Grails fixes

This plugin requires Grails 2.3.x (currently 2.3.11) which
includes Spring Loaded 1.2.0 which is incompatible with Java8 (jdk1.8.0_40+).
No fix is available until Grails 2.4.5.

 * https://github.com/spring-projects/spring-loaded/issues/98
 * https://jira.grails.org/browse/GRAILS-12042

Download Spring Loaded 1.2.2:

 * http://repo.spring.io/release/org/springframework/springloaded/1.2.2.RELEASE/

Remove the old jars and .pom files and place the new files in
their corresponding places under:

 * `$GRAILS_HOME/lib/org.springframework/springloaded/`

Rename and edit `ivy-1.2.0.RELEASE.xml` to reflect the new version number.
Unfortunately, that's not enough because something still wants the 1.2.0 jar.
So symlink that name:

    cd $GRAILS_HOME/lib/org.springframework/springloaded/jars
    ln -s springloaded-1.2.2.RELEASE.jar springloaded-1.2.0.RELEASE.jar

## Development

Hint: to deploy a new version to our internal Maven remote repository,
use "umask 2; grails maven-deploy" (via the release plugin).

## License

See LICENSE.txt
