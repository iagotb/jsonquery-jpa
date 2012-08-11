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
import com.github.markserrano.jsonquery.jpa.domain.Parent;
import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.github.markserrano.jsonquery.jpa.specifier.Order;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration (transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ParentEntityServiceTest {

	@Autowired
	private IEntityService<Parent> service;
	private JsonBooleanBuilder jsonBooleanBuilder = new JsonBooleanBuilder(Parent.class);
	private Order order = new Order(Parent.class);
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
	public void testReadAndCount_Jqgrid_SingleCriteria() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"gt\",\"data\":\"1\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals( "StoreB", results.getContent().get(0).getStore() );
		Assert.assertEquals( "StoreC", results.getContent().get(1).getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(2).getStore() );
	}
	
	@Test 
	public void testReadAndCount_Jqgrid_DoubleCriteria() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"gt\",\"data\":\"1\"}," +
		"{\"field\":\"store\",\"op\":\"ne\",\"data\":\"StoreC\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.getTotalElements());
		Assert.assertEquals( "StoreB", results.getContent().get(0).getStore() );
		Assert.assertEquals( "StoreD", results.getContent().get(1).getStore() );
	}
	
	
	@Test 
	public void testReadAndCount_Jqgrid_DoubleCriteria_Empty() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"gt\",\"data\":\"5\"}," +
		"{\"field\":\"store\",\"op\":\"ne\",\"data\":\"StoreC\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.getTotalElements());
	}

}
