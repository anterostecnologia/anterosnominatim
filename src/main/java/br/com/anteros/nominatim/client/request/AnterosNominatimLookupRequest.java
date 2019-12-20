package br.com.anteros.nominatim.client.request;


import java.util.List;

import br.com.anteros.nominatim.client.request.paramhelper.BooleanSerializer;
import br.com.anteros.nominatim.client.request.paramhelper.QueryParameter;

/**
 * Holds request parameters for a lookup request.
 * <p>
 * Attributes documentation was extracted from <a href="https://wiki.openstreetmap.org/wiki/Nominatim">Nominatim Wiki</a>
 * page on September 1st, 2015.
 * 
 */
public class AnterosNominatimLookupRequest extends AnterosNominatimRequest {

    /**
     * Preferred language order for showing search results, overrides the browser value. Either uses standard rfc2616
     * accept-language string or a simple comma separated list of language codes.
     */
    @QueryParameter("accept-language=%s")
    private String acceptLanguage;

    /** Holds the OSM lookup request. */
    @QueryParameter
    private LookupQuery query;

    /** Include a breakdown of the address into elements. */
    @QueryParameter(value = "addressdetails=%s", serializer = BooleanSerializer.class)
    private Boolean addressDetails;
    
    /**
     * Gets the preferred language order for showing search results which overrides the browser value.
     * 
     * @return the accept-language value
     */
    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    /**
     * Sets the preferred language order for showing search results, overrides the browser value.
     * 
     * @param acceptLanguage
     *            a standard rfc2616 accept-language string or a simple comma separated list of language codes
     */
    public void setAcceptLanguage(final String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    /**
     * @return the lookup query parameters
     */
    public LookupQuery getQuery() {
        return query;
    }

    /**
     * @param query
     *            the lookup query parameters to set
     */
    public void setQuery(final LookupQuery query) {
        this.query = query;
    }

    public void setQuery(final List<String> typeId) {
        this.query = new OsmTypeAndIdLookupQuery(typeId);
    }

    /**
     * Include a breakdown of the address into elements.
     * 
     * @return the addressDetails
     */
    public Boolean getAddressDetails() {
        return addressDetails;
    }

    /**
     * Include a breakdown of the address into elements.
     * 
     * @param addressDetails
     *            the addressDetails to set
     */
    public void setAddressDetails(final boolean addressDetails) {
        this.addressDetails = addressDetails;
    }
}

