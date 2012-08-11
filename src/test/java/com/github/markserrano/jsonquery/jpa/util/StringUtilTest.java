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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.util.StringUtil;

public class StringUtilTest {

	private String path;
	@Before
	public void setUp() throws Exception {
		path = "company.department.person.firstName";
	}

	@After
	public void tearDown() throws Exception {
		path = null;
	}

	@Test
	public void testGetFieldAtIndex_First() {
		Assert.assertEquals("company", StringUtil.getFieldAtIndex(path, 0));
	}

	@Test
	public void testGetFieldAtIndex_Second() {
		Assert.assertEquals("department", StringUtil.getFieldAtIndex(path, 1));
	}
	
	@Test
	public void testGetFieldAtIndex_Third() {
		Assert.assertEquals("person", StringUtil.getFieldAtIndex(path, 2));
	}
	
	@Test
	public void testGetFieldAtIndex_Fourth() {
		Assert.assertEquals("firstName", StringUtil.getFieldAtIndex(path, 3));
	}
	
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void testGetFieldAtIndex_LessThanTotalLength() {
		path = "company.department.person";
		Assert.assertEquals("firstName", StringUtil.getFieldAtIndex(path, 3));
	}
	
	@Test 
	public void testGetFieldAtIndex_Single() {
		path = "company";
		Assert.assertEquals("company", StringUtil.getFieldAtIndex(path, 3));
	}
	
	@Test 
	public void testGetFieldAtIndex_Empty() {
		path = "";
		Assert.assertEquals("", StringUtil.getFieldAtIndex(path, 3));
	}
	
	@Test (expected = NullPointerException.class)
	public void testGetFieldAtIndex_Null() {
		path = null;
		Assert.assertEquals("firstName", StringUtil.getFieldAtIndex(path, 3));
	}
}
