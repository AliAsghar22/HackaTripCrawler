package com.aliasghar;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class TourJsonGenerator {
    static TourJsonGenerator gen;
    static FileWriter writer;
    private TourJsonGenerator(){
        File f = new File("tours.json");

        try {
            if(!f.exists())
                f.createNewFile();
            writer = new FileWriter("tours.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        try {
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

        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);
        obj.put("length", length);
        obj.put("imageURL", imgURL);
        obj.put("discription", discription);
        obj.put("details", details);

        try {
            writer.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
