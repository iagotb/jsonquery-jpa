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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.markserrano.jsonquery.jpa.builder.JsonBooleanBuilder;
import com.github.markserrano.jsonquery.jpa.builder.operator.EqualBuilder;
import com.github.markserrano.jsonquery.jpa.domain.Child;
import com.github.markserrano.jsonquery.jpa.domain.Parent;
import com.github.markserrano.jsonquery.jpa.filter.JsonFilter;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.PathBuilder;

/**
 * 
 * @author Mark Anthony L. Serrano
 */
@Service
public class FilterService<T extends Serializable> implements IFilterService<T> {

	@Autowired
	private EntityManagerFactory emf;
	
	@Override
	public T findOne(BooleanBuilder builder, Class<T> clazz) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;
		
		JPQLQuery result = new JPAQuery(em).from(path).where(builder);
		
		return result.uniqueResult(entityPath);
	}
	
	@Override
	public T find(BooleanBuilder builder, Class<T> clazz) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;
		
		JPQLQuery result = new JPAQuery(em).from(path).where(builder);
		
		return result.uniqueResult(entityPath);
	}
	
	@Override
	public Page<T> readAndCount(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order) {
		Page<T> pageImpl = new PageImpl<T>(read(builder, page, clazz, order), page, count(builder, clazz, order));
		return pageImpl;
	}
	
	@Override
	public Page<T> readAndCount(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
		Page<T> pageImpl = new PageImpl<T>(read(builder, page, clazz, order, joinChildBuilder, joinChildField, joinChildClass), page, count(builder, clazz, order, joinChildBuilder, joinChildField, joinChildClass));
		return pageImpl;
	}
	
	@Override
	public List<T> read(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;
		
		JPQLQuery result = new JPAQuery(em).from(path).where(builder).orderBy(order);
		
		if (page != null) {
			result.offset(page.getOffset());
			result.limit(page.getPageSize());
		}
		
		return result.list(entityPath);
	}
	
	@Override
	public List<T> read(BooleanBuilder builder, Pageable page, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;
		
		if (joinChildBuilder.getValue() != null) {
			String childVariable = joinChildClass.getSimpleName().substring(0, 1).toLowerCase() + joinChildClass.getSimpleName().substring(1);
			PathBuilder<T> joinPath = new PathBuilder<T>(clazz, variable);
			PathBuilder<Object> joinAlias = new PathBuilder<Object>(joinChildClass, childVariable);
			
			EntityPath<T> jPath = (EntityPath<T>) joinPath.get(joinChildField);
			EntityPath<T> jAlias = (EntityPath<T>) joinAlias;
			
			JPQLQuery result = new JPAQuery(em).from(path).join(jPath, jAlias).with(joinChildBuilder).where(builder).orderBy(order);
	
			if (page != null) {
				result.offset(page.getOffset());
				result.limit(page.getPageSize());
			}
			
			return result.list(entityPath);
		} else {
			return read(builder, page, clazz, order);
		}
	}
	
	@Override
	public Long count(BooleanBuilder builder, Class<T> clazz, OrderSpecifier order) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;
		
		JPQLQuery result = new JPAQuery(em).from(path).where(builder).orderBy(order);
		
		return result.count();
	}

	@Override
	public Long count(BooleanBuilder builder, Class<T> clazz, OrderSpecifier order, BooleanBuilder joinChildBuilder, String joinChildField, Class<?> joinChildClass) {
		String variable = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, variable);
		EntityManager em = emf.createEntityManager();
		EntityPath<T> path = entityPath;

		if (joinChildBuilder.getValue() != null) {
			String childVariable = joinChildClass.getSimpleName().substring(0, 1).toLowerCase() + joinChildClass.getSimpleName().substring(1);
			PathBuilder<T> joinPath = new PathBuilder<T>(clazz, variable);
			PathBuilder<Object> joinAlias = new PathBuilder<Object>(joinChildClass, childVariable);
			
			EntityPath<T> jPath = (EntityPath<T>) joinPath.get(joinChildField);
			EntityPath<T> jAlias = (EntityPath<T>) joinAlias;
			
			JPQLQuery result = new JPAQuery(em).from(path).join(jPath, jAlias).with(joinChildBuilder).where(builder).orderBy(order);
			
			return result.count();
		} else {
			return count(builder, clazz, order);
		}
	}
	
	@Override
	public Page<T> read(String filter, Class<T> clazz, Pageable page, OrderSpecifier order) {
		return readAndCount(getJsonBooleanBuilder(clazz).build(new JsonFilter(filter)), page, clazz, order);
	}

	@Override
	public JsonBooleanBuilder getJsonBooleanBuilder(Class<T> clazz) {
		return new JsonBooleanBuilder(clazz);
	}
	
}
