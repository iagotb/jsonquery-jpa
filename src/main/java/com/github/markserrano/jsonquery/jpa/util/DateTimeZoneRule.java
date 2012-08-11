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

import org.joda.time.DateTimeZone;
import org.junit.rules.MethodRule;
import org.junit.runners.model.*;

import com.github.markserrano.jsonquery.jpa.util.DateTimeZoneModifier;

/**
 * Rule that sets the TimeZone
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 * @modification Mark Serrano (check for more info: <a href="http://joda-time.sourceforge.net/timezones.html">Joda Time Zones</a>)
 */
public class DateTimeZoneRule implements MethodRule {

  protected final DateTimeZone zone;

  public DateTimeZoneRule() throws IllegalArgumentException {
    this( "America/New_York" );
  }

  public DateTimeZoneRule( String zoneId ) throws IllegalArgumentException {
    this( DateTimeZone.forID( zoneId ) );
  }

  public DateTimeZoneRule( DateTimeZone zone ) {
    this.zone = zone;
  }

  private DateTimeZone oldTimeZone;

  @Override
  public Statement apply( final Statement statement, final FrameworkMethod frameworkMethod, Object target ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
        	DateTimeZoneModifier annotation = frameworkMethod.getAnnotation(DateTimeZoneModifier.class);
        	if (annotation == null) {
        		statement.evaluate();
        	} else {
        	    DateTimeZone.setDefault( DateTimeZone.forID(annotation.value()) );
        	}
        	
        } finally {
          after();
        }
      }
    };
  }

  private void before() {
    oldTimeZone = DateTimeZone.getDefault();
    DateTimeZone.setDefault( zone );
  }

  private void after() {
    DateTimeZone.setDefault( oldTimeZone );
  }

  public DateTimeZone getZone() {
    return zone;
  }

  public DateTimeZone getOldTimeZone() {
    if ( oldTimeZone == null ) {
      throw new IllegalStateException( "No old zone set" );
    }
    return oldTimeZone;
  }
}

