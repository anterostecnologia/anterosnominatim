package br.com.anteros.nominatim.client.request.paramhelper;

import br.com.anteros.nominatim.client.request.AnterosNominatimRequest;

/**
 * Serializes input value using {@link Object#toString()} unless the input object is an instance of
 * {@link AnterosNominatimRequest}, then it uses {@link AnterosNominatimRequest#getQueryString()}.
 * 
 */
public class ToStringSerializer implements QueryParameterSerializer {

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.request.paramhelper.QueryParameterSerializer#handle(java.lang.Object)
     */
    @Override
    public String handle(final Object value) {
        if (value instanceof AnterosNominatimRequest) {
            return ((AnterosNominatimRequest) value).getQueryString();
        } else {
            return value.toString();
        }
    }
}
