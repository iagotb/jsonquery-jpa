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

import com.github.markserrano.jsonquery.jpa.enumeration.OperatorEnum;

@RunWith(Parameterized.class)
public class OperatorEnumTest {

	private int value;
	private String description;
	private OperatorEnum operatorEnum;
	
	@Parameters
	public static Collection<?> data() {
		return Arrays.asList(new Object[][] { 
				{ 0, "eq", OperatorEnum.EQUAL },
				{ 1, "ne", OperatorEnum.NOT_EQUAL },
				{ 2, "lt", OperatorEnum.LESS_THAN },
				{ 3, "gt", OperatorEnum.GREATER_THAN },
				{ 4, "ge", OperatorEnum.GREATER_EQUAL },
				{ 5, "le", OperatorEnum.LESSER_EQUAL },
				{ 6, "ew", OperatorEnum.ENDS_WITH },
				{ 7, "bw", OperatorEnum.BEGINS_WITH },
				{ 8, "cn", OperatorEnum.CONTAINS }
		});
	}
     
	public OperatorEnumTest(int value, String description, OperatorEnum operatorEnum) {
		this.value = value;
		this.description = description;
		this.operatorEnum = operatorEnum;
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetValue() {
		Assert.assertEquals(value, operatorEnum.getValue().intValue());
	}

	@Test
	public void testGetDescription() {
		Assert.assertEquals(description, operatorEnum.getDescription());
	}

	@Test
	public void testGetEnumInt() {
		Assert.assertEquals(operatorEnum, OperatorEnum.getEnum(value));
	}
	
	@Test
	public void testGetEnumString() {
		Assert.assertEquals(operatorEnum, OperatorEnum.getEnum(description));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_True() {
		Assert.assertEquals(operatorEnum, OperatorEnum.getEnum(description, true));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_False() {
		Assert.assertEquals(operatorEnum, OperatorEnum.getEnum(description, false));
	}

}
