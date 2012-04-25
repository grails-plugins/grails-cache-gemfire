grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
		mavenRepo 'http://dist.gemstone.com/maven/release'
	}

	dependencies {
		compile('org.springframework.data:spring-data-gemfire:1.1.1.RELEASE') {
			excludes 'commons-logging', 'gemfire', 'jcl-over-slf4j', 'log4j', 'slf4j-api',
			         'slf4j-log4j12', 'spring-context-support', 'spring-core', 'spring-tx'
		}

		compile 'com.gemstone.gemfire:gemfire:6.6.2'
	}

	plugins {
		build(':release:2.0.0', ':rest-client-builder:1.0.2') {
			export = false
		}
		compile ':cache:0.5.BUILD-SNAPSHOT'
	}
}

