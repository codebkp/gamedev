/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package patagonia.http.auth;

import patagonia.http.annotation.Immutable;

/**
 * @since 4.2
 */
@Immutable
public final class AuthOption {

    private final AuthScheme authScheme;
    private final Credentials creds;

    public AuthOption(final AuthScheme authScheme, final Credentials creds) {
        super();
        if (authScheme == null) {
            throw new IllegalArgumentException("Auth scheme may not be null");
        }
        if (creds == null) {
            throw new IllegalArgumentException("User credentials may not be null");
        }
        this.authScheme = authScheme;
        this.creds = creds;
    }

    public AuthScheme getAuthScheme() {
        return this.authScheme;
    }

    public Credentials getCredentials() {
        return this.creds;
    }

    @Override
    public String toString() {
        return this.authScheme.toString();
    }

}

