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
package grails.plugin.cache.web.filter.gemfire;

import grails.plugin.cache.web.PageInfo;
import grails.plugin.cache.web.filter.PageFragmentCachingFilter;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * Gemfire-based implementation of PageFragmentCachingFilter.
 *
 * @author Burt Beckwith
 */
public class GemfirePageFragmentCachingFilter extends PageFragmentCachingFilter {

	@Override
	protected int getTimeToLive(ValueWrapper wrapper) {
		// not applicable yet
		return Integer.MAX_VALUE;
	}

	@Override
	protected com.gemstone.gemfire.cache.Cache getNativeCacheManager() {
		return (com.gemstone.gemfire.cache.Cache)super.getNativeCacheManager();
	}

	@Override
	protected void put(Cache cache, String key, PageInfo pageInfo, Integer timeToLiveSeconds) {
		// TTL isn't supported yet
		cache.put(key, pageInfo);
	}
}
