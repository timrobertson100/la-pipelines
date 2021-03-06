package au.org.ala.kvs;

import lombok.NonNull;

import java.util.Properties;

public class ALAKvConfigFactory {

    public static final String GEOCODE_PREFIX = "geocode";
    public static final String ALA_TAXONOMY_PREFIX = "ala_taxonomy";
    public static final String ALA_SPATIAL_PREFIX = "ala_spatial";
    public static final String ALA_COLLECTORY_PREFIX = "ala_collectory";
    public static final String ALA_LISTS_PREFIX = "ala_lists";
    private static final String WS_API_BASE_PATH_PROP = ".api.url";

    public static ALAKvConfig create(@NonNull Properties props) {
        String taxonomyBasePath = props.getProperty(ALA_TAXONOMY_PREFIX + WS_API_BASE_PATH_PROP);
        String spatialBasePath = props.getProperty(ALA_SPATIAL_PREFIX + WS_API_BASE_PATH_PROP);
        String collectoryBasePath = props.getProperty(ALA_COLLECTORY_PREFIX + WS_API_BASE_PATH_PROP);
        String listsBasePath = props.getProperty(ALA_LISTS_PREFIX + WS_API_BASE_PATH_PROP);
        String geocodeBasePath= props.getProperty(GEOCODE_PREFIX + WS_API_BASE_PATH_PROP);

        return ALAKvConfig.builder()
                .taxonomyBasePath(taxonomyBasePath)
                .spatialBasePath(spatialBasePath)
                .collectoryBasePath(collectoryBasePath)
                .listsBasePath(listsBasePath)
                .geocodeBasePath(geocodeBasePath)
                .build();
    }
}
