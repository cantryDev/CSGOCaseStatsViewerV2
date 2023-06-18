package de.cantry.csgocasestatsviewerv2.util;

import de.cantry.csgocasestatsviewerv2.Main;

import static de.cantry.csgocasestatsviewerv2.util.HttpUtils.httpGet;
import static de.cantry.csgocasestatsviewerv2.util.RegexUtils.regexFindFirst;

public class UpdateCheckerUtils {

    public static boolean hasNewerVersion() {
        var pom = httpGet("https://raw.githubusercontent.com/cantryDev/CSGOCaseStatsViewerV2/master/pom.xml", "", false).body();
        try {
            var newVersion = regexFindFirst("<version>([^<]+)</version>", pom);
            if (Main.class.getPackage().getImplementationVersion() == null) {
                return false;
            }
            var currentVersion = Main.class.getPackage().getImplementationVersion();
            if (Double.parseDouble(newVersion.replace(".", "")) > Double.parseDouble(currentVersion.replace(".", ""))) {
                System.out.println("New Version available.");
                System.out.println("Current version: " + currentVersion);
                System.out.println("New version" + newVersion);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
