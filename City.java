/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazon;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author walter
 */
public class City {

    public final int location;
    private final ArrayList<City> routes = new ArrayList();
    private ArrayList<City> afterRoutes = new ArrayList();

    public City(int location) {
        this.location = location;
    }

    public void init(ArrayList<City> allCities) {
        this.afterRoutes = allCities;
        this.afterRoutes.remove(this);
    }

    public void addRoute(City destination) {
        this.routes.add(destination);
        this.afterRoutes.remove(destination);
    }

    public void removeRoute(City destination) {
        this.routes.remove(destination);
        this.afterRoutes.add(destination);
    }

    
    public int routeCount() {
        return this.routes.size();
    }

    public int afterRouteCount() {
        return this.afterRoutes.size();
    }

    public ArrayList<City> getRoutes() {
        return new ArrayList<>(this.routes);
    }

    public ArrayList<City> getAfterRoutes() {
        return new ArrayList<>(this.afterRoutes);
    }

    public ArrayList<Integer> getSignature() {
        ArrayList<Integer> result = new ArrayList();
        for (City city : this.routes) {
            result.add(city.routeCount());
        }
        Collections.sort(result);
        return result;
    }

    public ArrayList<Integer> getAfterSignature() {
        ArrayList<Integer> result = new ArrayList();
        for (City city : this.afterRoutes) {
            result.add(city.afterRouteCount());
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public String toString() {
        ArrayList<Integer> routeIds = new ArrayList<>();
        for (City city : this.routes) {
            routeIds.add(city.location);
        }

        ArrayList<Integer> afterRouteIds = new ArrayList<>();
        for (City city : this.afterRoutes) {
            afterRouteIds.add(city.location);
        }
        return "Location " + this.location + " " + routeIds + " " + this.getSignature() + " | " + afterRouteIds + " " + this.getAfterSignature();
    }
}
