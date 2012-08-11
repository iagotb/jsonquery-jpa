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

import com.github.markserrano.jsonquery.jpa.builder.operator.EqualBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.mysema.query.BooleanBuilder;

public class EqualBuilderTest {

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
		rule = new JsonFilter.Rule("and", "money", "eq", "3000.75"); 
		Assert.assertEquals("child.money = 3000.75", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = NumberFormatException.class)
	public void testGet_EmptyData() {
		rule = new JsonFilter.Rule("and", "money", "eq", ""); 
		Assert.assertEquals("startsWith(child.name,)", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = NullPointerException.class)
	public void testGet_IncorrectOperator() {
		rule = new JsonFilter.Rule("and", "money", "gt", "3000.75"); 
		Assert.assertEquals("startsWith(child.name,Mik)", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = RuntimeException.class)
	public void testGet_IncorrectField() {
		rule = new JsonFilter.Rule("and", "age", "eq", "3000.75"); 
		Assert.assertEquals("startsWith(child.name,Mik)", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsWord_True() {
		rule = new JsonFilter.Rule("and", "married", "eq", "true"); 
		Assert.assertEquals("child.married = true", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsWord_False() {
		rule = new JsonFilter.Rule("and", "married", "eq", "false"); 
		Assert.assertEquals("child.married = false", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsNumber_1() {
		rule = new JsonFilter.Rule("and", "married", "eq", "1"); 
		Assert.assertEquals("child.married = false", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test 
	public void testGet_BooleanAsNumber_0() {
		rule = new JsonFilter.Rule("and", "married", "eq", "0"); 
		Assert.assertEquals("child.married = false", EqualBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
}
