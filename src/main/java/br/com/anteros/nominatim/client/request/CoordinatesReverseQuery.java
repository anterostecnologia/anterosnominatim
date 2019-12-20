package br.com.anteros.nominatim.client.request;

import br.com.anteros.nominatim.client.request.paramhelper.DoubleSerializer;
import br.com.anteros.nominatim.client.request.paramhelper.QueryParameter;

/**
 * Holds coordinates of the reverse geocoding request.
 * <p>
 * Attributes documentation was extracted from <a href="https://wiki.openstreetmap.org/wiki/Nominatim">Nominatim Wiki</a>
 * page on October 26th, 2013.
 * 
 */
public class CoordinatesReverseQuery extends ReverseQuery {

    /** The latitude. */
    @QueryParameter(value = "lat=%s", serializer = DoubleSerializer.class)
    private Double latitude;

    /** The longitude. */
    @QueryParameter(value = "lon=%s", serializer = DoubleSerializer.class)
    private Double longitude;

    /**
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     */
    public CoordinatesReverseQuery(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @param longitudeE6
     *            the longitude
     * @param latitudeE6
     *            the latitude
     */
    public CoordinatesReverseQuery(int longitudeE6, int latitudeE6) {
        this(longitudeE6 / 1E6, latitudeE6 / 1E6);
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
