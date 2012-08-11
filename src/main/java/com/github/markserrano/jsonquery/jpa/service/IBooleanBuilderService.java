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

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;

/**
 * 
 * @author Mark Anthony L. Serrano
 */
public interface IBooleanBuilderService<T extends Serializable> {

	public T find(BooleanBuilder builder, Class<T> clazz);
	public Page<T> readAndCount(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order); 
	public List<T> read(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order);
	public Long count(BooleanBuilder builder, Class<T> clazz, OrderSpecifier order);

}