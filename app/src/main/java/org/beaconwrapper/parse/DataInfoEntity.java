package org.beaconwrapper.parse;

/**
 * <p>This class stores JSONObject and JSONArray info with specified order.</p>
 * <p>This class fills data through recursion process which is defined in {@link ParserListClass}
 * and later on its very useful for filtering process to provide appropriate parsing output
 * </p>
 */
public class DataInfoEntity {
    private String data;
    private String type;
    private int deepCount;
    private boolean isRelativeData = false;

    /**
     * Instantiates a new Data info entity.
     *
     * @param data      the chunk of json string
     * @param type      the type as JSON object or array
     * @param deepCount the deep count for measure ordering of recursion process
     */
    public DataInfoEntity(String data, String type, int deepCount) {
        this.data = data;
        this.type = type;
        this.deepCount = deepCount;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets deep count.
     *
     * @return the deep count
     */
    public int getDeepCount() {
        return deepCount;
    }

    /**
     * Sets deep count.
     *
     * @param deepCount the deep count
     */
    public void setDeepCount(int deepCount) {
        this.deepCount = deepCount;
    }

    /**
     * Is relative data boolean.
     *
     * @return the boolean
     */
    public boolean isRelativeData() {
        return isRelativeData;
    }

    /**
     * Sets relative data.
     *
     * @param relativeData the relative data
     */
    public void setRelativeData(boolean relativeData) {
        isRelativeData = relativeData;
    }
}
