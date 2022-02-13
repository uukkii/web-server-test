package server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sun.net.httpserver.HttpExchange;
import object.Cat;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class HandlerGeneral {
    protected static final String pathToDatabase = "src/main/resources/cats.csv";
    protected static final String GET = "GET";
    protected static final String POST = "POST";
    protected static final String ONLY_GET_REQUEST_IS_ALLOWED = "Only \"GET\" request is allowed!";
    protected static final String ONLY_POST_REQUEST_IS_ALLOWED = "Only \"POST\" request is allowed!";
    protected static final String INVALID_ORDER_ANSWER = "Invalid order! Please, use \"acs\" or \"desc\"!";
    protected static final String INVALID_REQUEST = "Invalid request!";
    protected static final String PING_ANSWER_GET = "object.Cat Service. Version 0.1";
    protected static final String NEW_CAT_ADDED_ANSWER = "object.Cat has been added to database!";
    protected static final String tailMean = "Mean of tails";
    protected static final String tailMedian = "Median of tails";
    protected static final String tailMode = "Mode of tails";
    protected static final String whiskersMean = "Mean of whiskers";
    protected static final String whiskersMedian = "Median of whiskers";
    protected static final String whiskersMode = "Mode of whiskers";
    protected static final String EMPTY_REQUEST_CATS = "/cats";
    protected static final String EMPTY_REQUEST_CAT = "/cat";
    protected static final String ASC_ORDER = "asc";
    protected static final String DESC_ORDER = "desc";
    protected static final String CATS_PARAM_NAME = "name";
    protected static final String CATS_PARAM_COLOUR = "cat_color";
    protected static final String CATS_PARAM_TAIL = "tail_length";
    protected static final String CATS_PARAM_WHISKERS = "whiskers_length";
    protected static final String[] columnMapping = {"name", "catColor", "tailLength", "whiskersLength"};
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendResponse(HttpExchange httpExchange, int rCode, String requestParamValue) throws IOException {
        httpExchange.sendResponseHeaders(rCode, requestParamValue.length());
        httpExchange.getResponseBody().write(requestParamValue.getBytes());
        httpExchange.getResponseBody().flush();
        httpExchange.getResponseBody().close();
    }

    protected void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
        String param = objectMapper.writeValueAsString(requestParamValue);
        if (requestParamValue != null) {
            sendResponse(httpExchange, 200, param);
        } else sendResponse(httpExchange, 404, ONLY_GET_REQUEST_IS_ALLOWED);
    }

    protected List<Cat> toList() {
        List<Cat> listOfCats = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(pathToDatabase))) {
            ColumnPositionMappingStrategy<Cat> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Cat.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Cat> catCsvToBean = new CsvToBeanBuilder<Cat>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            listOfCats = catCsvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listOfCats;
    }
}
