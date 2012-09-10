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

package com.github.markserrano.jsonquery.jpa.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name="parent")
public class Parent implements Serializable {

	private static final long serialVersionUID = -5882039470272397466L;
	
	@Id
	@GeneratedValue
	private Long id;
	private String store;
	
	@OneToOne
	private Thing thing;
	
	@OneToMany (mappedBy="parent", cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Child> children;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Thing getThing() {
		return thing;
	}
	public void setThing(Thing thing) {
		this.thing = thing;
	}
	
	public List<Child> getChildren() {
		return children;
	}
	public void setChildren(List<Child> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "Parent [id=" + id + ", store=" + store + ", thing=" + thing
				+ "]";
	}
}

