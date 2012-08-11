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

import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.util.PathUtil;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class PathUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPath_String() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "name") instanceof StringPath);
	}

	@Test
	public void testGetPath_Boolean() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "married") instanceof BooleanPath);
	}
	
	@Test
	public void testGetPath_Integer() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "age") instanceof NumberPath);
	}
	
	@Test
	public void testGetPath_Long() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "id") instanceof NumberPath);
	}
	
	@Test
	public void testGetPath_Double() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "money") instanceof NumberPath);
	}
	
	@Test
	public void testGetPath_Date() {
		Assert.assertTrue(PathUtil.getPath(Child.class, "child", "birthDate") instanceof DatePath);
	}
	
	@Test 
	public void testGetPath_Clazz() {
		Assert.assertFalse(PathUtil.getPath(Child.class, "child", "parent.id") instanceof DatePath);
	}
}
