/*
 * Copyright 2011-2012 the original author or authors.
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

package com.github.markserrano.jsonquery.jpa.filter;

import java.util.HashMap;
import java.util.Map;


import com.github.markserrano.jsonquery.jpa.filter.DateRangeFilterReplacement;
import com.github.markserrano.jsonquery.jpa.util.FieldReplacementUtil;

/**
 * Sample implementation of {@link DateRangeFilterReplacement}
 */
public class LeadFilterReplacement extends DateRangeFilterReplacement {

	public LeadFilterReplacement(String datefrom, String dateto) {
		super(datefrom, dateto);
	}

	@Override
	public Map<String, String> getReplacementMap() {
		Map<String, String> replacement = new HashMap<String, String>();
		replacement.put("createDate", "createdDate");
		return replacement;
	}
	
	public String preReplace(String filter) {
		if (FieldReplacementUtil.doesFieldAndOpExists(filter, "createDate", "eq")) {
			filter = FieldReplacementUtil.forDateRange(filter, "createDate");
		} else {
			filter = FieldReplacementUtil.forDateRange(filter, "createdDate", datefrom, dateto);
		} 
		return filter;
	}
	

}
