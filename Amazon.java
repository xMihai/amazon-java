/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author walter
 */
public class Amazon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\walter\\Desktop\\work\\nbm\\amazon\\src\\amazon\\";
        String inputFile = "01_AMS.txt";
        String[] inputFileParts = inputFile.split("\\.");
        String outputFile = inputFileParts[0] + "_solution." + inputFileParts[1];

        ArrayList<int[]> routes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path + inputFile))) {
            String[] parts = br.readLine().split(" ");
            int cityCount = Integer.parseInt(parts[0]);
            int routeCount = Integer.parseInt(parts[1]);

            for (int i = 0; i < routeCount; i++) {
                String[] p = br.readLine().split(" ");
                routes.add(new int[]{Integer.parseInt(p[0]) - 1, Integer.parseInt(p[1]) - 1});
            }

            // TODO code application logic here
            World w = new World(cityCount, cityCount, routes, path + outputFile);

            w.all();

        }
    }

}
