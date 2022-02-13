package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerPing extends server.handlers.HandlerGeneral implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;

        if (GET.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest();
        } else if (POST.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest();
        }
        handleResponse(httpExchange, requestParamValue);
    }

    private String handlePostRequest() {
        return null;
    }

    private String handleGetRequest() {
        return PING_ANSWER_GET;
    }
}