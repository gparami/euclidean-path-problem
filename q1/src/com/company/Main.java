package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException{

        try {
            //Scanner to read the user's filename input.
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter the name for the data file? ");
            String fileName = scanner.next();

            BufferedReader reader;
            //Array list to store unsorted list of pools taken from the input database file.
            ArrayList<Pool> poolArrayList = new ArrayList<Pool>();

            //Input file should be in the resources directory parallel to the source directory.
            reader = new BufferedReader(new FileReader("resources/" + fileName));
            String line = reader.readLine();
            while (line != null) {

                String arrLine[] = line.split(",");

                //Adding each pool to the ArrayList
                if (arrLine.length == 3) {
                    String poolName = arrLine[0];
                    double poolLongitude = Double.parseDouble(arrLine[1]);
                    double poolLatitude = Double.parseDouble(arrLine[2]);
                    poolName = poolName.replace("\"", "");
                    poolArrayList.add(new Pool(poolName,poolLongitude,poolLatitude));
                }

                line = reader.readLine();
            }
            reader.close();



            System.out.println(poolArrayList.size());


        } catch (IOException e) {
            System.out.println("File Error!");
            e.printStackTrace();
        }






    }
}
