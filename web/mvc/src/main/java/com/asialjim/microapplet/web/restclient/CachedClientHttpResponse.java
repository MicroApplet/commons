/*
 * Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
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

package com.asialjim.microapplet.web.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedClientHttpResponse implements ClientHttpResponse {
    private final ClientHttpResponse response;
    private final byte[] buffer;

    public CachedClientHttpResponse(ClientHttpResponse response) throws IOException {
        this.response = response;
        this.buffer = StreamUtils.copyToByteArray(response.getBody());
    }

    public byte[] buffer(){
        return this.buffer;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public HttpStatusCode getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String getStatusText() throws IOException {
        return response.getStatusText();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public InputStream getBody() {
        return new ByteArrayInputStream(this.buffer());
    }
}