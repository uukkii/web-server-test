package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import object.Cat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerColourStat extends server.handlers.HandlerGeneral implements HttpHandler {

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

    private String handleGetRequest() throws JsonProcessingException {
        List<Cat> catList = toList();
        Map<String, Long> catMap = countCatsByColour(catList);
        return objectMapper.writeValueAsString(catMap);
    }

    private Map<String, Long> countCatsByColour(List<Cat> listOfCats) {
        return listOfCats.stream()
                .collect(Collectors.groupingBy(Cat::getCatColor, Collectors.counting()));
    }
}