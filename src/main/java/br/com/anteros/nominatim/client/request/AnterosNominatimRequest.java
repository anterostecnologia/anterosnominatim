package br.com.anteros.nominatim.client.request;

import br.com.anteros.nominatim.client.request.paramhelper.QueryParameter;

/**
 * Classes extending this should be able to build URL query string parts to append to nominatim server URL to make
 * requests.
 * 
 * Uses {@link QueryParameter} annotations to describe the query parameters.
 * 
 */
public abstract class AnterosNominatimRequest {

    /**
     * Generates the query string to be sent to nominatim to execute a request.
     * <p>
     * The query string <b>must</b> have URL encoded parameters.
     * <p>
     * example: <code>q=some%20city&amp;polygon_geojson=1</code>
     * 
     * @return a query string
     */
    public final String getQueryString() {
        return QueryParameterAnnotationHandler.process(this);
    }
}
