package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;

public class HandlerNewCat extends server.handlers.HandlerGeneral implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue = null;

        if (GET.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest();
        } else if (POST.equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, requestParamValue);
    }

    private String handlePostRequest(HttpExchange httpExchange) throws JsonProcessingException {
        String request = httpExchange.getRequestURI().toString();
        String object = request.substring((request.indexOf("\"") + 1), request.lastIndexOf("\""));
        JsonNode jsonObject = objectMapper.readTree(object);
        CsvSchema.Builder csvBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonObject.elements().next();
        firstObject.fieldNames().forEachRemaining(csvBuilder::addColumn);
        CsvSchema csvSchema = csvBuilder.build().withHeader();

        try {
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File(pathToDatabase), jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NEW_CAT_ADDED_ANSWER;
    }

    private String handleGetRequest() {
        return ONLY_POST_REQUEST_IS_ALLOWED;
    }
}