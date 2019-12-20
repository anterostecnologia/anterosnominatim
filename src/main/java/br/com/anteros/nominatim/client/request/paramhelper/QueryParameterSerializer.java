package br.com.anteros.nominatim.client.request.paramhelper;


/**
 * Serializes a value to a String representation.
 * 
 */
public interface QueryParameterSerializer {

    /**
     * Converts the input value into a string.
     * 
     * @param value
     *            the value to serialize
     * @return a string representation of the input value
     */
    String handle(Object value);
}
