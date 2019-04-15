package org.beaconwrapper;

import com.google.gson.annotations.Expose;

import org.beaconwrapper.beacon.BeaconKey;

public class DNAEntity {

    @Expose
    private String id;
    @BeaconKey
    private String journal;

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
