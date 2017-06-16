/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.spring.core.env;

import org.particleframework.core.reflect.ClassUtils;
import org.springframework.core.env.PropertyResolver;

import java.util.Optional;

/**
 * Adapts a {@link org.particleframework.config.PropertyResolver} to a Spring {@link org.springframework.core.env.PropertyResolver}
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public class PropertyResolverAdapter implements PropertyResolver {

    final org.particleframework.config.PropertyResolver target;

    public PropertyResolverAdapter(org.particleframework.config.PropertyResolver target) {
        this.target = target;
    }

    @Override
    public boolean containsProperty(String key) {
        return target.getProperty(key, String.class).isPresent();
    }

    @Override
    public String getProperty(String key) {
        return target.getProperty(key, String.class).orElse(null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return target.getProperty(key, String.class, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return target.getProperty(key, targetType).orElse(null);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return target.getProperty(key, targetType, defaultValue);
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        Optional<String> property = target.getProperty(key, String.class);
        if(property.isPresent()) {
            Optional<Class> aClass = ClassUtils.forName(key, Thread.currentThread().getContextClassLoader());
            if(aClass.isPresent()) {
                return aClass.get();
            }
        }
        return null;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return target.getProperty(key, String.class).orElseThrow(()-> new IllegalStateException("Property ["+key+"] not found"));
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return target.getProperty(key, targetType).orElseThrow(()-> new IllegalStateException("Property ["+key+"] not found"));
    }

    @Override
    public String resolvePlaceholders(String text) {
        throw new UnsupportedOperationException("Method resolvePlaceholders(..) not supported");
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method resolvePlaceholders(..) not supported");
    }
}
