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

import java.text.SimpleDateFormat;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LeadFilterReplacementTest {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private LeadFilterReplacement filterReplacement;
	private String filter;
	@Before
	public void setUp() throws Exception {
		filterReplacement = new LeadFilterReplacement("2012-07-01T00:00:00.000Z", "2012-07-24T00:00:00.000Z");
	}

	@After
	public void tearDown() throws Exception {
		filterReplacement = null;
		filter = null;
	}

	@Test
	public void testReplace_DateEq() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"createDate\",\"op\":\"eq\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}";
		
		Assert.assertEquals("{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"createdDate\",\"op\":\"le\",\"data\":\"2012-07-03T07:59:59.000+08:00\"}," +
				"{\"field\":\"createdDate\",\"op\":\"ge\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}", 
				filterReplacement.replace(filter));
	}
	
	@Test
	public void testReplace_NonDateField() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"firstName\",\"op\":\"eq\",\"data\":\"Mark\"}]}";
		
		Assert.assertEquals("{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"createdDate\",\"op\":\"le\",\"data\":\"2012-07-24T00:00:00.000Z\"}," +
				"{\"junction\":\"and\",\"field\":\"createdDate\",\"op\":\"ge\",\"data\":\"2012-07-01T00:00:00.000Z\"}," +
				"{\"field\":\"firstName\",\"op\":\"eq\",\"data\":\"Mark\"}]}", 
				filterReplacement.replace(filter));
	}
	
	@Test
	public void testReplace_NonDateField_ButWithDateValue() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"firstName\",\"op\":\"eq\",\"data\":\"2012-07-01T00:00:00.000Z\"}]}";
		
		Assert.assertEquals("{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"createdDate\",\"op\":\"le\",\"data\":\"2012-07-24T00:00:00.000Z\"}," +
				"{\"junction\":\"and\",\"field\":\"createdDate\",\"op\":\"ge\",\"data\":\"2012-07-01T00:00:00.000Z\"}," +
				"{\"field\":\"firstName\",\"op\":\"eq\",\"data\":\"2012-07-01T00:00:00.000Z\"}]}", 
				filterReplacement.replace(filter));
	}
	
}
