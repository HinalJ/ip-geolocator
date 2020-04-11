package geolocator;

import java.net.URL;

import java.io.IOException;

import com.google.gson.Gson;

import com.google.common.net.UrlEscapers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for obtaining geolocation information about an IP address or host
 * name. The class uses the <a href="http://ip-api.com/">IP-API.com</a>
 * service.
 */
public class GeoLocator {

    /**
     * URI of the geolocation service.
     */
    public static final String GEOLOCATOR_SERVICE_URI = "http://ip-api.com/json/";
    private static Logger logger = LoggerFactory.getLogger(GeoLocator.class);

    private static Gson GSON = new Gson();

    /**
     * Creates a <code>GeoLocator</code> object.
     */
    public GeoLocator() {}

    /**
     * Returns geolocation information about the JVM running the application.
     *
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation() throws IOException {
        logger.debug("Gets Geolocation");
        return getGeoLocation(null);
    }

    /**
     * Returns geolocation information about the IP address or host name
     * specified. If the argument is <code>null</code>, the method returns
     * geolocation information about the JVM running the application.
     *
     * @param ipAddrOrHost the IP address or host name, may be {@code null}
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        URL url;
        if (ipAddrOrHost != null) {
            ipAddrOrHost = UrlEscapers.urlPathSegmentEscaper().escape(ipAddrOrHost);
            logger.debug("IPAddOrHost is evaluated: " + ipAddrOrHost);
            url = new URL(GEOLOCATOR_SERVICE_URI + ipAddrOrHost);
            logger.info("URL is " + url);
        } else {
            url = new URL(GEOLOCATOR_SERVICE_URI);
            logger.info("URL is " + url);
        }
        String s = IOUtils.toString(url, "UTF-8");
        logger.debug("Converting url in UTF-8 encoding!");
        return GSON.fromJson(s, GeoLocation.class);
    }
    // CHECKSTYLE:OFF
    public static void main(String[] args) throws IOException {
        try {
            String arg = args.length > 0 ? args[0] : null;
            logger.debug("Obtaining command line argument");
            System.out.println(new GeoLocator().getGeoLocation(arg));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            logger.error("IOException Obtained: " + e.getMessage());
        }
    }

}
