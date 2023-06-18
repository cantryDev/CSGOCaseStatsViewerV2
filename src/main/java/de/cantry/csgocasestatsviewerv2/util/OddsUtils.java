package de.cantry.csgocasestatsviewerv2.util;

import de.cantry.csgocasestatsviewerv2.model.Rarity;

import java.util.TreeMap;

public class OddsUtils {

    public static TreeMap<Rarity, Double> getOdds(Rarity startingAt, Rarity endingAt) {
        double remainingOdds = 1;
        TreeMap<Rarity, Double> odds = new TreeMap<>();
        if (startingAt == Rarity.blue && endingAt == Rarity.gold) {
            odds.put(Rarity.blue, 0.7992);
            odds.put(Rarity.purple, 0.1598);
            odds.put(Rarity.pink, 0.032);
            odds.put(Rarity.red, 0.0064);
            odds.put(Rarity.gold, 0.0026);
            return odds;
        }
        for (int i = startingAt.getAsNumber(); i <= endingAt.getAsNumber(); i++) {
            Rarity currentRarity = Rarity.fromNumber(i);
            odds.put(currentRarity, (remainingOdds / 5) * 4);
            remainingOdds /= 5;
        }
        return odds;
    }


    public static double getOdds(Rarity startingAt, Rarity endingAt, Rarity target) {
        return getOdds(startingAt, endingAt).get(target);
    }

    public static TreeMap<Rarity, Double> getOddsForUnboxType(String unboxType) {
        switch (unboxType) {
            case "Capsule":
            case "Pins Capsule":
                return getOdds(Rarity.blue, Rarity.red);
            case "Case":
                return getOdds(Rarity.blue, Rarity.gold);
            case "Patch Pack":
                return getOdds(Rarity.blue, Rarity.pink);
            case "Souvenir Package":
            case "Collection Package":
                return getOdds(Rarity.grey, Rarity.red);
            case "Music Kit Box":
                return getOdds(Rarity.blue, Rarity.blue);
        }
        return new TreeMap<>();
    }

}
