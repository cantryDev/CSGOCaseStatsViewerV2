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

        // Calculate odds to be inline with official odds from Valve (CSGO China)
        // https://www.csgo.com.cn/news/gamebroad/20170911/206155.shtml
        //
        // Logic: Each tier should be 5x as rare as the previous tier (with the
        // exception of knives, case hardcoded above)
        // In the case of capsule cases with tiers blue, purple and pink,
        // odds should be 25/31 (80.645%), 5/31 (16.129%) and 1/31 (3.226%)
        // The denominator of 31 is obtained by doing 1+5+25=5^0+5^1+5^2+...
        int numRarities = endingAt.getAsNumber() - startingAt.getAsNumber() + 1;
        // Sum of n first elements in geometric series with a=1, r=5
        int totalOdds = (1 - (int) Math.pow(5, numRarities)) / (1 - 5);
        double numerator = Math.pow(5, numRarities - 1);
        for (int i = startingAt.getAsNumber(); i <= endingAt.getAsNumber(); i++) {
            Rarity currentRarity = Rarity.fromNumber(i);
            odds.put(currentRarity, numerator / totalOdds);
            numerator /= 5;
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
