package ro.deiutzblaxo.RestrictCreative.mySQL;

import java.util.ArrayList;

public interface LocationHandler {


    boolean locationExists(String location);

    void createLocation(String location);

    void removeLocation(String l);

    ArrayList<String> getLocations();

    int getCacheLocationsSize();

}
