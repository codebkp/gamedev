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


import patagonia.http.HttpEntity;
import patagonia.http.HttpResponse;
import patagonia.http.nio.ContentEncoder;
import patagonia.http.nio.IOControl;
import patagonia.http.nio.entity.EntityAsyncContentProducer;
import patagonia.http.nio.entity.HttpAsyncContentProducer;
import patagonia.http.protocol.HTTP;
import patagonia.http.protocol.HttpContext;

class ErrorResponseProducer implements HttpAsyncResponseProducer {

    private final HttpResponse response;
    private final HttpEntity entity;
    private final HttpAsyncContentProducer contentProducer;
    private final boolean keepAlive;

    ErrorResponseProducer(
            final HttpResponse response,
            final HttpEntity entity,
            final boolean keepAlive) {
        super();
        this.response = response;
        this.entity = entity;
        if (entity instanceof HttpAsyncContentProducer) {
            this.contentProducer = (HttpAsyncContentProducer) entity;
        } else {
            this.contentProducer = new EntityAsyncContentProducer(entity);
        }
        this.keepAlive = keepAlive;
    }

    public HttpResponse generateResponse() {
        if (this.keepAlive) {
            response.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        } else {
            response.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        }
        response.setEntity(this.entity);
        return response;
    }

    public void produceContent(
            final ContentEncoder encoder, final IOControl ioctrl) throws IOException {
        this.contentProducer.produceContent(encoder, ioctrl);
    }

    public void responseCompleted(final HttpContext context) {
    }

    public void failed(final Exception ex) {
    }

    public void close() throws IOException {
        this.contentProducer.close();
    }

}
