/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.spdy.server;

import io.netty.handler.codec.spdy.SpdyOrHttpChooser;
import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.spdy.SpdyOrHttpChooser.SelectedProtocol.*;

/**
 * The Jetty project provides an implementation of the Transport Layer Security (TLS) extension for Next
 * Protocol Negotiation (NPN) for OpenJDK 7 or greater. NPN allows the application layer to negotiate which
 * protocol to use over the secure connection.
 * <p>
 * This NPN service provider negotiates using SPDY.
 * <p>
 * To enable NPN support, start the JVM with: {@code java -Xbootclasspath/p:<path_to_npn_boot_jar> ...}. The
 * "path_to_npn_boot_jar" is the path on the file system for the NPN Boot Jar file which can be downloaded from
 * Maven at coordinates org.mortbay.jetty.npn:npn-boot. Different versions applies to different OpenJDK versions.
 *
 * @see <a href="http://www.eclipse.org/jetty/documentation/current/npn-chapter.html">Jetty documentation</a>
 */
public class SpdyServerProvider implements ServerProvider {

    private String selectedProtocol;

    @Override
    public void unsupported() {
        // if unsupported, default to http/1.1
        selectedProtocol = "http/1.1";
    }

    @Override
    public List<String> protocols() {
        return Arrays.asList("spdy/3.1", "spdy/3", "http/1.1");
    }

    @Override
    public void protocolSelected(String protocol) {
        selectedProtocol = protocol;
    }


    static Map<String,SpdyOrHttpChooser.SelectedProtocol> PROTOCOLS = new HashMap<>();
    static {
        PROTOCOLS.put("spdy/3",SPDY_3);
        PROTOCOLS.put("spdy/3.1",SPDY_3_1);
        PROTOCOLS.put("http/1.1",HTTP_1_1);
        PROTOCOLS.put("http/1.0",HTTP_1_0);
    }

    public SpdyOrHttpChooser.SelectedProtocol getSelectedProtocol() {
        if (selectedProtocol == null) {
            return UNKNOWN;
        }
        return PROTOCOLS.get(selectedProtocol);
    }
}