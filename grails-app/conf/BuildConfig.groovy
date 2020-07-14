grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.repos.internalMaven.url = 'file:////net/www_dev/data/html/maven2'
grails.project.repos.default = 'internalMaven'
grails.release.scm.enabled = false

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = 'ivy'
grails.project.dependency.resolution = {
    cacheDir 'target/ivy-cache'

    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        mavenRepo grails.project.repos.internalMaven.url

        grailsCentral()
        mavenLocal()
        mavenCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // workaround for broken "grails doc": the jar is now in /lib/
        // supposedly was fixed in 2.3.1 but is not
        // https://grails.atlassian.net/browse/GRAILS-10508
        compile 'grails-docs:grails-docs:2.3.11'

        compile 'edu.ucar.eol:groovy-ncareol-extensions:1.6.0'
    }

    plugins {
        build(":release:3.1.2",
              ":rest-client-builder:2.1.1") {
            export = false
        }
    }
}
