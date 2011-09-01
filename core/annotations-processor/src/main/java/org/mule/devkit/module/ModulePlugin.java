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

package org.mule.devkit.module;

import org.mule.devkit.Plugin;
import org.mule.devkit.generation.Generator;
import org.mule.devkit.module.generation.BeanDefinitionParserGenerator;
import org.mule.devkit.module.generation.DummyInboundEndpointGenerator;
import org.mule.devkit.module.generation.EnumTransformerGenerator;
import org.mule.devkit.module.generation.HttpCallbackAdapterGenerator;
import org.mule.devkit.module.generation.HttpCallbackGenerator;
import org.mule.devkit.module.generation.InterceptCallbackGenerator;
import org.mule.devkit.module.generation.JaxbTransformerGenerator;
import org.mule.devkit.module.generation.LifecycleAdapterFactoryGenerator;
import org.mule.devkit.module.generation.LifecycleAdapterGenerator;
import org.mule.devkit.module.generation.MessageProcessorGenerator;
import org.mule.devkit.module.generation.MessageSourceGenerator;
import org.mule.devkit.module.generation.NamespaceHandlerGenerator;
import org.mule.devkit.module.generation.OAuthAdapterGenerator;
import org.mule.devkit.module.generation.PoolAdapterGenerator;
import org.mule.devkit.module.generation.ProcessorCallbackGenerator;
import org.mule.devkit.module.generation.RegistryBootstrapGenerator;
import org.mule.devkit.module.generation.SchemaGenerator;
import org.mule.devkit.module.generation.SessionAdapterGenerator;
import org.mule.devkit.module.generation.SpringNamespaceHandlerGenerator;
import org.mule.devkit.module.generation.SpringSchemaGenerator;
import org.mule.devkit.module.generation.TransformerGenerator;
import org.mule.devkit.module.validation.ModuleValidator;
import org.mule.devkit.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class ModulePlugin implements Plugin {
    private List<Validator> validators;
    private List<Generator> generators;

    public ModulePlugin() {
        this.generators = new ArrayList<Generator>();
        this.generators.add(new SchemaGenerator());
        this.generators.add(new DummyInboundEndpointGenerator());
        this.generators.add(new HttpCallbackGenerator());
        this.generators.add(new LifecycleAdapterGenerator());
        this.generators.add(new HttpCallbackAdapterGenerator());
        this.generators.add(new OAuthAdapterGenerator());
        this.generators.add(new LifecycleAdapterFactoryGenerator());
        this.generators.add(new SessionAdapterGenerator());
        this.generators.add(new PoolAdapterGenerator());
        this.generators.add(new JaxbTransformerGenerator());
        this.generators.add(new TransformerGenerator());
        this.generators.add(new EnumTransformerGenerator());
        this.generators.add(new ProcessorCallbackGenerator());
        this.generators.add(new InterceptCallbackGenerator());
        this.generators.add(new BeanDefinitionParserGenerator());
        this.generators.add(new MessageSourceGenerator());
        this.generators.add(new MessageProcessorGenerator());
        this.generators.add(new NamespaceHandlerGenerator());
        this.generators.add(new SpringNamespaceHandlerGenerator());
        this.generators.add(new SpringSchemaGenerator());
        this.generators.add(new RegistryBootstrapGenerator());

        this.validators = new ArrayList<Validator>();
        this.validators.add(new ModuleValidator());
    }

    public List<Validator> getValidators() {
        return this.validators;
    }

    public List<Generator> getGenerators() {
        return this.generators;
    }
}
