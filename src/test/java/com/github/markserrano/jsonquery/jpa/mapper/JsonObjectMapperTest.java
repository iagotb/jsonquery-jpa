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

package com.github.markserrano.jsonquery.jpa.mapper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.mapper.JsonObjectMapper;

public class JsonObjectMapperTest {

	private String filterSingle;
	private String filterMultiple;
	
	@Before
	public void setUp() throws Exception {
		filterSingle = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"field\":\"firstName\",\"op\":\"cn\",\"data\":\"Jane\"}]}";
		
		filterMultiple = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"firstName\",\"op\":\"cn\",\"data\":\"Jane\"}," +
		"{\"field\":\"lastName\",\"op\":\"ew\",\"data\":\"Adams\"}," +
		"{\"field\":\"age\",\"op\":\"eq\",\"data\":\"20\"}]" +
		"}";
	}

	@After
	public void tearDown() throws Exception {
		filterSingle = null;
		filterMultiple = null;
	}

	@Test
	public void testmap_Single() {
		Assert.assertEquals("firstName",JsonObjectMapper.map(filterSingle).getRules().get(0).getField());
		Assert.assertEquals("cn",JsonObjectMapper.map(filterSingle).getRules().get(0).getOp());
		Assert.assertEquals("Jane",JsonObjectMapper.map(filterSingle).getRules().get(0).getData());
	}
	
	@Test
	public void testmap_Multiple() {
		Assert.assertEquals(3, JsonObjectMapper.map(filterMultiple).getRules().size());
		
		Assert.assertEquals("firstName",JsonObjectMapper.map(filterMultiple).getRules().get(0).getField());
		Assert.assertEquals("cn",JsonObjectMapper.map(filterMultiple).getRules().get(0).getOp());
		Assert.assertEquals("Jane",JsonObjectMapper.map(filterMultiple).getRules().get(0).getData());
		
		Assert.assertEquals("lastName",JsonObjectMapper.map(filterMultiple).getRules().get(1).getField());
		Assert.assertEquals("ew",JsonObjectMapper.map(filterMultiple).getRules().get(1).getOp());
		Assert.assertEquals("Adams",JsonObjectMapper.map(filterMultiple).getRules().get(1).getData());

		Assert.assertEquals("age",JsonObjectMapper.map(filterMultiple).getRules().get(2).getField());
		Assert.assertEquals("eq",JsonObjectMapper.map(filterMultiple).getRules().get(2).getOp());
		Assert.assertEquals("20",JsonObjectMapper.map(filterMultiple).getRules().get(2).getData());
	}

	@Test
	public void testmap_Null() {
		Assert.assertNull(JsonObjectMapper.map(null));
	}
	
	@Test (expected = RuntimeException.class)
	public void testmap_InvalidJson() {
		String filterInvalid = "{\"groupOp\":\"AND\",\"rules\":" +
				"[//{\"field\":\"firstName\",\"op\":\"cn\",\"data\":\"Jane\"}]]}";
		
		Assert.assertNull(JsonObjectMapper.map(filterInvalid));
	}
}
