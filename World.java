/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazon;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author walter
 */
public class World {

    private final int origCityCount;
    private final int cityCount;
    private final ArrayList<int[]> routes;
    private final ArrayList<City> cities = new ArrayList<>();
    private final String outputFile;

    public World(int origCityCount, int minCityCount, ArrayList<int[]> routes, String outputFile) {
        this.origCityCount = origCityCount;
        this.routes = routes;
        this.outputFile = outputFile;

        // Find optimum city number
        int optimalCityCount = minCityCount;
        while (optimalCityCount * (optimalCityCount - 1) % 4 != 0) {
            optimalCityCount++;
        }
        this.cityCount = optimalCityCount;
        System.out.println(origCityCount + " -> " + optimalCityCount + " cities");

        // Create cities
        for (int i = 0; i < this.cityCount; i++) {
            this.cities.add(new City(i));

        }

        for (int i = 0; i < this.cityCount; i++) {
            this.cities.get(i).init(new ArrayList<>(this.cities));
        }

        // Routes in cities
        for (int[] route : this.routes) {
            this.addRoute(route);
        }

    }

    private void addRoute(int[] route) {
        this.cities.get(route[0]).addRoute(this.cities.get(route[1]));
        this.cities.get(route[1]).addRoute(this.cities.get(route[0]));
    }

    private void removeRoute(int[] route) {
        this.cities.get(route[0]).removeRoute(this.cities.get(route[1]));
        this.cities.get(route[1]).removeRoute(this.cities.get(route[0]));
    }

    public void getCities() {
        for (City city : this.cities) {
            System.out.println(city);
        }
    }

    public boolean checkShallow() {
        ArrayList<Integer> routeCounts1 = new ArrayList<>();
        ArrayList<Integer> routeCounts2 = new ArrayList<>();

        for (City city : this.cities) {
            routeCounts1.add(city.routeCount());
            routeCounts2.add(cityCount - city.routeCount() - 1);
//            System.out.println(city);
        }

        Collections.sort(routeCounts1);
        Collections.sort(routeCounts2);

//        System.out.println(routeCounts1);
//        System.out.println(routeCounts2);
//        System.out.println();
        return routeCounts1.equals(routeCounts2);
    }

    public boolean checkDeep() {
        ArrayList<Integer> sigs = new ArrayList<>();
        ArrayList<Integer> afterSigs = new ArrayList<>();

        for (City city : this.cities) {
            sigs.add(city.getSignature().hashCode());
            afterSigs.add(city.getAfterSignature().hashCode());
        }

        Collections.sort(sigs);
        Collections.sort(afterSigs);

        return sigs.equals(afterSigs);
    }

    public void all() {
        int optimalRouteCount = this.cityCount * (this.cityCount - 1) / 4;
        System.out.println(this.routes.size() + " -> " + optimalRouteCount + " routes");

        ArrayList<int[]> newRoutes = new ArrayList<>();

        for (City city : this.cities) {
            ArrayList<City> afterRoutes = city.getAfterRoutes();

            for (City afterRoute : afterRoutes) {
                int[] newRoute = new int[]{city.location, afterRoute.location};
                Arrays.sort(newRoute);

                boolean found = false;
                for (int[] r : newRoutes) {
                    if (Arrays.hashCode(r) == Arrays.hashCode(newRoute)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    newRoutes.add(newRoute);
                }
            }
        }

        System.out.println(newRoutes.size() + " new routes");

        CombinationGenerator generator = new CombinationGenerator(newRoutes.size(), optimalRouteCount - this.routes.size());
        ArrayList<int[]> combinationHolder = new ArrayList<>();

        boolean found = false;

        while (generator.generateNextCombination()) {
            generator.loadCombination(newRoutes, combinationHolder);

            for (int[] route : combinationHolder) {
                this.addRoute(route);
            }

            if (this.checkShallow() && this.checkDeep()) {

                found = true;

                for (int[] route : combinationHolder) {
                    System.out.print(Arrays.toString(route));
                }
                System.out.println();
                this.remap(combinationHolder);
            }

            for (int[] route : combinationHolder) {
                this.removeRoute(route);
            }

        }

        if (!found) {
            World w = new World(this.origCityCount, this.cityCount + 1, this.routes, this.outputFile);
            w.all();
        }

    }

    private HashMap<Integer, City> remap(ArrayList<int[]> newRoutes) {
        HashMap<Integer, City> oldWorld = new HashMap<>();
        HashMap<Integer, City> newWorld = new HashMap<>();

        for (City city : this.cities) {
            for (City c : this.cities) {
                oldWorld.put(c.location, c);
                newWorld.put(c.location, null);
            }
            if (this.relocate(oldWorld, newWorld, city, oldWorld)) {
                // poate fi format din mai multe bucati
                System.out.println("OK");
                for (City c : this.cities) {
                    System.out.println(newWorld.get(c.location).location);
                }
                break;
            } else {
                System.out.println("NOT");
            }
        }

//        try {
//            PrintWriter writer = new PrintWriter(this.outputFile, "UTF-8");
//            
//            writer.println(this.cityCount - this.origCityCount + " " + newRoutes.size());
//            
//            for (int[] route : newRoutes) {
//                writer.println(route[0] + " " + route[1]);
//            }
//            
//            
//            for (int loc : newWorld) {
//                writer.println(loc);
//            }
//            
//            writer.close();
//            
//        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
//            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(this.cityCount - this.origCityCount + " " + newRoutes.size());
//        for (int loc : newWorld) {
//            System.out.println(loc);
//        }
        return newWorld;
    }

    private boolean relocate(HashMap<Integer, City> oldWorld, HashMap<Integer, City> newWorld, City newCity, HashMap<Integer, City> oldRoutes) {
        System.out.print("Relocate " + (newCity.location + 1) + "[");
        for (City c: oldRoutes.values()) {
            if (c != null ) System.out.print(c.location + 1);
            System.out.print(" ");
        }
        System.out.println("]");

        // run through all remaining old locations
        for (City oldCity : oldRoutes.values()) {

            if (oldCity == null || newWorld.getOrDefault(oldCity.location, null) != null) {
                continue;
            }

            if (oldCity.getSignature().hashCode() == newCity.getAfterSignature().hashCode()) {
                // Remove it so we know it's assigned
                oldWorld.put(newCity.location, null);

                newWorld.put(oldCity.location, newCity);

                if (!this.fit(oldWorld, newWorld, newCity, oldCity)) {
                    oldWorld.put(newCity.location, newCity);
                    newWorld.put(oldCity.location, null);
                    return false;
                }
            }

        }
        return true;
    }

    private boolean fit(HashMap<Integer, City> oldWorld, HashMap<Integer, City> newWorld, City newCity, City oldCity) {
        System.out.print("Fit " + (newCity.location + 1) + " " + (oldCity.location + 1));

        HashMap<Integer, City> oldRoutes = new HashMap();
        for (City oldRoute : oldCity.getRoutes()) {
            oldRoutes.put(oldRoute.location, oldRoute);
        }

        for (City newRoute : newCity.getAfterRoutes()) {

            if (oldWorld.getOrDefault(newRoute.location, null) != null) {
                this.relocate(oldWorld, newWorld, newRoute, oldRoutes);
            } else {
                boolean found = false;
                for (City oldRoute : oldCity.getRoutes()) {
                    if (newWorld.getOrDefault(oldRoute.location, null) != newRoute) {
                        oldRoutes.put(oldRoute.location, null);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("FALSE");
                    return false;
                }
            }
        }

        System.out.println("TRUE");
        return true;
    }

}
