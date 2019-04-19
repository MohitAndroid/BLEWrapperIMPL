package org.beaconwrapper.beacon;


import java.util.List;

/**
 * <p>This model class encapsulates final result in it.</p>
 * <p>
 *     <pre>
 *         {@field key contains beacon payload}
 *         {@field result contains beacon filtered data processed by beacon payload as key}
 *         {@field beaconDetails contains beacon info}
 *     </pre>
 * </p>
 *
 * @param <T> the type parameter belongs to a generic class which is to be filtered
 */
public class BeaconResultEntity<T> {

    private String key;
    private List<T> result;
    private IBeacon beaconDetail;


    /**
     * Instantiates a new Beacon result entity.
     *
     * @param key          the contains beacon payload
     * @param result       the contains beacon filtered data processed by beacon payload as key
     * @param beaconDetail the contains beacon info
     */
    public BeaconResultEntity(String key, List<T> result, IBeacon beaconDetail) {
        this.key = key;
        this.result = result;
        this.beaconDetail = beaconDetail;
    }

    /**
     * Instantiates a new Beacon result entity.
     */
    public BeaconResultEntity() {}

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(List<T> result) {
        this.result = result;
    }

    /**
     * Gets beacon detail.
     *
     * @return the beacon detail
     */
    public IBeacon getBeaconDetail() {
        return beaconDetail;
    }

    /**
     * Sets beacon detail.
     *
     * @param beaconDetail the beacon detail
     */
    public void setBeaconDetail(IBeacon beaconDetail) {
        this.beaconDetail = beaconDetail;
    }
}
