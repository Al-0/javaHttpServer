package com.javacore.httpServer;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private Cats cats = Cats.getInstance();

    public void getCat(HttpExchange t, Integer catIndex) throws IOException {
        String response = cats.getCat(catIndex);
        sendResponse(t, response);
    }

    public void getCats(HttpExchange t) throws IOException {
        String response = "{\n";

        HashMap<Integer, String> catsList = cats.getCats();
        for (Map.Entry<Integer, String> entry : catsList.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            response += "\t" + key + ": " + value + ",\n";
        }
        response += "}";

        sendResponse(t, response);
    }

    public void postCat(HttpExchange t) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);

        // send response
        String response = "";
        if (parameters.size() == 1) {
            for (String key : parameters.keySet()) {
                if (key.equals("name")){
                    if (parameters.get(key) == null){
                        response = "Please provide a non empty name";
                    } else {
                        String newCatName = parameters.get(key).toString();
                        cats.postCat(newCatName);
                        response = "A new cat has been added!";
                    }
                } else {
                    response = "A parameter of 'name' must be provided";
                }
            }
        } else {
            response = "Incorrect number of parameters";
        }

        sendResponse(t, response);
    }

    public void deleteCat(HttpExchange t, Integer catIndex) throws IOException {
        String response = cats.deleteCat(catIndex);
        sendResponse(t, response);
    }

    public void updateCat(HttpExchange t) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);

        // send response
        String response = "";
        if (parameters.size() == 2) {
            String newCatName = "";
            Integer catIndex = -1;
            for (String key : parameters.keySet()) {

                if (key.equals("name")){
                    newCatName = parameters.get(key).toString();
                } else if (key.equals("index")) {
                    catIndex = Integer.parseInt(parameters.get(key).toString());
                }
            }
            if (catIndex != -1 && newCatName.length() > 0) {
                response = cats.updateCat(catIndex, newCatName);
            }
            else {
                response = "No name or index provided";
            }
        } else {
            response = "Incorrect number of parameters";
        }

        sendResponse(t, response);
    }

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    public void sendResponse(HttpExchange t, String response) throws IOException{
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
