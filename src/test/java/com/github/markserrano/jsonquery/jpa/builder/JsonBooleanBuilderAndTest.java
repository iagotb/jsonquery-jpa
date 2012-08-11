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

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.mapper.JsonObjectMapper;
import com.github.markserrano.jsonquery.jpa.util.DateTimeZoneModifier;
import com.github.markserrano.jsonquery.jpa.util.DateTimeZoneRule;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;

public class JsonBooleanBuilderAndTest {

	private JsonBooleanBuilder jsonBooleanBuilder;
	private BooleanBuilder booleanBuilder; 
	
	@Rule
	public DateTimeZoneRule dateTimeZoneRule = new DateTimeZoneRule("Asia/Manila");
	
	private String filterMultiple = "{\"groupOp\":\"AND\",\"rules\":" +
	"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
	"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
	"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
	"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
	"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
	"{\"field\":\"parent.id\",\"op\":\"eq\",\"data\":\"2\"}," +
	"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980 +5\"}" +
	"]}";
	
	@Before
	public void setUp() throws Exception {
		jsonBooleanBuilder = new JsonBooleanBuilder(Child.class);
		booleanBuilder = jsonBooleanBuilder.build(new JsonFilter(filterMultiple));
	}

	@After
	public void tearDown() throws Exception {
		jsonBooleanBuilder = null;
		booleanBuilder = null;
	}
	
	@Test
	public void testBuild_ByJsonFilter() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> path = entityPath.get( new NumberPath<Long>(Long.class, "id") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.eq( Long.valueOf("1") ));
		
		filterMultiple = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}" +
		"]}";
		booleanBuilder = jsonBooleanBuilder.build(new JsonFilter(filterMultiple));

		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("child.id = 1", builder1.toString());
		Assert.assertEquals("child.id = 1", booleanBuilder.toString());
	}
	
	@Test
	public void testBuild_WhenNull_ReturnInit() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> path = entityPath.get( new NumberPath<Long>(Long.class, "id") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.eq( Long.valueOf("1") ));
		
		filterMultiple = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}" +
		"]}";
		booleanBuilder = jsonBooleanBuilder.build(new JsonFilter(null));

		Assert.assertNotNull(booleanBuilder);
	}
	
	@Test
	public void testBuild_Long_Eq() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> path = entityPath.get( new NumberPath<Long>(Long.class, "id") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.eq( Long.valueOf("1") ));
		
		int index = 0;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));

		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("child.id = 1", builder1.toString());
		Assert.assertEquals("child.id = 1", booleanBuilder.toString());
	}
	
	@Test
	public void testBuild_String_Bw() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		StringPath path = entityPath.get( new StringPath("name") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.startsWith("Jane Adams"));

		int index = 1;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));

		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("startsWith(child.name,Jane Adams)", builder1.toString());
		Assert.assertEquals("startsWith(child.name,Jane Adams)", booleanBuilder.toString());	
	}
	
	@Test
	public void testBuild_Integer_Bw() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Integer> path = entityPath.get( new NumberPath<Integer>(Integer.class, "age") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.gt( Integer.valueOf("20") ));

		int index = 2;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));

		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("child.age > 20", builder1.toString());
		Assert.assertEquals("child.age > 20", booleanBuilder.toString());	
	}
	
	@Test
	public void testBuild_Double_Lt() {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Double> path = entityPath.get( new NumberPath<Double>(Double.class, "money") );
		BooleanBuilder builder1 = new BooleanBuilder();
		builder1.and(path.lt( Double.valueOf("2000.75") ));

		int index = 3;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));

		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("child.money < 2000.75", builder1.toString());
		Assert.assertEquals("child.money < 2000.75", booleanBuilder.toString());	
	}
	
	@DateTimeZoneModifier("Asia/Manila")
	@Test
	public void testBuild_Date_Eq() throws ParseException {
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		DatePath<DateTime> path = entityPath.get( new DatePath<DateTime>(DateTime.class, "birthDate") );
		BooleanBuilder builder1 = new BooleanBuilder();

		DateTime dt = new DateTime("1959-09-30T00:00:00.000+08:00");
		builder1.and(path.eq(dt));
		
		int index = 4;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));
		
		Assert.assertEquals(builder1.toString(), booleanBuilder.toString());
		Assert.assertEquals("child.birthDate = 1959-09-30T00:00:00.000+08:00", builder1.toString());
		Assert.assertEquals("child.birthDate = 1959-09-30T00:00:00.000+08:00", booleanBuilder.toString());	
	}
	
	@Test
	public void testBuild_Date_Eq_TimezoneOffset() throws ParseException {
		
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"2011-10-07T00:00:00.000Z\"}" +
		"]}";
		
		int index = 0;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filter).getRules().get(index));
		Assert.assertEquals("child.birthDate = 2011-10-07T00:00:00.000+08:00", booleanBuilder.toString());	
	}
	
	@Test
	public void testBuild_Parent_Eq() {
		int index = 5;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));
		
		Assert.assertNotSame("parent = 2", booleanBuilder.toString());	
	}
	
	@DateTimeZoneModifier("Asia/Manila")
	@Test
	public void testBuild_JavaDate_Eq() {
		int index = 6;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));
		
		Assert.assertEquals("child.creationDate = Fri Nov 30 00:00:00 PHT 8", booleanBuilder.toString());	
	}
	
	@Test (expected = RuntimeException.class)
	public void testBuild_DateButWrongFormat_Eq() {
		int index = 7;
		booleanBuilder = jsonBooleanBuilder.build(new BooleanBuilder(), JsonObjectMapper.map(filterMultiple).getRules().get(index));
		
		Assert.assertEquals("createDate = 01-31-1980 +5", booleanBuilder.toString());	
	}
	
	@DateTimeZoneModifier("Asia/Manila")
	@Test
	public void testBuild() {
		Assert.assertEquals("child.id = 1 && startsWith(child.name,Jane Adams) && child.age > 20 && child.money < 2000.75 && child.birthDate = 1959-09-30T00:00:00.000+08:00 && child.parent.id = 2 && child.creationDate = Fri Nov 30 00:00:00 PHT 8", booleanBuilder.toString());
	}
}
