package server;

import com.sun.net.httpserver.HttpServer;
import server.handlers.HandlerColourStat;
import server.handlers.HandlerLengthStat;
import server.handlers.HandlerPing;
import server.handlers.HandlerShowCats;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private HttpServer server;
    private ThreadPoolExecutor threadPoolExecutor;


    public Server() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.createContext("/cat", new server.handlers.HandlerNewCat());
        server.createContext("/ping", new HandlerPing());
        server.createContext("/cats", new HandlerShowCats());
        server.createContext("/stats/colour", new HandlerColourStat());
        server.createContext("/stats/length", new HandlerLengthStat());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("server.Server at " + server.getAddress().toString() + " has been started!");
    }
}