package org.beaconwrapper.parse;

import java.util.List;

/**
 * The interface Filter listener provides callbacks.
 *
 * @param <T> is type parameter in which parsed data to be converted
 */
public interface FilterListener<T> {
    /**
     * Gives parsable data list.
     *
     * @param filteredData the list of parsed filtered data
     */
    void onResponse(List<T> filteredData);

    /**
     * On error.
     *
     * @param errorMsg the error msg
     */
    void onError(String errorMsg);
}
