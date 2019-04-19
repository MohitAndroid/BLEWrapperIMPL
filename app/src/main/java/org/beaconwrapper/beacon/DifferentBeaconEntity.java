package org.beaconwrapper.beacon;

/**
 * <p>The class is specifically design for checking any new difference is happened while publishing
 * result via callback.</p>
 * <p>There are two possibilities</p>
 * <p>
 *     <pre>
 *         1st : if no difference is found while publishing result than , no need to published it
 *         2nd : if difference is found while publishing result than , just publish it out
 *     </pre>
 * </p>
 */
public class DifferentBeaconEntity {

    private String beaconKey;
    private String beaconName;


    /**
     * Instantiates a new Different beacon entity.
     *
     * @param beaconKey  the beacon key represent beacon bluetooth address
     * @param beaconName the beacon name represent beacon payload
     */
    public DifferentBeaconEntity(String beaconKey, String beaconName) {
        this.beaconKey = beaconKey;
        this.beaconName = beaconName;
    }

    /**
     * Instantiates a new Different beacon entity.
     */
    public DifferentBeaconEntity() {}

    /**
     * Gets beacon key.
     *
     * @return the beacon key
     */
    public String getBeaconKey() {
        return beaconKey;
    }

    /**
     * Sets beacon key.
     *
     * @param beaconKey the beacon key
     */
    public void setBeaconKey(String beaconKey) {
        this.beaconKey = beaconKey;
    }

    /**
     * Gets beacon name.
     *
     * @return the beacon name
     */
    public String getBeaconName() {
        return beaconName;
    }

    /**
     * Sets beacon name.
     *
     * @param beaconName the beacon name
     */
    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }
}
