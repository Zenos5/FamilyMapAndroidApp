package edu.byu.cs240.familymap.model;

/**
 * Utility functions
 */
public class Utils {

    /**
     * Returns google map value of a map type
     *
     * @param mapType type of map
     * @return integer associated with map type
     */
    public static int toGoogleMapType(MapType mapType) {
        int googleMapValue = 0;
        switch (mapType) {
            case ROADMAP:
                break;
            case SATELLITE:
                googleMapValue = 1;
                break;
            case HYBRID:
                googleMapValue = 2;
                break;
            case TERRAIN:
                googleMapValue = 3;
                break;
        }
        return googleMapValue;
    }
}
