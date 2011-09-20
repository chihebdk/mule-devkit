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

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.Transformer;
import org.mule.api.callback.SourceCallback;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

@Module(name = "pool", poolable = true)
public class PoolModule {
    @Configurable
    @Optional
    @Default("10")
    private int base;

    @Processor
    public int sumMultiplyAndDivide(int sum1, int sum2, @Optional @Default("1") int multiply) {
        return ((sum1 + sum2) * multiply) / base;
    }

    public void setBase(int value) {
        this.base = value;
    }

    @Source
    public void count(int startAt, int endAt, int step, SourceCallback callback) throws Exception {
        int count = startAt;
        while (true) {
            if (Thread.interrupted() || count == endAt)
                throw new InterruptedException();

            callback.process(count);

            count += step;
        }
    }

    @Transformer(sourceTypes = {String.class})
    public Character transformStringToChar(Object payload) {
        if (payload != null) {
            return ((String) payload).charAt(0);
        }

        return null;
    }
}
