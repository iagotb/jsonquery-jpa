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

import com.github.markserrano.jsonquery.jpa.builder.operator.ContainsBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.mysema.query.BooleanBuilder;

public class ContainsBuilderTest {

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
		rule = new JsonFilter.Rule("and", "name", "cn", "ik"); 
		Assert.assertEquals("contains(child.name,ik)", ContainsBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test
	public void testGet_EmptyData() {
		rule = new JsonFilter.Rule("and", "name", "cn", ""); 
		Assert.assertEquals("contains(child.name,)", ContainsBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = NullPointerException.class)
	public void testGet_IncorrectOperator() {
		rule = new JsonFilter.Rule("and", "name", "ew", "ik"); 
		Assert.assertEquals("startsWith(child.name,Mik)", ContainsBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = RuntimeException.class)
	public void testGet_IncorrectField() {
		rule = new JsonFilter.Rule("and", "age", "cn", "Mik"); 
		Assert.assertEquals("startsWith(child.name,Mik)", ContainsBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
	@Test (expected = RuntimeException.class)
	public void testGet_BooleanAsWord_True() {
		rule = new JsonFilter.Rule("and", "married", "cn", "true"); 
		Assert.assertEquals("child.married = true", ContainsBuilder.get(Child.class, "child", booleanBuilder, rule).toString());
	}
	
}
