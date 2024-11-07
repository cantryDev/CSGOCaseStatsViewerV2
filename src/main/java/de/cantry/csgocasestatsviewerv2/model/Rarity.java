package de.cantry.csgocasestatsviewerv2.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.cantry.csgocasestatsviewerv2.exception.GlobalException;

import java.util.Arrays;

public enum Rarity {
    Grey(1, "Rarity_Common", ""),
    Light_blue(2, "Rarity_Uncommon", ""),
    Blue(3, "Rarity_Rare", "\033[0;34m"),
    Purple(4, "Rarity_Mythical", "\033[0;35m"),
    Pink(5, "Rarity_Legendary", ""),
    Red(6, "Rarity_Ancient", "\033[0;31m"),
    Gold(7, "Rarity_Contraband", "\033[0;33m");

    final int asNumber;
    final String ansiColor;
    final String internalName;

    Rarity(int i, String internalName, String ansiColor) {
        this.asNumber = i;
        this.internalName = internalName;
        this.ansiColor = ansiColor;
    }

    public static Rarity fromNumber(int number) {
        return Rarity.values()[number - 1];
    }

    public static Rarity fromDescription(JsonObject jsonObject) {
        String itemRarity = "";
        boolean unusual = false;
        for (JsonElement element : jsonObject.get("tags").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            if (object.get("category").getAsString().equals("Rarity")) {
                itemRarity = object.get("internal_name").getAsString();
            } else if (object.get("category").getAsString().equals("Quality")) {
                if (object.get("internal_name").getAsString().equals("unusual")) {
                    unusual = true;
                }
            }
        }
        if (unusual && itemRarity.startsWith("Rarity_Ancient")) {
            return Rarity.Gold;
        }
        String finalItemRarity = itemRarity.replace("_Weapon", "").replace("_Character", "");
        var rarityOpt = Arrays.stream(values()).filter(rarity -> rarity.internalName.equals(finalItemRarity)).findFirst();
        if (rarityOpt.isEmpty()) {
            throw new GlobalException("Failed to get rarity from item: " + jsonObject);
        } else {
            return rarityOpt.get();
        }
    }

    public int getAsNumber() {
        return asNumber;
    }

    public String getInternalName(){
        return internalName;
    }

    public String getAnsiColor() {
        return ansiColor;
    }

    public String getAnsiReset() {
        return "\033[0m";
    }

}
