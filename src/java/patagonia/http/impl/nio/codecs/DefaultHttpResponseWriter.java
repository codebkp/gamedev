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

package patagonia.http.impl.nio.codecs;

import java.io.IOException;


import patagonia.http.HttpResponse;
import patagonia.http.annotation.NotThreadSafe;
import patagonia.http.message.LineFormatter;
import patagonia.http.nio.NHttpMessageWriter;
import patagonia.http.nio.reactor.SessionOutputBuffer;
import patagonia.http.params.HttpParams;
import patagonia.http.util.CharArrayBuffer;

/**
 * Default {@link NHttpMessageWriter} implementation for {@link HttpResponse}s.
 *
 * @since 4.1
 */
@NotThreadSafe
public class DefaultHttpResponseWriter extends AbstractMessageWriter<HttpResponse> {

    public DefaultHttpResponseWriter(final SessionOutputBuffer buffer,
                              final LineFormatter formatter,
                              final HttpParams params) {
        super(buffer, formatter, params);
    }

    @Override
    protected void writeHeadLine(final HttpResponse message) throws IOException {
        CharArrayBuffer buffer = lineFormatter.formatStatusLine(
                this.lineBuf, message.getStatusLine());
        this.sessionBuffer.writeLine(buffer);
    }

}
