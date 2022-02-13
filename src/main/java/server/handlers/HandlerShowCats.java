package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import object.Cat;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerShowCats extends server.handlers.HandlerGeneral implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;

        if (GET.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if (POST.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest();
        }
        handleResponse(httpExchange, requestParamValue);
    }

    private String handlePostRequest() {
        return ONLY_GET_REQUEST_IS_ALLOWED;
    }

    private String handleGetRequest(HttpExchange httpExchange) throws JsonProcessingException {
        List<Cat> catList = toList();
        if (EMPTY_REQUEST_CATS.equals(httpExchange.getRequestURI().toString()) && catList != null) {
            return objectMapper.writeValueAsString(catList);
        } else {
            String request = httpExchange.getRequestURI().toString();
            // Empty request check
            if (EMPTY_REQUEST_CATS.equals(request)) {
                return INVALID_REQUEST;
            }
            // Split request to attribute and order params
            String attribute = request.substring(request.indexOf("=") + 1, request.indexOf("&"));
            String order = request.substring(request.lastIndexOf("=") + 1);
            // Sorting cats by attribute
            List<Cat> sortedList = sortCats(attribute, catList);
            // Sorting cats by order
            if (order.equals(ASC_ORDER)) {
                return objectMapper.writeValueAsString(sortedList);
            } else if (order.equals(DESC_ORDER)) {
                Collections.reverse(sortedList);
                return objectMapper.writeValueAsString(sortedList);
            } else return INVALID_ORDER_ANSWER;
        }
    }

    private List<Cat> sortCats(String attribute, List<Cat> catList) {
        List<Cat> sortedList;
        switch (attribute) {
            case CATS_PARAM_NAME -> sortedList = catList.stream()
                    .sorted(Comparator.comparing(Cat::getName))
                    .collect(Collectors.toList());
            case CATS_PARAM_COLOUR -> sortedList = catList.stream()
                    .sorted(Comparator.comparing(Cat::getCatColor))
                    .collect(Collectors.toList());
            case CATS_PARAM_TAIL -> sortedList = catList.stream()
                    .sorted(Comparator.comparing(Cat::getTailLength))
                    .collect(Collectors.toList());
            case CATS_PARAM_WHISKERS -> sortedList = catList.stream()
                    .sorted(Comparator.comparing(Cat::getWhiskersLength))
                    .collect(Collectors.toList());
            default -> throw new IllegalStateException("Unexpected value: " + attribute);
        }
        return sortedList;
    }
}