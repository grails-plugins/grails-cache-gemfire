/* Copyright 2012 SpringSource.
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
import grails.plugin.cache.gemfire.GrailsGemfireCacheManager
import grails.plugin.cache.web.filter.gemfire.GemfirePageFragmentCachingFilter

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.gemfire.CacheFactoryBean

class CacheGemfireGrailsPlugin {

	private final Logger log = LoggerFactory.getLogger('grails.plugin.cache.CacheGemfireGrailsPlugin')

	String version = '0.1-SNAPSHOT'
	String grailsVersion = '2.0 > *'
	def loadAfter = ['cache']
	def pluginExcludes = ['scripts/CreateCacheGemfireTestApps.groovy']

	String title = 'Gemfire Cache Plugin'
	String author = 'Burt Beckwith'
	String authorEmail = 'beckwithb@vmware.com'
	String description = 'A Gemfire-based implementation of the Cache plugin'
	String documentation = 'http://grails.org/plugin/cache-gemfire'

	String license = 'APACHE'
	def organization = [name: 'SpringSource', url: 'http://www.springsource.org/']
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPCACHEGEMFIRE']
	def scm = [url: 'https://github.com/grails-plugins/grails-cache-gemfire']

	def doWithSpring = {
		if (!isEnabled(application)) {
			log.warn 'Gemfire Cache plugin is disabled'
			return
		}

		def cacheConfig = application.config.grails.cache
		def gemfireConfig = cacheConfig.gemfire
		if (cacheConfig.config instanceof Closure) {
			// TODO
		}
 
		gemfireCache(CacheFactoryBean)

		grailsCacheManager(GrailsGemfireCacheManager) {
			cache = ref('gemfireCache')
		}

		grailsCacheFilter(GemfirePageFragmentCachingFilter) {
			cacheManager = ref('grailsCacheManager')
			nativeCacheManager = ref('gemfireCache')
			// TODO this name might be brittle - perhaps do by type?
			cacheOperationSource = ref('org.springframework.cache.annotation.AnnotationCacheOperationSource#0')
			keyGenerator = ref('webCacheKeyGenerator')
			expressionEvaluator = ref('webExpressionEvaluator')
		}
	}

	def doWithApplicationContext = { ctx ->

		def cacheConfig = application.config.grails.cache

		// TODO hard-coding from the core plugin for now
		def configuredCacheNames = ['grailsBlocksCache', 'grailsTemplatesCache']
		if (cacheConfig.config instanceof Closure) {
			// reusing the builder from the core plugin since there's no configurability yet
			ConfigBuilder builder = new ConfigBuilder()
			builder.parse cacheConfig.config
			configuredCacheNames.addAll builder.cacheNames
		}

		// just calling getCache() here to make sure all of the caches exist at startup
		def cacheManager = ctx.grailsCacheManager
		for (String name in configuredCacheNames) {
			cacheManager.getCache name
		}

		log.debug "Configured caches: $cacheManager.cacheNames"
	}

	private boolean isEnabled(GrailsApplication application) {
		// TODO
		true
	}
}
