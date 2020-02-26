package au.org;

import au.org.ala.kvs.cache.SamplingCache2;
import au.org.ala.kvs.cache.SamplingKeyValueStoreFactory;
import au.org.ala.kvs.client.LatLng;
import org.gbif.kvs.KeyValueStore;

import java.util.Map;

public class SamplingTest {


    public static void main(String[] args) throws Exception {

        SamplingKeyValueStoreFactory.setupFor2(args[0]);

        SamplingCache2 cache = SamplingKeyValueStoreFactory.getForDataset2(args[0]);

        Map<String, String> samples = cache.getSamples(Double.parseDouble(args[1]),Double.parseDouble(args[2]));
        if(samples != null) {
            for (Map.Entry<String, String> entry : samples.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        } else {
            System.out.println("No match for lat lng");
        }
    }
}
