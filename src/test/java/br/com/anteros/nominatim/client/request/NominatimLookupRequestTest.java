package br.com.anteros.nominatim.client.request;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NominatimLookupRequestTest {

    private AnterosNominatimLookupRequest req;

    @Before
    public void reset() {
        req = new AnterosNominatimLookupRequest();
    }

    @Test
    public void simpleQuery() throws UnsupportedEncodingException {
		List<String> typeIds = new ArrayList<String>();
		typeIds.add("R146656");
		typeIds.add("W104393803");
		
        req.setQuery(typeIds);
        assertEquals("osm_ids=R146656,W104393803", req.getQueryString());
    }

    @Test
    public void simpleQueryWithAcceptLanguage() throws UnsupportedEncodingException {
    	List<String> typeIds = new ArrayList<String>();
		typeIds.add("R146656");
		typeIds.add("W104393803");
		
		req.setQuery(typeIds);
        req.setAcceptLanguage("fr_FR");
        assertEquals("accept-language=fr_FR&osm_ids=R146656,W104393803", req.getQueryString());
    }

    @Test
    public void simpleQueryWithAddress() throws UnsupportedEncodingException {
		List<String> typeIds = new ArrayList<String>();
		typeIds.add("R146656");
		typeIds.add("W104393803");
		
        req.setQuery(typeIds);
        req.setAddressDetails(true);
        assertEquals("osm_ids=R146656,W104393803&addressdetails=1", req.getQueryString());
    }

    @Test
    public void simpleQueryWithoutAddress() throws UnsupportedEncodingException {
		List<String> typeIds = new ArrayList<String>();
		typeIds.add("R146656");
		typeIds.add("W104393803");
		
        req.setQuery(typeIds);
        req.setAddressDetails(false);
        assertEquals("osm_ids=R146656,W104393803&addressdetails=0", req.getQueryString());
    }
}

