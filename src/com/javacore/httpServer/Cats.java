package com.javacore.httpServer;

import java.util.HashMap;

public class Cats {
    private static Cats INSTANCE;
    private HashMap<Integer, String> data = new HashMap<>();
    private int indexCounter = 2;

    private Cats() {
        this.data.put(0, "Calico");
        this.data.put(1, "Scottish Fold");
    }

    public static Cats getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Cats();
        }

        return INSTANCE;
    }

    public String getCat(Integer catIndex) {
        if (data.containsKey(catIndex)){
            return data.get(catIndex);
        } else {
            return "No such cat with that index";
        }
    }

    public HashMap<Integer, String> getCats(){
        return data;
    }

    public void postCat(String newCat){
        data.put(indexCounter, newCat);
        indexCounter += 1;
    }

    public String deleteCat(Integer catIndex) {
        if (data.containsKey(catIndex)){
            data.remove(catIndex);
            return "Cat with index " + catIndex.toString() + " has been removed";
        } else {
            return "No such cat with that index";
        }
    }

    public String updateCat(Integer catIndex, String catName) {
        if (data.containsKey(catIndex)) {
            data.put(catIndex, catName);
            return "Cat with index " + catIndex.toString() + " has been successfully updated to " + catName;
        }
        else {
            return "No cat with such an index was found";
        }
    }
}
