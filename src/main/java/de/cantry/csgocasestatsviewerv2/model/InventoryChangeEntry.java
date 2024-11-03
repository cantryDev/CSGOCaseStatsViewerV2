package de.cantry.csgocasestatsviewerv2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryChangeEntry {

    private String event;

    private String partner;

    private long time;

    private List<Item> itemsAdded;

    private List<Item> itemsRemoved;


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Item> getItemsAdded() {
        return itemsAdded == null ? new ArrayList<>() : itemsAdded;
    }

    public void setItemsAdded(List<Item> itemsAdded) {
        this.itemsAdded = itemsAdded;
    }

    public List<Item> getItemsRemoved() {
        return itemsRemoved == null ? new ArrayList<>() : itemsRemoved;
    }

    public void setItemsRemoved(List<Item> itemsRemoved) {
        this.itemsRemoved = itemsRemoved;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryChangeEntry that = (InventoryChangeEntry) o;
        if (time == that.time && Objects.equals(event, that.event) && Objects.equals(partner, that.partner)) {
            var addedEqual = false;
            var removedEqual = false;
            if (itemsAdded != null && that.itemsAdded != null) {
                addedEqual = Objects.equals(itemsAdded.stream().map(Item::getAssetID).collect(Collectors.toList()), that.itemsAdded.stream().map(Item::getAssetID).collect(Collectors.toList()));
            }
            if (itemsRemoved != null && that.itemsRemoved != null) {
                removedEqual = Objects.equals(itemsRemoved.stream().map(Item::getAssetID).collect(Collectors.toList()), that.itemsRemoved.stream().map(Item::getAssetID).collect(Collectors.toList()));
            }
            if (itemsAdded == null && that.itemsAdded == null) {
                removedEqual = true;
            }
            if (itemsRemoved == null && that.itemsRemoved == null) {
                removedEqual = true;
            }
            return addedEqual && removedEqual;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, partner, time, itemsAdded, itemsRemoved);
    }

    @Override
    public String toString() {
        return "InventoryChangeEntry{" +
                "event='" + event + '\'' +
                ", partner='" + partner + '\'' +
                ", time=" + time +
                ", itemsAdded=" + ((itemsAdded == null) ? 0 : itemsAdded) +
                ", itemsRemoved=" + ((itemsRemoved == null) ? 0 : itemsRemoved) +
                '}';
    }
}
