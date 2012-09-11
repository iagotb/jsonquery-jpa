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

package com.github.markserrano.jsonquery.jpa.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.enumeration.OperatorEnum;
import com.github.markserrano.jsonquery.jpa.util.QueryUtil;

public class QueryUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddOr_Single() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expected = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"junction\":\"or\",\"field\":\"name1\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		Assert.assertEquals(expected, QueryUtil.addSplitOr(filter, "name"));
	}

	@Test
	public void testAddSplitOr_Double() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expected = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"junction\":\"or\",\"field\":\"name1\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"junction\":\"or\",\"field\":\"money1\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expanded = QueryUtil.addSplitOr(filter, "name");
		expanded = QueryUtil.addSplitOr(expanded, "money");
		Assert.assertEquals(expected, expanded);
	}
	
	@Test
	public void testEscape() {
		Assert.assertEquals("\"firstName\"", QueryUtil.escape("firstName"));
	}
	
	@Test
	public void testAddAnd() {
		String expected = "{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}";
		
		Assert.assertEquals(expected, QueryUtil.addAnd("id", OperatorEnum.EQUAL, "1"));
	}
	
	@Test
	public void testAddAnd_Filter() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expected = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		Assert.assertEquals(expected, QueryUtil.addAnd(filter, "id", OperatorEnum.EQUAL, "1"));
	}
	
	@Test
	public void testAddOr() {
		String expected = "{\"junction\":\"or\",\"field\":\"id\",\"op\":\"ge\",\"data\":\"1\"}";
		
		Assert.assertEquals(expected, QueryUtil.addOr("id", OperatorEnum.GREATER_EQUAL, "1"));
	}

	@Test
	public void testAddOr_Filter() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expected = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"junction\":\"or\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}," +
		"{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		Assert.assertEquals(expected, QueryUtil.addOr(filter, "id", OperatorEnum.EQUAL, "1"));
	}
	
	@Test
	public void testAddOr_WhenInitialFiltersAreEmpty_ThenNull() {
		String expected = "{\"junction\":\"or\",\"field\":\"id\",\"op\":\"ge\",\"data\":\"1\"}";
		
		String filters = "";
		Assert.assertNotSame(expected, QueryUtil.addOr(filters, "id", OperatorEnum.GREATER_EQUAL, "1"));
	}
	
	@Test
	public void testAddOr_WhenInitialFiltersAreInitialized() {
		String expected = "{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"or\",\"field\":\"id\",\"op\":\"ge\",\"data\":\"1\"}]}";

		String filters = "{\"groupOp\":\"AND\",\"rules\":[]}";
		Assert.assertEquals(expected, QueryUtil.addOr(filters, "id", OperatorEnum.GREATER_EQUAL, "1"));
	}
	
	@Test
	public void testAddAnd_WhenInitialFiltersAreEmpty_ThenNull() {
		String expected = "{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}";

		String filters = "";
		Assert.assertNotSame(expected, QueryUtil.addAnd(filters, "id", OperatorEnum.EQUAL, "1"));
	}
	
	@Test
	public void testAddAnd_WhenInitialFiltersAreInitialized() {
		String expected = "{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}]}";

		String filters = "{\"groupOp\":\"AND\",\"rules\":[]}";
		Assert.assertEquals(expected, QueryUtil.addAnd(filters, "id", OperatorEnum.EQUAL, "1"));
	}
	
	@Test
	public void testInit() {
		Assert.assertEquals(QueryUtil.INIT_FILTER, QueryUtil.init());
	}
	
	@Test
	public void testInitFilter_WhenNull_ThenInit() {
		String filter = null;
		Assert.assertEquals(QueryUtil.INIT_FILTER, QueryUtil.initFilter(filter));
	}
	
	@Test
	public void testInitFilter_WhenEmpty_ThenInit() {
		String filter = "";
		Assert.assertEquals(QueryUtil.INIT_FILTER, QueryUtil.initFilter(filter));
	}
	
	@Test
	public void testInitFilter_WhenContainsSingleSpaceOnly_ThenInit() {
		String filter = " ";
		Assert.assertEquals(QueryUtil.INIT_FILTER, QueryUtil.initFilter(filter));
	}

	@Test
	public void testInitFilter_WhenNotEmpty_ThenReturnSameFilter() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"id\",\"op\":\"eq\",\"data\":\"1\"}]}";
		Assert.assertEquals(filter, QueryUtil.initFilter(filter));
	}
	
	@Test
	public void testRemoveAnd() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expected = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";

		Map<String, Object> map = QueryUtil.remove(filter, "age");
		Assert.assertTrue((Boolean) map.get("isFound"));
		Assert.assertEquals("age", map.get("field").toString());
		Assert.assertEquals("gt", map.get("op").toString());
		Assert.assertEquals("20", map.get("data").toString());
		
		Assert.assertEquals(expected, map.get("parentFilter").toString());
	}
	
	@Test
	public void testCreateParentAndChildFilter_SingleFieldRemoval() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expectedParentFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";

		String expectedChildFilter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"junction\":\"and\"," + 
				"\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}" +
				"]}";
		
		List<String> fieldsToRemove = new ArrayList<String>();
		fieldsToRemove.add("age");

		Map<String, String> filters = QueryUtil.createParentAndChildFilter(filter, fieldsToRemove);
		
		Assert.assertEquals(expectedChildFilter, filters.get("childFilter"));
		Assert.assertEquals(expectedParentFilter, filters.get("parentFilter"));
	}
	
	@Test
	public void testCreateParentAndChildFilter_MultipleFieldsRemoval() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";
		
		String expectedParentFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"name\",\"op\":\"bw\",\"data\":\"Jane Adams\"}," +
		"{\"field\":\"money\",\"op\":\"lt\",\"data\":\"2000.75\"}," +
		"{\"field\":\"creationDate\",\"op\":\"eq\",\"data\":\"01-31-1980\"}" +
		"]}";

		String expectedChildFilter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"junction\":\"and\",\"field\":\"birthDate\",\"op\":\"eq\",\"data\":\"1959-09-30T00:00:00.000Z\"}," +
				"{\"junction\":\"and\",\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}" +
				"]}";
		
		List<String> fieldsToRemove = new ArrayList<String>();
		fieldsToRemove.add("age");
		fieldsToRemove.add("birthDate");

		Map<String, String> filters = QueryUtil.createParentAndChildFilter(filter, fieldsToRemove);
		
		Assert.assertEquals(expectedChildFilter, filters.get("childFilter"));
		Assert.assertEquals(expectedParentFilter, filters.get("parentFilter"));
	}
	
	@Test
	public void testCreateParentAndChildFilter_WhenParentFilterIsEmptyButChildIsNotEmpty() {
		String filter = "{\"groupOp\":\"AND\",\"rules\":" +
		"[{\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}" +
		"]}";
		
		String expectedParentFilter = "{\"groupOp\":\"AND\",\"rules\":" +
		"["+
		"]}";

		String expectedChildFilter = "{\"groupOp\":\"AND\",\"rules\":" +
				"[{\"junction\":\"and\",\"field\":\"age\",\"op\":\"gt\",\"data\":\"20\"}" +
				"]}";
		
		List<String> fieldsToRemove = new ArrayList<String>();
		fieldsToRemove.add("age");
		fieldsToRemove.add("birthDate");

		Map<String, String> filters = QueryUtil.createParentAndChildFilter(filter, fieldsToRemove);
		
		Assert.assertEquals(expectedChildFilter, filters.get("childFilter"));
		Assert.assertEquals(expectedParentFilter, filters.get("parentFilter"));
	}
}
