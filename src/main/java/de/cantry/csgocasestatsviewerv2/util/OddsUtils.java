package de.cantry.csgocasestatsviewerv2.util;

import de.cantry.csgocasestatsviewerv2.model.Rarity;

import java.util.TreeMap;

public class OddsUtils {

    public static TreeMap<Rarity, Double> getOdds(Rarity startingAt, Rarity endingAt) {
        TreeMap<Rarity, Double> odds = new TreeMap<>();
        if (startingAt == Rarity.Blue && endingAt == Rarity.Gold) {
            odds.put(Rarity.Blue, 0.7923);
            odds.put(Rarity.Purple, 0.15985);
            odds.put(Rarity.Pink, 0.03197);
            odds.put(Rarity.Red, 0.00639);
            odds.put(Rarity.Gold, 0.00256);
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
                return getOdds(Rarity.Blue, Rarity.Red);
            case "Case":
                return getOdds(Rarity.Blue, Rarity.Gold);
            case "Patch Pack":
                return getOdds(Rarity.Blue, Rarity.Pink);
            case "Souvenir Package":
            case "Collection Package":
                return getOdds(Rarity.Grey, Rarity.Red);
            case "Music Kit Box":
                return getOdds(Rarity.Blue, Rarity.Blue);
        }
        return new TreeMap<>();
    }

}
