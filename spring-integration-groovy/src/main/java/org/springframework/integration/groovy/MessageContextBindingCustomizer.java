/*
 * Copyright 2002-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.integration.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.Script;

import java.util.Map;

import org.springframework.integration.Message;
import org.springframework.scripting.groovy.GroovyObjectCustomizer;
import org.springframework.util.Assert;

/**
 * A groovy object customizer used internally by the groovy message processors. Not public because the customizer is not
 * the best API for groovy context binding, but it's what we have in Spring right now.
 * 
 * @since 2.0
 * @author Dave Syer
 * 
 */
class MessageContextBindingCustomizer implements GroovyObjectCustomizer {

	private volatile Message<?> message;
	private final GroovyObjectCustomizer customizer;

	public MessageContextBindingCustomizer() {
		this((GroovyObjectCustomizer) null);
	}

	public MessageContextBindingCustomizer(Map<String, ?> context) {
		this(new MapContextBindingCustomizer(context));
	}

	public MessageContextBindingCustomizer(GroovyObjectCustomizer customizer) {
		this.customizer = customizer;
	}

	public void setMessage(Message<?> message) {
		this.message = message;
	}

	public void customize(GroovyObject goo) {
		Assert.state(goo instanceof Script, "Expected a Script");
		if (customizer != null) {
			customizer.customize(goo);
		}
		if (this.message != null) {
			Binding binding = ((Script) goo).getBinding();
			binding.setVariable("payload", this.message.getPayload());
			binding.setVariable("headers", this.message.getHeaders());
		}
	}
}