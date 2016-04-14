package com.aliasghar;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class TourJsonGenerator {
    static TourJsonGenerator gen;
    static FileWriter writer;
    static JSONObject json;
    private TourJsonGenerator(){
        File f = new File("tours.json");
        json = new JSONObject();
        try {
            if(f.exists()) {
                JSONParser parser = new JSONParser();
                try {
                    json =(JSONObject) parser.parse(new FileReader(f));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
                f.createNewFile();
            writer = new FileWriter("tours.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close(){

        try {
            writer.write(json.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TourJsonGenerator getTourJsonGenarator(){
        if(gen == null)
            gen = new TourJsonGenerator();
        return gen;

    }
    public synchronized void gen(int id, String name, int length, String imgURL, String discription, ArrayList<TourDetail> details){

        JSONObject obj2 = new JSONObject();
        JSONObject obj3 = new JSONObject();
        JSONArray array = new JSONArray();

        obj2.put("id", id);
        obj2.put("length", length);
        obj2.put("imageURL", imgURL);
        obj2.put("discription", discription);
        for(int i = 0; i < details.size(); i++)
            obj3.put(i, details.get(i).description);
        array.add(obj3);
        obj2.put("detail", array);

        json.put(name, obj2);

    }
}
