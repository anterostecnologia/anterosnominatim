package br.com.anteros.nominatim.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.jts.JtsAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.anteros.nominatim.client.request.AnterosNominatimLookupRequest;
import br.com.anteros.nominatim.client.request.AnterosNominatimReverseRequest;
import br.com.anteros.nominatim.client.request.AnterosNominatimSearchRequest;
import br.com.anteros.nominatim.client.request.paramhelper.OsmType;
import br.com.anteros.nominatim.gson.ArrayOfAddressElementsDeserializer;
import br.com.anteros.nominatim.gson.ArrayOfPolygonPointsDeserializer;
import br.com.anteros.nominatim.gson.BoundingBoxDeserializer;
import br.com.anteros.nominatim.gson.PolygonPointDeserializer;
import br.com.anteros.nominatim.model.Address;
import br.com.anteros.nominatim.model.BoundingBox;
import br.com.anteros.nominatim.model.Element;
import br.com.anteros.nominatim.model.PolygonPoint;

/**
 * An implementation of the Nominatim Api Service.
 * 
 */
public final class AnterosJsonNominatimClient implements AnterosNominatimClient {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnterosJsonNominatimClient.class);
    
    /** The default nominatim base URL. */
    private static final String DEFAULT_BASE_URL = "https://nominatim.openstreetmap.org/";
    
    /** UTF-8 encoding.*/
    public static final String ENCODING_UTF_8 = "UTF-8";

    /** Gson instance for Nominatim API calls. */
    private final Gson gsonInstance;

    /** The url to make search queries. */
    private final String searchUrl;

    /** The url for reverse geocoding. */
    private final String reverseUrl;
    
    /** The url for address lookup. */
    private final String lookupUrl;

    /** The default search options. */
    private final AnterosNominatimOptions defaults;

    /** The HTTP client. */
    private HttpClient httpClient;

    /** The default response handler for search requests. */
    private AnterosNominatimResponseHandler<List<Address>> defaultSearchResponseHandler;

    /** The default response handler for reverse geocoding requests. */
    private AnterosNominatimResponseHandler<Address> defaultReverseGeocodingHandler;
    
    /** The default response handler for lookup requests. */
    private AnterosNominatimResponseHandler<List<Address>> defaultLookupHandler;

    /**
     * Creates the json nominatim client with the default base URL ({@value #DEFAULT_BASE_URL}.
     * 
     * @param httpClient
     *            an HTTP client
     * @param email
     *            an email to add in the HTTP requests parameters to "sign" them
     */
    public AnterosJsonNominatimClient(final HttpClient httpClient, final String email) {

        this(DEFAULT_BASE_URL, httpClient, email, new AnterosNominatimOptions());
    }

    /**
     * Creates the json nominatim client with the default base URL ({@value #DEFAULT_BASE_URL}.
     * 
     * @param httpClient
     *            an HTTP client
     * @param email
     *            an email to add in the HTTP requests parameters to "sign" them
     * @param defaults
     *            defaults options, they override null valued requests options
     */
    public AnterosJsonNominatimClient(final HttpClient httpClient, final String email, final AnterosNominatimOptions defaults) {

        this(DEFAULT_BASE_URL, httpClient, email, defaults);
    }

    /**
     * Creates the json nominatim client.
     * 
     * @param baseUrl
     *            the nominatim server url
     * @param httpClient
     *            an HTTP client
     * @param email
     *            an email to add in the HTTP requests parameters to "sign" them (see
     *            https://wiki.openstreetmap.org/wiki/Nominatim_usage_policy)
     */
    public AnterosJsonNominatimClient(final String baseUrl, final HttpClient httpClient, final String email) {

        this(baseUrl, httpClient, email, new AnterosNominatimOptions());
    }

    /**
     * Creates the json nominatim client.
     * 
     * @param baseUrl
     *            the nominatim server url
     * @param httpClient
     *            an HTTP client
     * @param email
     *            an email to add in the HTTP requests parameters to "sign" them (see
     *            https://wiki.openstreetmap.org/wiki/Nominatim_usage_policy)
     * @param defaults
     *            defaults options, they override null valued requests options
     */
    public AnterosJsonNominatimClient(final String baseUrl, final HttpClient httpClient, final String email, final AnterosNominatimOptions defaults) {

        String emailEncoded;
        try {
            emailEncoded = URLEncoder.encode(email, ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            emailEncoded = email;
        }
        this.searchUrl = String.format("%s/search?format=jsonv2&email=%s", baseUrl.replaceAll("/$", ""), emailEncoded);
        this.reverseUrl = String.format("%s/reverse?format=jsonv2&email=%s", baseUrl.replaceAll("/$", ""), emailEncoded);
        this.lookupUrl = String.format("%s/lookup?format=json&email=%s", baseUrl.replaceAll("/$", ""), emailEncoded);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("API search URL: {}", searchUrl);
            LOGGER.debug("API reverse URL: {}", reverseUrl);
        }

        this.defaults = defaults;

        // prepare gson instance
        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Element[].class, new ArrayOfAddressElementsDeserializer());
        gsonBuilder.registerTypeAdapter(PolygonPoint.class, new PolygonPointDeserializer());
        gsonBuilder.registerTypeAdapter(PolygonPoint[].class, new ArrayOfPolygonPointsDeserializer());
        gsonBuilder.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());

        gsonBuilder.registerTypeAdapterFactory(new JtsAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new GeometryAdapterFactory());

        gsonInstance = gsonBuilder.create();

        // prepare httpclient
        this.httpClient = httpClient;
        defaultSearchResponseHandler = new AnterosNominatimResponseHandler<List<Address>>(gsonInstance, new TypeToken<List<Address>>() {
        }.getType());
        defaultReverseGeocodingHandler = new AnterosNominatimResponseHandler<Address>(gsonInstance, Address.class);
        defaultLookupHandler = new AnterosNominatimResponseHandler<List<Address>>(gsonInstance, new TypeToken<List<Address>>() {
        }.getType());
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#search(br.com.anteros.nominatim.client.request.AnterosNominatimSearchRequest)
     */
    @Override
    public List<Address> search(final AnterosNominatimSearchRequest search) throws IOException {

        defaults.mergeTo(search);
        final String apiCall = String.format("%s&%s", searchUrl, search.getQueryString());
        LOGGER.debug("search url: {}", apiCall);
        final HttpGet req = new HttpGet(apiCall);
        return httpClient.execute(req, defaultSearchResponseHandler);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#getAddress(br.com.anteros.nominatim.client.request.AnterosNominatimReverseRequest)
     */
    @Override
    public Address getAddress(final AnterosNominatimReverseRequest reverse) throws IOException {

        final String apiCall = String.format("%s&%s", reverseUrl, reverse.getQueryString());
        LOGGER.debug("reverse geocoding url: {}", apiCall);
        final HttpGet req = new HttpGet(apiCall);
        return httpClient.execute(req, defaultReverseGeocodingHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#lookupAddress(br.com.anteros.nominatim.client.request.AnterosNominatimLookupRequest)
     */
    @Override
    public List<Address> lookupAddress(final AnterosNominatimLookupRequest lookup) throws IOException {

        final String apiCall = String.format("%s&%s", lookupUrl, lookup.getQueryString());
        LOGGER.debug("lookup url: {}", apiCall);
        final HttpGet req = new HttpGet(apiCall);
        return httpClient.execute(req, defaultLookupHandler);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#search(java.lang.String)
     */
    @Override
    public List<Address> search(final String query) throws IOException {

        final AnterosNominatimSearchRequest q = new AnterosNominatimSearchRequest();
        q.setQuery(query);
        return this.search(q);
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#getAddress(double, double)
     */
    @Override
    public Address getAddress(final double longitude, final double latitude) throws IOException {

        final AnterosNominatimReverseRequest q = new AnterosNominatimReverseRequest();
        q.setQuery(longitude, latitude);
        return this.getAddress(q);
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#getAddress(double, double, int)
     */
    @Override
    public Address getAddress(final double longitude, final double latitude, final int zoom)
            throws IOException {

        final AnterosNominatimReverseRequest q = new AnterosNominatimReverseRequest();
        q.setQuery(longitude, latitude);
        q.setZoom(zoom);
        return this.getAddress(q);
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#getAddress(int, int)
     */
    @Override
    public Address getAddress(final int longitudeE6, final int latitudeE6) throws IOException {

        return this.getAddress((double) (longitudeE6 / 1E6), (double) (latitudeE6 / 1E6));
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#getAddress(String, long)
     */
    @Override
    public Address getAddress(final String type, final long id) throws IOException {

        final AnterosNominatimReverseRequest q = new AnterosNominatimReverseRequest();
        q.setQuery(OsmType.from(type), id);
        return this.getAddress(q);
    }

    /**
     * {@inheritDoc}
     * 
     * @see br.com.anteros.nominatim.client.AnterosNominatimClient#lookupAddress(java.util.List)
     */
    @Override
    public List<Address> lookupAddress(final List<String> typeId) throws IOException {

        final AnterosNominatimLookupRequest q = new AnterosNominatimLookupRequest();
        q.setQuery(typeId);
        return this.lookupAddress(q);
    }
}
