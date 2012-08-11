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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.markserrano.jsonquery.jpa.util.DateTimeUtil;
import com.github.markserrano.jsonquery.jpa.util.DateTimeZoneRule;

public class DateTimeTest {
	
	private String literal;
	
	@Before
	public void setUp() throws Exception {
		literal = "1992-09-29T00:00:00.000Z";
	}

	@After
	public void tearDown() throws Exception {
		literal = null;
	}
	
	@Rule
	public DateTimeZoneRule dateTimeZoneRule = new DateTimeZoneRule("Asia/Manila");

	@Test
	public void testStringToDateTime() {
		Assert.assertEquals("1992-09-29T08:00:00.000+08:00", new DateTime(literal).toString());
	}
	
	@Test
	public void testStringToDateTime_WithTimezoneOffset() {
		TimeZone timeZone = new DateTime().getZone().toTimeZone();
		Assert.assertEquals("1992-09-29T08:00:00.000+08:00", 
				new DateTime(literal).withZone(DateTimeZone.forTimeZone(timeZone)).toString());
	}
	
	@Test
	public void testStringToDateTime_WithTimezoneOffset_AndNoOffset() {
		TimeZone timeZone = new DateTime().getZone().toTimeZone();
		Assert.assertEquals(new DateTime(literal).toString(), 
				new DateTime(literal).withZone(DateTimeZone.forTimeZone(timeZone)).toString());
	}
	
	@Test
	public void testStringToDateTime_ForSpecifiedTimezoneOffset_Plus() {
		Assert.assertEquals("1992-09-29T05:00:00.000+05:00", 
				new DateTime(literal).withZone(DateTimeZone.forOffsetHours(5)).toString());
	}
	
	@Test
	public void testStringToDateTime_ForSpecifiedTimezoneOffset_Minus() {
		Assert.assertEquals("1992-09-28T19:00:00.000-05:00", 
				new DateTime(literal).withZone(DateTimeZone.forOffsetHours(-5)).toString());
	}
	
	@Test
	public void testGetYesterdaySpan() {
		Map<String, DateTime> map = DateTimeUtil.getYesterdaySpan();
		
		DateTime now = new DateTime();
		DateTime from = now.minusDays(1)
				.minusHours(now.getHourOfDay())
				.minusMinutes(now.getMinuteOfHour())
				.minusSeconds(now.getSecondOfMinute())
				.minusMillis(now.getMillisOfSecond());
		DateTime to = from.plusDays(1).minusSeconds(1);
		
		Assert.assertEquals(from.toString(), map.get("from").toString());
		Assert.assertEquals(to.toString(), map.get("to").toString());
	}

	@Test
	public void testExtractDateAsString() {
		Assert.assertEquals("2012-07-02T00:00:00.000Z", 
				DateTimeUtil.getDateTimeAsString("{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"createDate\",\"op\":\"eq\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}", 
						"createDate"));
	}
	
	@Test
	public void testExtractDateAsDateTime() {
		Assert.assertEquals(new DateTime("2012-07-02T00:00:00.000Z").toString(), 
				DateTimeUtil.getDateTime("{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"createDate\",\"op\":\"eq\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}", 
						"createDate").toString());
	}
	
	@Test
	public void testModifyDateAsRange() {
		Assert.assertEquals(
				"{\"groupOp\":\"AND\",\"rules\":[{\"junction\":\"and\",\"field\":\"createDate\",\"op\":\"le\",\"data\":\"2012-07-03T07:59:59.000+08:00\"}," +
				"{\"field\":\"createDate\",\"op\":\"ge\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}", 
				DateTimeUtil.toDateRangeQuery("{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"createDate\",\"op\":\"eq\",\"data\":\"2012-07-02T00:00:00.000Z\"}]}", 
						"createDate"));
	}

}
