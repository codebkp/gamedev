/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package patagonia.http.nio.protocol;

import java.io.IOException;


import patagonia.http.ContentTooLongException;
import patagonia.http.HttpEntity;
import patagonia.http.HttpResponse;
import patagonia.http.annotation.ThreadSafe;
import patagonia.http.entity.ContentType;
import patagonia.http.nio.ContentDecoder;
import patagonia.http.nio.IOControl;
import patagonia.http.nio.entity.ContentBufferEntity;
import patagonia.http.nio.util.HeapByteBufferAllocator;
import patagonia.http.nio.util.SimpleInputBuffer;
import patagonia.http.protocol.HttpContext;

/**
 * Basic implementation of {@link HttpAsyncResponseConsumer}. Please note that
 * this consumer buffers response content in memory and should be used for
 * relatively small response messages.
 *
 * @since 4.2
 */
@ThreadSafe
public class BasicAsyncResponseConsumer extends AbstractAsyncResponseConsumer<HttpResponse> {

    private volatile HttpResponse response;
    private volatile SimpleInputBuffer buf;

    public BasicAsyncResponseConsumer() {
        super();
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) throws IOException {
        this.response = response;
    }

    @Override
    protected void onEntityEnclosed(
            final HttpEntity entity, final ContentType contentType) throws IOException {
        long len = entity.getContentLength();
        if (len > Integer.MAX_VALUE) {
            throw new ContentTooLongException("Entity content is too long: " + len);
        }
        if (len < 0) {
            len = 4096;
        }
        this.buf = new SimpleInputBuffer((int) len, new HeapByteBufferAllocator());
        this.response.setEntity(new ContentBufferEntity(entity, this.buf));
    }

    @Override
    protected void onContentReceived(
            final ContentDecoder decoder, final IOControl ioctrl) throws IOException {
        if (this.buf == null) {
            throw new IllegalStateException("Content buffer is null");
        }
        this.buf.consumeContent(decoder);
    }

    @Override
    protected void releaseResources() {
        this.response = null;
        this.buf = null;
    }

    @Override
    protected HttpResponse buildResult(final HttpContext context) {
        return this.response;
    }

}
