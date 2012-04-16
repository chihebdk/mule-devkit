/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mule.devkit.it.studio;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;

import java.util.List;
import java.util.Map;

/**
 * Collection module
 *
 * @author MuleSoft, Inc.
 */
@Module(name = "collection")
@SuppressWarnings("unchecked")
public class CollectionModule {
    /**
     * Configurable strings
     */
    @Configurable
    @Optional
    private List<String> strings;

    /**
     * Configurable items
     */
    @Configurable
    @Optional
    private List items;

    /**
     * Configurable map of strings
     */
    @Configurable
    @Optional
    private Map<String, String> mapStrings;

    /**
     * Configurable list of strings
     */
    @Configurable
    @Optional
    private Map mapItems;

    /**
     * Method that accepts a List<String>
     *
     * @param strings a list of strings
     * @return bla
     */
    @Processor
    public List<String> operation1(List<String> strings) {
        return strings;
    }

    /**
     * Method that accepts a Map<String, String>
     *
     * @param mapStrings a map of strings
     * @return bla
     */
    @Processor
    public Map<String, String> operation2(Map<String, String> mapStrings) {
        return mapStrings;
    }

    /**
     * Method that accepts a Map<String, Object>
     *
     * @param mapObjects a map of string-object
     * @return bla
     */
    @Processor
    public Map<String, Object> operation3(Map<String, Object> mapObjects) {
        return mapObjects;
    }

    /**
     * Method that accepts a raw Map
     *
     * @param properties a raw map
     * @return bla
     */
    @Processor
    public Map operation4(Map properties) {
        return properties;
    }

    /**
     * Method that accepts a raw List
     *
     * @param list a list
     * @return bla
     */
    @Processor
    public List operation5(List list) {
        return list;
    }

    /**
     * Method that accepts a List<Map<String, Strin>>
     *
     * @param objects a list of maps
     * @return bla
     */
    @Processor
    public List<Map<String, String>> operation6(List<Map<String, String>> objects) {
        return objects;
    }

    public void setStrings(List strings) {
        this.strings = strings;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setMapStrings(Map mapStrings) {
        this.mapStrings = mapStrings;
    }

    public void setMapItems(Map<String, String> mapItems) {
        this.mapItems = mapItems;
    }
}