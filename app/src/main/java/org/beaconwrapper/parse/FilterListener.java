package org.beaconwrapper.parse;

import java.util.List;

public interface FilterListener<T> {

    void onResponse(List<T> filteredData);

    void onError(String errorMsg);
}
