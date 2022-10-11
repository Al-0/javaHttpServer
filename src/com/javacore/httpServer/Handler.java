package com.javacore.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        Controller controller = new Controller();
        switch(t.getRequestMethod()) {
            case "GET":
                String path = t.getRequestURI().getPath();
                path = path.replaceFirst("/cats", "");
                path = path.replaceFirst("/", "");
                if (path.length() > 0 && isNumeric(path) == true){
                    controller.getCat(t, Integer.parseInt(path));
                } else {
                    controller.getCats(t);
                }
                break;
            case "POST":
                controller.postCat(t);
                break;
            case "DELETE":
                break;
            case "PUT":
                break;
            default:

        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
