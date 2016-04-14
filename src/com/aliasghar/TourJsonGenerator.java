package com.aliasghar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class TourJsonGenerator {
    static TourJsonGenerator gen;
    static Writer writer;
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
        Gson gson = new GsonBuilder().create();
        gson.toJson(id, writer);
        gson.toJson(name, writer);
        gson.toJson(length, writer);
        gson.toJson(imgURL, writer);
        gson.toJson(discription, writer);
        gson.toJson(details, writer);

    }
}
