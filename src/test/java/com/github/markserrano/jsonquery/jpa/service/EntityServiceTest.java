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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.markserrano.jsonquery.jpa.domain.Parent;
import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;
import com.github.markserrano.jsonquery.jpa.specifier.Order;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration (transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class EntityServiceTest {

	@Autowired
	private IEntityService<Parent> service;
	
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
	public void testFindOne_SingleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.eq(1L);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId);
		
		Assert.assertNotNull(service.findOne(builder, Parent.class));
		Assert.assertEquals("StoreA", service.findOne(builder, Parent.class).getStore());
	}
	
	@Test
	public void testFindOne_DoubleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.eq(1L);
		
		StringPath spath = entityPath.get(new StringPath("store"));
		BooleanExpression hasStore = spath.eq("StoreA");
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId).and(hasStore);
		
		Assert.assertNotNull(service.findOne(builder, Parent.class));
		Assert.assertEquals("StoreA", service.findOne(builder, Parent.class).getStore());
	}
	
	
	@Test
	public void testCount_SingleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId);
		
		Assert.assertEquals(new Long(3).longValue(), service.count(builder, Parent.class, orderSpecifier).longValue());
	}
	
	@Test
	public void testCount_DoubleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);

		StringPath spath = entityPath.get(new StringPath("store"));
		BooleanExpression hasStore = spath.ne("StoreB");
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId).and(hasStore);
		
		Assert.assertEquals(new Long(2).longValue(), service.count(builder, Parent.class, orderSpecifier).longValue());
	}
	
	@Test
	public void testRead_SingleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId);
		
		Assert.assertEquals(new Integer(2).longValue(), service.read(builder, new PageRequest(0,2), Parent.class, orderSpecifier).size());
	}
	
	@Test
	public void testRead_DoubleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);

		StringPath spath = entityPath.get(new StringPath("store"));
		BooleanExpression hasStore = spath.ne("StoreB");
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId).and(hasStore);
		
		Assert.assertEquals(new Integer(2).longValue(), service.read(builder, new PageRequest(0,2), Parent.class, orderSpecifier).size());
	}
	
	@Test
	public void testReadAndCount_SingleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId);

		Assert.assertEquals(new Integer(2).longValue(), service.readAndCount(builder, new PageRequest(0,2), Parent.class, orderSpecifier).getNumberOfElements());
		Assert.assertEquals(new Integer(3).longValue(), service.readAndCount(builder, new PageRequest(0,2), Parent.class, orderSpecifier).getTotalElements());
	}
	
	@Test
	public void testReadAndCount_DoubleCriteria() {
		PathBuilder<Parent> entityPath = new PathBuilder<Parent>(Parent.class, "parent");
		
		NumberPath<Long> npath = entityPath.get(new NumberPath<Long>(Long.class, "id"));
		BooleanExpression hasId = npath.gt(1L);

		StringPath spath = entityPath.get(new StringPath("store"));
		BooleanExpression hasStore = spath.ne("StoreB");
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(hasId).and(hasStore);
		
		Assert.assertEquals(new Integer(2).longValue(), service.readAndCount(builder, new PageRequest(0,2), Parent.class, orderSpecifier).getTotalElements());
	}

}
