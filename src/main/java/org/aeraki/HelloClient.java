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

public class HelloClient {
    private static HelloService.Client client;
    private static TTransport transport;
    private static String serverHost = "localhost";

    public static void main(String[] args) {
        if (args.length > 0) {
            serverHost = args[0];
        }
        connectServer();
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
}
