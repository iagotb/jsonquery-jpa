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

package com.github.markserrano.jsonquery.jpa.builder;


import java.text.ParseException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.mysema.query.BooleanBuilder;

public class JsonBooleanBuilderOperatorTest {

	private JsonBooleanBuilder jsonBooleanBuilder;
	
	@Before
	public void setUp() throws Exception {
		jsonBooleanBuilder = new JsonBooleanBuilder(Child.class);
	}

	@After
	public void tearDown() throws Exception {
		jsonBooleanBuilder = null;
	}

	@Test
	public void testBuild_Eq() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"eq\",\"data\":\"Jane Adams\"}" +
		"]}";
		
		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name = Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Ne() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"ne\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name != Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Lt() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"lt\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name < Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Gt() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"gt\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name > Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Le() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"le\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name <= Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Ge() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"ge\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("child.name >= Jane Adams", builder.toString());
	}
	
	@Test
	public void testBuild_Ew() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"ew\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("endsWith(child.name,Jane Adams)", builder.toString());
	}
	
	@Test
	public void testBuild_Bw() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("startsWith(child.name,Jane Adams)", builder.toString());
	}
	
	@Test
	public void testBuild_Cn() throws ParseException {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[" +
		"{\"field\":\"name\",\"op\":\"cn\",\"data\":\"Jane Adams\"}" +
		"]}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Assert.assertEquals("contains(child.name,Jane Adams)", builder.toString());
	}
}
