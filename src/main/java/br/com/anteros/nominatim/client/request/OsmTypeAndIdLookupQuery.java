package br.com.anteros.nominatim.client.request;


import java.util.List;

import br.com.anteros.nominatim.client.request.paramhelper.ListSerializer;
import br.com.anteros.nominatim.client.request.paramhelper.QueryParameter;

/**
 * Holds OSM TYPE and ID of the lookup request.
 * <p>
 * Attributes documentation was extracted from <a href="https://wiki.openstreetmap.org/wiki/Nominatim">Nominatim Wiki</a>
 * page on September 1st, 2015.
 * 
 */
public class OsmTypeAndIdLookupQuery extends LookupQuery {

    /**
     * List of type and id of the elements to lookup.
     */
    @QueryParameter(value = "osm_ids=%s", serializer = ListSerializer.class)
    private List<String> typeId;

    /**
     * @param typeId
     *            list of type and id of the elements to lookup
     */
    public OsmTypeAndIdLookupQuery(final List<String> typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the typeId
     */
    public List<String> getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            list of type and id of the elements to lookup set
     */
    public void setTypeId(List<String> typeId) {
        this.typeId = typeId;
    }
}
