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

package org.mule.devkit.it;

import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Transformer;
import org.mule.api.annotations.param.InvocationHeaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Module(name = "headers")
public class HeadersModule {
    @Processor
    public String processHeader(@InvocationHeaders("foo") String foo) {
        return foo;
    }

    @Processor
    public String processHeaderOptional(@InvocationHeaders("faz?") String faz) {
        if (faz == null) {
            return "faz not set";
        }
        return faz;
    }

    @Processor
    public URL processHeaderWithType(@InvocationHeaders("url") URL apple) {
        return apple;
    }

    @Processor
    public Map<?, ?> processHeaders(@InvocationHeaders("foo, bar") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processHeadersAll(@InvocationHeaders("*") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processHeadersWildcard(@InvocationHeaders("MULE_*") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processHeadersMultiWildcard(@InvocationHeaders("MULE_*, ba*") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processSingleMapHeader(@InvocationHeaders("foo") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processHeadersOptional(@InvocationHeaders("foo, bar, baz?") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public Map<?, ?> processHeadersAllOptional(@InvocationHeaders("foo?, bar?") Map<?, ?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersList(@InvocationHeaders("foo, bar, baz") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersListAll(@InvocationHeaders("*") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processSingleHeaderList(@InvocationHeaders("foo") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersListOptional(@InvocationHeaders("foo, bar, baz?") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersListAllOptional(@InvocationHeaders("foo?, bar?") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersListWildcard(@InvocationHeaders("MULE_*") List<?> headers) {
        return headers;
    }

    @Processor
    public List<?> processHeadersListMultiWildcard(@InvocationHeaders("MULE_*, ba*") List<?> headers) {
        return headers;
    }

    @Transformer(sourceTypes = {String.class})
    public static URL transformStringToUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
