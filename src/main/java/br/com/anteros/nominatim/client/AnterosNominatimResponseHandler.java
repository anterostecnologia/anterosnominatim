package br.com.anteros.nominatim.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;

import com.google.gson.Gson;

/**
 * Parses a json response from the Nominatim API for a reverse geocoding request.
 * 
 */
public final class AnterosNominatimResponseHandler<T> implements ResponseHandler<T> {

    /** Gson instance for Nominatim API calls. */
    private final Gson gsonInstance;

    /** The expected type of objects. */
    private final Type responseType;

    /**
     * Constructor.
     * 
     * @param gsonInstance
     *            the Gson instance
     * @param responseType
     *            the expected type of objects
     */
    public AnterosNominatimResponseHandler(final Gson gsonInstance, final Type responseType) {

        this.gsonInstance = gsonInstance;
        this.responseType = responseType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
     */
    @Override
    public T handleResponse(final HttpResponse response) throws IOException {

        InputStream content = null;
        final T addresses;

        try {
            final StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                throw new IOException(String.format("HTTP error: %s %s", status.getStatusCode(), status.getReasonPhrase()));
            }
            content = response.getEntity().getContent();
            addresses = gsonInstance
                    .fromJson(new InputStreamReader(content, "utf-8"), responseType);
        } finally {
            if (null != content) {
                content.close();
            }
            response.getEntity().consumeContent();
        }

        return addresses;
    }
}
