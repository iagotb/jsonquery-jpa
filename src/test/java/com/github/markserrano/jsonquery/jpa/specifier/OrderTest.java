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

package com.github.markserrano.jsonquery.jpa.specifier;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;
import com.github.markserrano.jsonquery.jpa.specifier.Order;


public class OrderTest {

	private Order order = new Order(Child.class);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet_Long_Asc() {
		Assert.assertEquals("child.id ASC", order.by("id", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_String_Asc() {
		Assert.assertEquals("child.name ASC", order.by("name", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_Integer_Asc() {
		Assert.assertEquals("child.age ASC", order.by("age", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_Double_Asc() {
		Assert.assertEquals("child.money ASC", order.by("money", OrderEnum.ASC).toString() );
	}

	@Test
	public void testGet_DateTime_Asc() {
		Assert.assertEquals("child.birthDate ASC", order.by("birthDate", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_Date_Asc() {
		Assert.assertEquals("child.creationDate ASC", order.by("creationDate", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_Long_Desc() {
		Assert.assertEquals("child.id DESC", order.by("id", OrderEnum.DESC).toString() );
	}
	
	@Test
	public void testGet_String_Desc() {
		Assert.assertEquals("child.name DESC", order.by("name", OrderEnum.DESC).toString() );
	}
	
	@Test
	public void testGet_Integer_Desc() {
		Assert.assertEquals("child.age DESC", order.by("age", OrderEnum.DESC).toString() );
	}
	
	@Test
	public void testGet_Double_Desc() {
		Assert.assertEquals("child.money DESC", order.by("money", OrderEnum.DESC).toString() );
	}

	@Test
	public void testGet_DateTime_Desc() {
		Assert.assertEquals("child.birthDate DESC", order.by("birthDate", OrderEnum.DESC).toString() );
	}
	
	@Test
	public void testGet_Date_Desc() {
		Assert.assertEquals("child.creationDate DESC", order.by("creationDate", OrderEnum.DESC).toString() );
	}
	
	@Test
	public void testGet_DateTime_Asc_False() {
		Assert.assertNotSame("child.birthDate DESC", order.by("birthDate", OrderEnum.ASC).toString() );
	}
	
	@Test
	public void testGet_DateTime_Desc_False() {
		Assert.assertNotSame("child.birthDate DESC", order.by("birthDate", OrderEnum.ASC).toString() );
	}
}
