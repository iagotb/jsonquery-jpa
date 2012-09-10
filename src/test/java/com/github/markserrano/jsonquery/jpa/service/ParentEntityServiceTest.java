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
import com.github.markserrano.jsonquery.jpa.util.QueryUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.PathBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration (transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ParentEntityServiceTest {

	@Autowired
	private IFilterService<Parent> service;
	private JsonBooleanBuilder jsonBooleanBuilder = new JsonBooleanBuilder(Parent.class);
	private JsonBooleanBuilder childBooleanBuilder = new JsonBooleanBuilder(Child.class);
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
	public void testReadAndCount_WhenNoFilterPresent() {
		String filter = QueryUtil.init();

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		
		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(4, results.getTotalElements());
		Assert.assertEquals( "StoreA", results.getContent().get(0).getStore() );
		Assert.assertEquals( "StoreB", results.getContent().get(1).getStore() );
		Assert.assertEquals( "StoreC", results.getContent().get(2).getStore() );
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
	
	@Test 
	public void testReadAndCount_UsingJoins_WhenParentIsNotFiltered_ButChildIsFiltered() {
		// Scenario 1
		String filter = QueryUtil.init();
		String childFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"age\",\"op\":\"gt\",\"data\":\"1\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		BooleanBuilder childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));

		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(6, results.getTotalElements());
		
		// Scenario 2
		childFilter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}]" +
				"}";
		childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));
		results = service.readAndCount(builder, new PageRequest(0,3), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.getTotalElements());
	}
	
	@Test 
	public void testReadAndCount_UsingJoins_WhenParentAndChildAreNotFiltered() {
		String filter = QueryUtil.init();
		String childFilter = QueryUtil.init();

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		BooleanBuilder childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));

		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,10), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(4, results.getTotalElements());
		Assert.assertEquals(1, results.getContent().get(0).getId().intValue());
		Assert.assertEquals(2, results.getContent().get(1).getId().intValue());
		Assert.assertEquals(3, results.getContent().get(2).getId().intValue());
		Assert.assertEquals(4, results.getContent().get(3).getId().intValue());
	}
	
	@Test 
	public void testReadAndCount_UsingJoins_WhenParentIsFiltered_AndChildIsFiltered() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"field\":\"store\",\"op\":\"ne\",\"data\":\"StoreB\"}]" +
				"}";
		String childFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"age\",\"op\":\"gt\",\"data\":\"18\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		BooleanBuilder childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));

		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,10), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals(1, results.getContent().get(0).getId().intValue());
		Assert.assertEquals(1, results.getContent().get(1).getId().intValue());
		Assert.assertEquals(4, results.getContent().get(2).getId().intValue());
	}
	
	@Test 
	public void testReadAndCount_UsingJoins_WhenParentIsFiltered_AndChildIsNotFiltered() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"field\":\"store\",\"op\":\"ne\",\"data\":\"StoreB\"}]" +
				"}";
		String childFilter = QueryUtil.init();

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		BooleanBuilder childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));

		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,10), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals(1, results.getContent().get(0).getId().intValue());
		Assert.assertEquals(3, results.getContent().get(1).getId().intValue());
		Assert.assertEquals(4, results.getContent().get(2).getId().intValue());
	}
	
	@Test 
	public void testReadAndCount_UsingJoins_WhenParentIsFiltered_AndChildIsFiltered_AndPaged() {
		// Scenario 1
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"field\":\"store\",\"op\":\"ne\",\"data\":\"StoreB\"}]" +
				"}";
		String childFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"age\",\"op\":\"gt\",\"data\":\"18\"}]" +
		"}";

		BooleanBuilder builder = jsonBooleanBuilder.build(new JsonFilter(filter));
		BooleanBuilder childBuilder = childBooleanBuilder.build(new JsonFilter(childFilter));
		
		Page<Parent> results = service.readAndCount(builder, new PageRequest(0,2), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals(1, results.getContent().get(0).getId().intValue());
		Assert.assertEquals(1, results.getContent().get(1).getId().intValue());
		
		// Scenario 2
		results = service.readAndCount(builder, new PageRequest(1,2), Parent.class, orderSpecifier, childBuilder, "children", Child.class);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.getTotalElements());
		Assert.assertEquals(4, results.getContent().get(0).getId().intValue());
	}
}
