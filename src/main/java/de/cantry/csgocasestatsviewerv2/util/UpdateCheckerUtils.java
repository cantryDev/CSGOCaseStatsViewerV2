package de.cantry.csgocasestatsviewerv2.util;

import de.cantry.csgocasestatsviewerv2.Main;

import static de.cantry.csgocasestatsviewerv2.util.HttpUtils.httpGet;
import static de.cantry.csgocasestatsviewerv2.util.RegexUtils.regexFindFirst;

public class UpdateCheckerUtils {

    public static boolean hasNewerVersion() {
        var pom = httpGet("https://raw.githubusercontent.com/cantryDev/CSGOCaseStatsViewerV2/master/pom.xml", "", false).body();
        try {
            var version = Double.valueOf(regexFindFirst("<version>([^<]+)</version>", pom).replace(".", ""));
            if (Main.class.getPackage().getImplementationVersion() == null) {
                return false;
            }
            var currentVersion = Double.valueOf(Main.class.getPackage().getImplementationVersion().replace(".", ""));
            if (version > currentVersion) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
