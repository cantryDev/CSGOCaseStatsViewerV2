package de.cantry.csgocasestatsviewerv2.model;

import com.google.gson.JsonObject;

public class DumpModel {

    private long highestTimestamp;
    private long lowestTimestamp;
    private String source;
    private JsonObject cursor;

    private boolean success;

    public long getHighestTimestamp() {
        return highestTimestamp;
    }

    public void setHighestTimestamp(long highestTimestamp) {
        this.highestTimestamp = highestTimestamp;
    }

    public long getLowestTimestamp() {
        return lowestTimestamp;
    }

    public void setLowestTimestamp(long lowestTimestamp) {
        this.lowestTimestamp = lowestTimestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public JsonObject getCursor() {
        return cursor;
    }

    public void setCursor(JsonObject cursor) {
        this.cursor = cursor;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
