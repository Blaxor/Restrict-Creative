package ro.deiutzblaxo.RestrictCreative.mySQL;

import ro.deiutzblaxo.RestrictCreative.cache.LocationCache;

import java.util.ArrayList;

public class DataService implements LocationHandler {

    LocationCache locationCache;

    private final MySQLService mySQLService;

    public DataService(MySQLService mySQLService) {
        this.mySQLService = mySQLService;
        this.locationCache = new LocationCache();
    }


    @Override
    public boolean locationExists(String location) {

        Boolean value = locationCache.getCache(location);

        if (value == null) {
            value = mySQLService.locationExists(location);
            locationCache.putCache(location, value);
        }
        return value;
    }

    @Override
    public void createLocation(String location) {
        locationCache.putCache(location, true);
        mySQLService.createLocation(location);
    }

    @Override
    public void removeLocation(String location) {
        locationCache.putCache(location, false);
        mySQLService.removeLocation(location);
    }

    @Override
    public ArrayList<String> getLocations() {
        return mySQLService.getLocations();
    }

    @Override
    public int getCacheLocationsSize() {
        return locationCache.size();
    }


}
