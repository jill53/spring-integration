/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.integration.aggregator;

import java.lang.reflect.Method;

import org.springframework.integration.core.Message;
import org.springframework.integration.handler.MessageMappingMethodInvoker;
import org.springframework.util.Assert;

/**
 * {@link CorrelationStrategy} implementation that works as an adapter to another bean.
 *
 * @author: Marius Bogoevici
 */
public class CorrelationStrategyAdapter implements CorrelationStrategy {

	private final MessageMappingMethodInvoker invoker;


	public CorrelationStrategyAdapter(Object object, String methodName) {
		this.invoker = new MessageMappingMethodInvoker(object, methodName, true);
	}

	public CorrelationStrategyAdapter(Object object, Method method) {
		Assert.notNull(object, "'object' must not be null");
		Assert.notNull(method, "'method' must not be null");
		Assert.isTrue(method.getParameterTypes().length == 1, "Method must accept exactly one parameter");
		Assert.isTrue(!Void.TYPE.equals(method.getReturnType()), "Method return type must not be void");
		this.invoker = new MessageMappingMethodInvoker(object, method);
	}

	public Object getCorrelationKey(Message<?> message) {
		return invoker.processMessage(message);
	}

}
