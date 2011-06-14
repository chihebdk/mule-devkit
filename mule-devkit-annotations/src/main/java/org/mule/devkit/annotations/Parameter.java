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

package org.mule.devkit.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {
    /**
     * The name that the user of the module will use to configure this parameter.
     *
     * @return The name of the XML attribute
     */
    String name() default "";

    /**
     * Denotes if this field is optional or not. If it is not optional then the user
     * of the module must provide a value for this parameter before they can invoke
     * the processor.
     *
     * Defaults to false.
     *
     * @return True if optional, false otherwise
     */
    boolean optional() default false;

    /**
     * Default value for this parameter
     */
    String defaultValue() default "";
}
