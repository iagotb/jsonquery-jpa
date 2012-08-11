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

package com.github.markserrano.jsonquery.jpa.enumeration;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.markserrano.jsonquery.jpa.enumeration.OrderEnum;

@RunWith(Parameterized.class)
public class OrderEnumTest {

	private int value;
	private String description;
	private OrderEnum orderEnum;
	
	@Parameters
	public static Collection<?> data() {
		return Arrays.asList(new Object[][] { 
				{ 0, "asc", OrderEnum.ASC },
				{ 1, "desc", OrderEnum.DESC }
		});
	}
     
	public OrderEnumTest(int value, String description, OrderEnum orderEnum) {
		this.value = value;
		this.description = description;
		this.orderEnum = orderEnum;
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetValue() {
		Assert.assertEquals(value, orderEnum.getValue().intValue());
	}

	@Test
	public void testGetDescription() {
		Assert.assertEquals(description, orderEnum.getDescription());
	}

	@Test
	public void testGetEnumInt() {
		Assert.assertEquals(orderEnum, OrderEnum.getEnum(value));
	}
	
	@Test
	public void testGetEnumString() {
		Assert.assertEquals(orderEnum, OrderEnum.getEnum(description));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_True() {
		Assert.assertEquals(orderEnum, OrderEnum.getEnum(description, true));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_False() {
		Assert.assertEquals(orderEnum, OrderEnum.getEnum(description, false));
	}

}
