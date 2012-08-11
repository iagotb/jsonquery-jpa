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

import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.domain.GrandChild;
import com.github.markserrano.jsonquery.jpa.util.ClassUtil;


public class ClassUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMatchingType() throws SecurityException, NoSuchFieldException {
		Assert.assertEquals(java.lang.String.class, ClassUtil.getMatchingType( Child.class.getDeclaredField("name"), "name") );
	}
	
	@Test (expected = NoSuchFieldException.class)
	public void testGetMatchingType_NoMatch() throws SecurityException, NoSuchFieldException {
		Assert.assertEquals(java.lang.String.class, ClassUtil.getMatchingType( Child.class.getDeclaredField("dummy"), "name") );
	}

	@Test
	public void testGetType_String() {
		Assert.assertEquals(java.lang.String.class, ClassUtil.getType( Child.class, "name") );
	}

	@Test
	public void testGetType_Long() {
		Assert.assertEquals(Long.class, ClassUtil.getType(Child.class, "id"));
	}
	
	@Test (expected = RuntimeException.class)
	public void testGetType_NoMatch() {
		Assert.assertEquals(java.lang.String.class, ClassUtil.getType( Child.class, "dummy") );
	}
	
	@Test
	public void testGetTypeByParent() {
		Assert.assertNull(ClassUtil.getTypeByParent( Child.class, "name") );
	}

	@Test 
	public void testGetTypeByParent_NoMatch() {
		Assert.assertNull( ClassUtil.getTypeByParent( Child.class, "dummy") );
	}
	
	@Test
	public void testGetType_ByInheritance() {
		Assert.assertEquals(java.lang.Long.class, ClassUtil.getTypeByParent( GrandChild.class, "id") );
	}

	@Test 
	public void testGetType_ByInheritance_NoMatch() {
		Assert.assertNull( ClassUtil.getTypeByParent( GrandChild.class, "dummy") );
	}
	
	@Test
	public void testGetId() {
		Assert.assertEquals("id", ClassUtil.getId( GrandChild.class) );
	}
	
	@Test
	public void testGetIdByParent() {
		Assert.assertEquals("id", ClassUtil.getIdByParent( GrandChild.class) );
	}

	@Test
	public void testGetClassName() {
		Assert.assertEquals("grandChild", ClassUtil.getVariableName(GrandChild.class) );
	}
}
