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

import grails.plugin.cache.GrailsValueWrapper;

import org.springframework.data.gemfire.support.GemfireCache;

import com.gemstone.gemfire.cache.Region;

/**
 * @author Burt Beckwith
 */
public class GrailsGemfireCache extends GemfireCache {

	/**
	 * Constructor.
	 * @param region
	 */
	public GrailsGemfireCache(Region<?, ?> region) {
		super(region);
	}

	@Override
	public GrailsValueWrapper get(Object key) {
		Object value = getNativeCache().get(key);
		return value == null ? null : new GrailsValueWrapper(value, null);
	}
}
