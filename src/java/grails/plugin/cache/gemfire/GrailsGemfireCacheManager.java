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
package grails.plugin.cache.gemfire;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.gemfire.support.GemfireCache;
import org.springframework.util.Assert;

import com.gemstone.gemfire.cache.Region;

/**
 * Based on org.springframework.data.gemfire.support.GemfireCacheManager.
 * Changed to directly implement CacheManager since loadCaches() might be empty,
 * and it's possible to add caches at runtime.
 *
 * @author Costin Leau
 * @author Burt Beckwith
 */
public class GrailsGemfireCacheManager implements CacheManager, InitializingBean {

	protected com.gemstone.gemfire.cache.Cache gemfireCache;
	protected final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	protected final Set<String> cacheNames = new LinkedHashSet<String>();

	public Cache getCache(String name) {
		Cache cache = cacheMap.get(name);
		if (cache == null) {
			// check the gemfire cache again in case the cache was added at runtime
			Region<?, ?> region = gemfireCache.getRegion(name);
			if (region != null) {
				cache = new GemfireCache(region);
				addCache(cache);
			}
		}

		return cache;
	}

	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(cacheNames);
	}

	public void afterPropertiesSet() {
		for (Cache cache : loadCaches()) {
			addCache(cache);
		}
	}

	protected void addCache(Cache cache) {
		cacheMap.put(cache.getName(), cache);
		cacheNames.add(cache.getName());
	}

	protected Collection<Cache> loadCaches() {
		Assert.notNull(gemfireCache, "a backing GemFire cache is required");
		Assert.isTrue(!gemfireCache.isClosed(), "the GemFire cache is closed; an open instance is required");

		Set<Region<?, ?>> regions = gemfireCache.rootRegions();
		Collection<Cache> caches = new LinkedHashSet<Cache>(regions.size());
		for (Region<?, ?> region : regions) {
			caches.add(new GrailsGemfireCache(region));
		}

		return caches;
	}

	/**
	 * Dependency injection for the backing GemFire Cache.
	 * @param cache the cache
	 */
	public void setCache(com.gemstone.gemfire.cache.Cache cache) {
		gemfireCache = cache;
	}
}
