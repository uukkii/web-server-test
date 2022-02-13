package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import object.Cat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HandlerLengthStat extends server.handlers.HandlerGeneral implements HttpHandler {

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
        return ONLY_GET_REQUEST_IS_ALLOWED;
    }

    private String handleGetRequest() throws JsonProcessingException {
        List<Cat> catList = toList();
        int[] arrayOfTails = toArrayTails(catList);
        int[] arrayOfWhiskers = toArrayWhiskers(catList);
        int numberOfCats = catList.size();
        // Calculating mean of tails and whiskers
        int meanOfTails = sumOfCatsTails(catList) / numberOfCats;
        int meanOfWhiskers = sumOfCatsWhiskers(catList) / numberOfCats;
        // Calculating median of tails and whiskers
        int medianOfTails = getMedian(arrayOfTails);
        int medianOfWhiskers = getMedian(arrayOfWhiskers);
        // Calculating mode of tails and whiskers
        int modeOfTails = getMode(arrayOfTails);
        int modeOfWhiskers = getMode(arrayOfWhiskers);
        // To Map
        Map<String, Integer> catMap = toMap(meanOfTails, meanOfWhiskers,
                medianOfTails, medianOfWhiskers,
                modeOfTails, modeOfWhiskers);
        return objectMapper.writeValueAsString(catMap);
    }

    private Integer sumOfCatsTails(List<Cat> listOfCats) {
        return listOfCats.stream()
                .map(Cat::getTailLength)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Integer sumOfCatsWhiskers(List<Cat> listOfCats) {
        return listOfCats.stream()
                .map(Cat::getWhiskersLength)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int[] toArrayTails(List<Cat> listOfCats) {
        return listOfCats.stream()
                .map(Cat::getTailLength)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private int[] toArrayWhiskers(List<Cat> listOfCats) {
        return listOfCats.stream()
                .map(Cat::getWhiskersLength)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private Integer getMedian(int[] arrayOfLength) {
        Arrays.sort(arrayOfLength);
        if (arrayOfLength.length % 2 == 0) {
            return ((arrayOfLength[arrayOfLength.length / 2] + arrayOfLength[arrayOfLength.length / 2 - 1]));
        } else return arrayOfLength[arrayOfLength.length / 2];
    }

    private Integer getMode(int[] arrayOfLength) {
        Integer result = null;
        Map<Integer, Integer> integerHashMap = new HashMap<>();
        for (Integer i : arrayOfLength) {
            if (!integerHashMap.containsKey(i)) {
                integerHashMap.put(i, 1);
            } else {
                integerHashMap.put(i, integerHashMap.get(i) + 1);
            }
        }
        int valueInMap = integerHashMap.values()
                .stream()
                .reduce(0, (max, element) -> element > max ? element : max);
        for (Map.Entry<Integer, Integer> entry : integerHashMap.entrySet()) {
            if (entry.getValue().equals(valueInMap)) {
                result = entry.getKey();
            }
        }
        return result;
    }

    private Map<String, Integer> toMap(int meanOfTails, int meanOfWhiskers,
                                       int medianOfTails, int medianOfWhiskers,
                                       int modeOfTails, int modeOfWhiskers) {
        Map<String, Integer> mapOfData = new HashMap<>();
        mapOfData.put(tailMean, meanOfTails);
        mapOfData.put(whiskersMean, meanOfWhiskers);
        mapOfData.put(tailMedian, medianOfTails);
        mapOfData.put(whiskersMedian, medianOfWhiskers);
        mapOfData.put(tailMode, modeOfTails);
        mapOfData.put(whiskersMode, modeOfWhiskers);
        Stream<Map.Entry<String, Integer>> sorted = mapOfData.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByKey().reversed());
        return mapOfData;
    }
}