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

import com.github.markserrano.jsonquery.jpa.enumeration.JunctionEnum;

@RunWith(Parameterized.class)
public class JunctionEnumTest {

	private int value;
	private String description;
	private JunctionEnum junctionEnum;
	
	@Parameters
	public static Collection<?> data() {
		return Arrays.asList(new Object[][] { 
				{ 0, "and", JunctionEnum.AND },
				{ 1, "or", JunctionEnum.OR }
		});
	}
     
	public JunctionEnumTest(int value, String description, JunctionEnum junctionEnum) {
		this.value = value;
		this.description = description;
		this.junctionEnum = junctionEnum;
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetValue() {
		Assert.assertEquals(value, junctionEnum.getValue().intValue());
	}

	@Test
	public void testGetDescription() {
		Assert.assertEquals(description, junctionEnum.getDescription());
	}

	@Test
	public void testGetEnumInt() {
		Assert.assertEquals(junctionEnum, JunctionEnum.getEnum(value));
	}
	
	@Test
	public void testGetEnumString() {
		Assert.assertEquals(junctionEnum, JunctionEnum.getEnum(description));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_True() {
		Assert.assertEquals(junctionEnum, JunctionEnum.getEnum(description, true));
	}
	
	@Test
	public void testGetEnumString_CaseSensitive_False() {
		Assert.assertEquals(junctionEnum, JunctionEnum.getEnum(description, false));
	}

}
