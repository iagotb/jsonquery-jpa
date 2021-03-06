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

package com.github.markserrano.jsonquery.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.specifier.Order;
import com.github.markserrano.jsonquery.jpa.util.QueryUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration (transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ParentChildEntityServiceTest {

	@Autowired
	private IFilterService<Child> service;
	private JsonBooleanBuilder jsonBooleanBuilder = new JsonBooleanBuilder(Child.class);
	private Order order = new Order(Child.class);
	private OrderSpecifier<?> orderSpecifier;
	
	@Before
	public void setUp() throws Exception {
		orderSpecifier = order.by("id", OrderEnum.ASC);
	}

	@After
	public void tearDown() throws Exception {
		orderSpecifier = null;
	}

	@Test 
	public void testReadAndCount_Jqgrid_SingleCriteria_ByParent() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.id\",\"op\":\"eq\",\"data\":\"1\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals( "StoreA", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreA", results.getContent().get(1).getParent().getStore() );
		Assert.assertEquals( "StoreA", results.getContent().get(2).getParent().getStore() );
	}
	
	@Test 
	public void testReadAndCount_Jqgrid_DoubleCriteria_ByParent() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.id\",\"op\":\"gt\",\"data\":\"1\"}," +
		"{\"field\":\"birthDate\",\"op\":\"lt\",\"data\":\"1995-10-01T00:00:00.000Z\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.getTotalElements());
		Assert.assertEquals( "StoreB", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(1).getParent().getStore() );
	}
	
	@Test 
	public void testReadAndCount_Jqgrid_DoubleCriteria_ByParentAndId() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.id\",\"op\":\"gt\",\"data\":\"1\"}," +
		"{\"field\":\"id\",\"op\":\"lt\",\"data\":\"3\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.getTotalElements());
	}
	
	@Test 
	public void testReadAndCount_Jqgrid_DoubleCriteria_ByDateTimeAndDate() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"creationDate\",\"op\":\"gt\",\"data\":\"2011-11-15\"}," +
		"{\"field\":\"birthDate\",\"op\":\"lt\",\"data\":\"1995-10-01T00:00:00.000Z\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.getTotalElements());
		Assert.assertEquals( "Jane Adams", results.getContent().get(0).getName() );
		Assert.assertEquals( "StoreB", results.getContent().get(0).getParent().getStore() );
	}
	
	@Test 
	public void testReadAndCount_SingleCriteriaBy_ForeignTable_FieldOtherThanId() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.store\",\"op\":\"ne\",\"data\":\"StoreB\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(5, results.getTotalElements());
		Assert.assertEquals( "StoreA", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreA", results.getContent().get(1).getParent().getStore() );
		Assert.assertEquals( "StoreA", results.getContent().get(2).getParent().getStore() );
	}
	
	@Test 
	public void testReadAndCount_DoubleCriteriaBy_ForeignTable_FieldOtherThanId() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.store\",\"op\":\"ne\",\"data\":\"StoreB\"}," +
		"{\"field\":\"birthDate\",\"op\":\"lt\",\"data\":\"1995-10-01T00:00:00.000Z\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.getTotalElements());
		Assert.assertEquals( "StoreA", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(1).getParent().getStore() );
	}
	
	@Test 
	public void testReadAndCount_SingleCriteriaBy_ForeignTable_FieldById() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.id\",\"op\":\"ne\",\"data\":\"1\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals( "StoreB", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreC", results.getContent().get(1).getParent().getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(2).getParent().getStore() );
	}
	
	@Test (expected = RuntimeException.class)
	public void testReadAndCount_SingleCriteriaBy_ForeignTable_FieldOtherThanId_NoMatch() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"parent.company\",\"op\":\"ne\",\"data\":\"StoreB\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Child> results = service.readAndCount(builder, new PageRequest(0,3), Child.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals( "StoreA", results.getContent().get(0).getParent().getStore() );
		Assert.assertEquals( "StoreC", results.getContent().get(1).getParent().getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(2).getParent().getStore() );
	}

}
