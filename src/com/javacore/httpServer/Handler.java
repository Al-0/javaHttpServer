package com.javacore.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        Controller controller = new Controller();
        String path = parsePath(t.getRequestURI().getPath());
        switch(t.getRequestMethod()) {
            case "GET":
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
                if (path.length() > 0 && isNumeric(path) == true){
                    controller.deleteCat(t, Integer.parseInt(path));
                } else {
                    controller.deleteCat(t, -1);
                }
                break;
            case "PUT":
                break;
            default:

        }
    }

    private String parsePath(String path) {
        String newPath = path.replaceFirst("/cats", "");
        newPath = newPath.replaceFirst("/", "");
        return newPath;
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
