// Copyright Aeraki Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.aeraki;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class HelloClient {
    private static HelloService.Client client;
    private static TTransport transport;
    public static String serverHost = "localhost";

    public static void main(String[] args) {
        startTestServer();

        if (args.length > 0) {
            serverHost = args[0];
        }
        connectServer();

        if(args.length>1 && args[1].equals("demo")) {
            System.out.println("Periodically call thrift server");
            while (true) {
                try {
                    Thread.sleep(5000);
                    String response = client.sayHello("Aeraki");
                    System.out.println(response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    connectServer();
                }
            }
        }
    }


    private static void connectServer() {
        while (true) {
            try {
                if (transport != null) {
                    transport.close();
                }
                Thread.sleep(5000);
                transport = new TSocket(serverHost, 9090);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                client = new HelloService.Client(protocol);
                System.out.println("Connected to " + serverHost);
                return;
            } catch (Exception x) {
                System.out.println("Can't connect to " + serverHost);
                x.printStackTrace();
            }
        }
    }

    /**
     * startTestServer starts a HTTP server for e2e test
     */
    static void startTestServer() {
        Thread serverThread = new Thread(){
            public void run(){
                System.out.println("Start a http server for e2e test");
                int port = 9009;
                Server server = new Server(port);
                ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
                context.setContextPath("/");
                ServletHolder h = new ServletHolder(new HttpServletDispatcher());
                h.setInitParameter("javax.ws.rs.Application", "com.embedded.Services");
                context.addServlet(h, "/*");
                server.setHandler(context);
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serverThread.start();
    }
}
