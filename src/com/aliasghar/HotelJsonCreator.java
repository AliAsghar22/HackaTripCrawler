package com.aliasghar;

import org.apache.pdfbox.pdmodel.graphics.predictor.Sub;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class HotelJsonCreator {
    static HotelJsonCreator gen;
    static FileWriter writer;
    static BufferedWriter bufferedWriter;
    static JSONObject obj ;

    private HotelJsonCreator(){
        File f = new File("hotel.json");
        obj = new JSONObject();
        try {
            if(!f.exists())
                f.createNewFile();
            writer = new FileWriter("hotel.json");
            bufferedWriter=new BufferedWriter(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        try {
            bufferedWriter.write(obj.toJSONString());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static HotelJsonCreator getHotelJsonCreator(){
        if(gen == null)
            gen = new HotelJsonCreator();
        return gen;

    }
    public synchronized void gen(String name,String place,String address,String DistanceToAirport,String Facilities,String Description,String image){
        JSONObject Subobj = new JSONObject();
        Subobj.put("place",place);
        Subobj.put("address",address);
        Subobj.put("DistanceToAirport",DistanceToAirport);
        Subobj.put("Facilities",Facilities);
        Subobj.put("Description",Description);
        Subobj.put("image",image);
        obj.put(name,Subobj);



    }
}
