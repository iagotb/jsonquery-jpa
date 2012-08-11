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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.builder.JunctionBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.mapper.JsonObjectMapper;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;

public class JunctionBuilderTest {

	private BooleanBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		builder = new BooleanBuilder();
	}

	@After
	public void tearDown() throws Exception {
		builder = null;
	}

	@Test
	public void testGetBuilder_And_Long() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		
		Assert.assertEquals("child.id = 1", JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule).toString());
	}
	
	@Test
	public void testGetBuilder_And_Long_String() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath path = entityPath.get( new StringPath(srule.getField()) );
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		Assert.assertEquals("child.id = 1 && child.name = Jane Adams", JunctionBuilder.getBuilder(path.eq(srule.getData()), builder, srule).toString());
	}
	
	@Test
	public void testGetBuilder_And_Long_String_Integer() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"eq\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		JsonFilter.Rule nrule = jqgridFilter.getRules().get(2);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath spath = entityPath.get( new StringPath(srule.getField()) );
		NumberPath<Integer> npath = entityPath.get(new NumberPath<Integer>(Integer.class, nrule.getField())); 
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		JunctionBuilder.getBuilder(spath.eq(srule.getData()), builder, srule);
		Assert.assertEquals("child.id = 1 && child.name = Jane Adams && child.age = 20", JunctionBuilder.getBuilder(npath.eq( Integer.valueOf(nrule.getData()) ), builder, nrule).toString());
	}
	
	@Test
	public void testGetBuilder_Or_Long() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"junction\":\"or\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		
		Assert.assertEquals("child.id = 1", JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule).toString());
	}

	@Test
	public void testGetBuilder_Or_Long_String() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"junction\":\"or\",\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath path = entityPath.get( new StringPath(srule.getField()) );
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		Assert.assertEquals("child.id = 1 || child.name = Jane Adams", JunctionBuilder.getBuilder(path.eq(srule.getData()), builder, srule).toString());
	}
	
	@Test
	public void testGetBuilder_Or_Long_String_Integer() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"eq\",\"data\":\"Jane Adams\"}," +
		"{\"junction\":\"or\",\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		JsonFilter.Rule nrule = jqgridFilter.getRules().get(2);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath spath = entityPath.get( new StringPath(srule.getField()) );
		NumberPath<Integer> npath = entityPath.get(new NumberPath<Integer>(Integer.class, nrule.getField())); 
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		JunctionBuilder.getBuilder(spath.eq(srule.getData()), builder, srule);
		Assert.assertEquals("child.id = 1 && child.name = Jane Adams || child.age = 20", JunctionBuilder.getBuilder(npath.eq( Integer.valueOf(nrule.getData()) ), builder, nrule).toString());
	}
	
	@Test
	public void testGetBuilder_ExplicitAnd_Long() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		
		Assert.assertEquals("child.id = 1", JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule).toString());
	}
	
	@Test
	public void testGetBuilder_ExplicitAnd_Long_String() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"junction\":\"and\",\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath path = entityPath.get( new StringPath(srule.getField()) );
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		Assert.assertEquals("child.id = 1 && child.name = Jane Adams", JunctionBuilder.getBuilder(path.eq(srule.getData()), builder, srule).toString());
	}
	
	@Test
	public void testGetBuilder_ExplicitAnd_Long_String_Integer() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"eq\",\"data\":\"Jane Adams\"}," +
		"{\"junction\":\"and\",\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		JsonFilter jqgridFilter = JsonObjectMapper.map(filter);
		JsonFilter.Rule lrule = jqgridFilter.getRules().get(0);
		JsonFilter.Rule srule = jqgridFilter.getRules().get(1);
		JsonFilter.Rule nrule = jqgridFilter.getRules().get(2);
		
		PathBuilder<Child> entityPath = new PathBuilder<Child>(Child.class, "child");
		NumberPath<Long> lpath = entityPath.get( new NumberPath<Long>(Long.class, lrule.getField()) );
		StringPath spath = entityPath.get( new StringPath(srule.getField()) );
		NumberPath<Integer> npath = entityPath.get(new NumberPath<Integer>(Integer.class, nrule.getField())); 
		
		JunctionBuilder.getBuilder(lpath.eq( Long.valueOf(lrule.getData()) ), builder, lrule);
		JunctionBuilder.getBuilder(spath.eq(srule.getData()), builder, srule);
		Assert.assertEquals("child.id = 1 && child.name = Jane Adams && child.age = 20", JunctionBuilder.getBuilder(npath.eq( Integer.valueOf(nrule.getData()) ), builder, nrule).toString());
	}
}
