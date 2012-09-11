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

package com.github.markserrano.jsonquery.jpa.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.filter.FilterReplacement;
import com.github.markserrano.jsonquery.jpa.filter.LeadFilterReplacement;
import com.github.markserrano.jsonquery.jpa.util.HqlFilterUtil;


public class HqlFilterUtilTest {

	private FilterReplacement filterReplacement;
	
	@Before
	public void setUp() throws Exception {
		filterReplacement = new LeadFilterReplacement("2012-07-01T00:00:00.000Z", "2012-07-30T00:00:00.000Z");
	}

	@After
	public void tearDown() throws Exception {
		filterReplacement = null;
	}


	@Test
	public void testGetData() {
		Assert.assertEquals("test", HqlFilterUtil.getDateOrReturnData("test"));
		Assert.assertEquals("'2012-07-03'", HqlFilterUtil.getDateOrReturnData("2012-07-03T07:59:59.000+08:00"));
	}
	
	@Test
	public void testWhere() {		
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"createDate\",\"op\":\"eq\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}";
		Assert.assertEquals("WHERE createdDate <= '2012-07-03' AND createdDate >= '2012-07-02'", HqlFilterUtil.where(filterReplacement.replace(filter)));
	}
	
	@Test
	public void testWhere_Contains() {		
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"firstName\",\"op\":\"cn\",\"data\":\"Mark\"}]}";
		Assert.assertEquals("WHERE createdDate <= '2012-07-30' AND createdDate >= '2012-07-01' AND firstName LIKE '%Mark%'", HqlFilterUtil.where(filterReplacement.replace(filter)));
	}	
	
	@Test
	public void testWhere_BeginsWith() {		
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"firstName\",\"op\":\"bw\",\"data\":\"Mark\"}]}";
		Assert.assertEquals("WHERE createdDate <= '2012-07-30' AND createdDate >= '2012-07-01' AND firstName LIKE '%Mark'", HqlFilterUtil.where(filterReplacement.replace(filter)));
	}

	@Test
	public void testWhere_EndsWith() {		
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"firstName\",\"op\":\"ew\",\"data\":\"Mark\"}]}";
		Assert.assertEquals("WHERE createdDate <= '2012-07-30' AND createdDate >= '2012-07-01' AND firstName LIKE 'Mark%'", HqlFilterUtil.where(filterReplacement.replace(filter)));
	}
}
