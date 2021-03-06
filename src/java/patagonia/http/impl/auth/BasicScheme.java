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

package patagonia.http.impl.auth;



import patagonia.http.Header;
import patagonia.http.HttpRequest;
import patagonia.http.annotation.NotThreadSafe;
import patagonia.http.auth.AUTH;
import patagonia.http.auth.AuthenticationException;
import patagonia.http.auth.ChallengeState;
import patagonia.http.auth.ContextAwareAuthScheme;
import patagonia.http.auth.Credentials;
import patagonia.http.auth.InvalidCredentialsException;
import patagonia.http.auth.MalformedChallengeException;
import patagonia.http.auth.params.AuthParams;
import patagonia.http.message.BufferedHeader;
import patagonia.http.protocol.BasicHttpContext;
import patagonia.http.protocol.HttpContext;
import patagonia.http.util.CharArrayBuffer;
import patagonia.http.util.EncodingUtils;
import patagonia.util.Base64;

/**
 * Basic authentication scheme as defined in RFC 2617.
 * <p>
 * The following parameters can be used to customize the behavior of this
 * class:
 * <ul>
 *  <li>{@link patagonia.http.auth.params.AuthPNames#CREDENTIAL_CHARSET}</li>
 * </ul>
 *
 * @since 4.0
 */
@NotThreadSafe
public class BasicScheme extends RFC2617Scheme {

    /** Whether the basic authentication process is complete */
    private boolean complete;

    /**
     * Creates an instance of <tt>BasicScheme</tt> with the given challenge
     * state.
     *
     * @since 4.2
     */
    public BasicScheme(final ChallengeState challengeState) {
        super(challengeState);
        this.complete = false;
    }

    public BasicScheme() {
        this(null);
    }

    /**
     * Returns textual designation of the basic authentication scheme.
     *
     * @return <code>basic</code>
     */
    public String getSchemeName() {
        return "basic";
    }

    /**
     * Processes the Basic challenge.
     *
     * @param header the challenge header
     *
     * @throws MalformedChallengeException is thrown if the authentication challenge
     * is malformed
     */
    @Override
    public void processChallenge(
            final Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        this.complete = true;
    }

    /**
     * Tests if the Basic authentication process has been completed.
     *
     * @return <tt>true</tt> if Basic authorization has been processed,
     *   <tt>false</tt> otherwise.
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Returns <tt>false</tt>. Basic authentication scheme is request based.
     *
     * @return <tt>false</tt>.
     */
    public boolean isConnectionBased() {
        return false;
    }

    /**
     * @deprecated (4.2) Use {@link ContextAwareAuthScheme#authenticate(Credentials, HttpRequest, patagonia.http.protocol.HttpContext)}
     */
    @Deprecated
    public Header authenticate(
            final Credentials credentials, final HttpRequest request) throws AuthenticationException {
        return authenticate(credentials, request, new BasicHttpContext());
    }

    /**
     * Produces basic authorization header for the given set of {@link Credentials}.
     *
     * @param credentials The set of credentials to be used for authentication
     * @param request The request being authenticated
     * @throws InvalidCredentialsException if authentication credentials are not
     *   valid or not applicable for this authentication scheme
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return a basic authorization string
     */
    @Override
    public Header authenticate(
            final Credentials credentials,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {

        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }

        String charset = AuthParams.getCredentialCharset(request.getParams());
        return authenticate(credentials, charset, isProxy());
    }

    /**
     * Returns a basic <tt>Authorization</tt> header value for the given
     * {@link Credentials} and charset.
     *
     * @param credentials The credentials to encode.
     * @param charset The charset to use for encoding the credentials
     *
     * @return a basic authorization header
     */
    public static Header authenticate(
            final Credentials credentials,
            final String charset,
            boolean proxy) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset may not be null");
        }

        StringBuilder tmp = new StringBuilder();
        tmp.append(credentials.getUserPrincipal().getName());
        tmp.append(":");
        tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());

        byte[] base64password = Base64.encode(EncodingUtils.getBytes(tmp.toString(), charset), Base64.DEFAULT);

        CharArrayBuffer buffer = new CharArrayBuffer(32);
        if (proxy) {
            buffer.append(AUTH.PROXY_AUTH_RESP);
        } else {
            buffer.append(AUTH.WWW_AUTH_RESP);
        }
        buffer.append(": Basic ");
        buffer.append(base64password, 0, base64password.length);

        return new BufferedHeader(buffer);
    }

}
