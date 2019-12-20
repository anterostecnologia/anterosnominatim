package br.com.anteros.nominatim.client.request;

import br.com.anteros.nominatim.client.request.paramhelper.OsmType;
import br.com.anteros.nominatim.client.request.paramhelper.QueryParameter;

/**
 * Holds OSM TYPE and ID of the reverse geocoding request.
 * <p>
 * Attributes documentation was extracted from <a href="https://wiki.openstreetmap.org/wiki/Nominatim">Nominatim Wiki</a>
 * page on October 26th, 2013.
 * 
 */
public class OsmTypeAndIdReverseQuery extends ReverseQuery {

    /**
     * The type of the searched element.
     */
    @QueryParameter("osm_type=%s")
    private OsmType type;

    /**
     * The id of the searched element.
     */
    @QueryParameter("osm_id=%s")
    private long id;

    /**
     * @param type
     *            the type of the searched element
     * @param id
     *            the id of the searched element
     */
    public OsmTypeAndIdReverseQuery(final OsmType type, final long id) {
        this.type = type;
        this.id = id;
    }

    /**
     * @return the osm type
     */
    public OsmType getType() {
        return type;
    }

    /**
     * @param type
     *            the osm type to set
     */
    public void setType(OsmType type) {
        this.type = type;
    }

    /**
     * @return the osm id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the osm id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}
