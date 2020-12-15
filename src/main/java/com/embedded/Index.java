package com.embedded;

/*
  for text/html files
import java.io.File;
import java.net.URISyntaxException;
import javax.ws.rs.Produces;
import javax.activation.MimetypesFileTypeMap;
*/
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.aeraki.HelloService;

import javax.ws.rs.core.Response;
import org.aeraki.HelloClient;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@Path("/")
public class Index {
    /*
    for html files
    @GET
    @Produces("text/html")
    public Response  index() throws URISyntaxException {
        File f = new File(System.getProperty("user.dir")+"/index.html");
        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }
    */

    @GET
    @Path("/hello")
    public Response  echoGet() {
        try{
            TTransport transport = new TSocket(HelloClient.serverHost, 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            HelloService.Client client = new HelloService.Client(protocol);
            String hello = client.sayHello("Aeraki");
            System.out.println(hello);
            transport.close();
            return Response.status(200).entity(hello).build();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return Response.status(503).entity(ex.getMessage()).build();
        }
    }
}
