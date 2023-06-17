package de.cantry.csgocasestatsviewerv2.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

public enum Rarity {
    grey(1, "Rarity_Common"),
    light_blue(2, "Rarity_Uncommon"),
    blue(3, "Rarity_Rare"),
    purple(4, "Rarity_Mythical"),
    pink(5, "Rarity_Legendary"),
    red(6, "Rarity_Ancient"),
    gold(7, "");

    int asNumber;

    String internalName;

    Rarity(int i, String internalName) {
        asNumber = i;
        this.internalName = internalName;
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
        if (unusual && itemRarity.equals("Rarity_Ancient_Weapon")) {
            return Rarity.gold;
        }
        String finalItemRarity = itemRarity.replace("_Weapon","");
        return Arrays.stream(values()).filter(rarity -> rarity.internalName.equals(finalItemRarity)).findFirst().get();
    }

    public int getAsNumber() {
        return asNumber;
    }

    public String getInternalName(){
        return internalName;
    }
}
