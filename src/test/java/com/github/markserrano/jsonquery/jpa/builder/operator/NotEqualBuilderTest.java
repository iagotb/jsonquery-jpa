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

package com.github.markserrano.jsonquery.jpa.builder.operator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.builder.operator.NotEqualBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.mysema.query.BooleanBuilder;

public class NotEqualBuilderTest {

	private BooleanBuilder booleanBuilder; 
	private JsonFilter.Rule rule;
	
	@Before
	public void setUp() throws Exception {
		booleanBuilder = new BooleanBuilder();
		rule = new JsonFilter.Rule(); 
	}

	@After
	public void tearDown() throws Exception {
		booleanBuilder = null;
		rule = null;
	}

	@Test
	public void testGet() {
		rule = new JsonFilter.Rule("and", "name", "ne", "Mike Myers"); 
		Assert.assertEquals("child.name != Mike Myers", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test
	public void testGet_EmptyData() {
		rule = new JsonFilter.Rule("and", "name", "ne", ""); 
		Assert.assertEquals("child.name != ", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = NullPointerException.class)
	public void testGet_IncorrectOperator() {
		rule = new JsonFilter.Rule("and", "name", "eq", "Mike Myers"); 
		Assert.assertEquals("startsWith(child.name,Mik)", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = RuntimeException.class)
	public void testGet_IncorrectField() {
		rule = new JsonFilter.Rule("and", "age", "ne", "Mike Myers"); 
		Assert.assertEquals("startsWith(child.name,Mik)", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsWord_True() {
		rule = new JsonFilter.Rule("and", "married", "ne", "true"); 
		Assert.assertEquals("child.married != true", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsWord_False() {
		rule = new JsonFilter.Rule("and", "married", "ne", "false"); 
		Assert.assertEquals("child.married != false", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsNumber_1() {
		rule = new JsonFilter.Rule("and", "married", "ne", "1"); 
		Assert.assertEquals("child.married != false", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsNumber_0() {
		rule = new JsonFilter.Rule("and", "married", "ne", "0"); 
		Assert.assertEquals("child.married != false", NotEqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
}
