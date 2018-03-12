package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, NullPointerException{

        try {
            Object fileObj = new JSONParser().parse(new FileReader("resources/wading-pools.json"));

            JSONObject poolsJO = (JSONObject) fileObj;

            ArrayList<Pool> poolArrayList = new ArrayList<Pool>();

            JSONArray pools = (JSONArray) poolsJO.get("features");

            for (Object obj:pools) {

                JSONObject pool = (JSONObject) obj;

                JSONObject poolProperties = (JSONObject) pool.get("properties");
                JSONObject poolGeometry = (JSONObject) pool.get("geometry");
                JSONArray poolCoordinates = (JSONArray) poolGeometry.get("coordinates");

                String poolName = (String) poolProperties.get("NAME");
                double poolLongitude‎ = (double) poolCoordinates.get(0);
                double poolLatitude‎ = (double) poolCoordinates.get(1);

                poolName = "\"" + poolName.substring(14) + "\"";

                Pool cPool = new Pool(poolName, poolLongitude‎, poolLatitude‎);
                poolArrayList.add(cPool);

            }

            WriteToText(poolArrayList);
            WriteToProlog(poolArrayList);

            for (Pool P: poolArrayList) {
                System.out.println("NAME: " + P.getName() + " Longitude: " + P.getLon() + " Latitude " + P.getLat());
            }

        } catch (ParseException e) {
            System.out.println("File Error!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Data Error!");
            e.printStackTrace();
        }

    }

    static void WriteToText(ArrayList<Pool> pools) throws IOException {

        try {
            File database = new File("resources/database.txt");
            database.createNewFile();
            FileWriter writer = new FileWriter(database);
            for (Pool P:pools) writer.write(P.getName() + "," + P.getLon() + "," + P.getLat() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Text Write Error!");
            e.printStackTrace();
        }
    }

    static void WriteToProlog(ArrayList<Pool> pools) {

        try {
            File database = new File("resources/database.pl");
            database.createNewFile();
            FileWriter writer = new FileWriter(database);
            for (Pool P:pools) writer.write("pool(" + P.getName() + ", " + P.getLon() + ", " + P.getLat() + ").\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Text Write Error!");
            e.printStackTrace();
        }

    }

}
