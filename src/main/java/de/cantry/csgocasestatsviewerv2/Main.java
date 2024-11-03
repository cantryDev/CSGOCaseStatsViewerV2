package de.cantry.csgocasestatsviewerv2;

import de.cantry.csgocasestatsviewerv2.steam.service.AnalysisService;
import de.cantry.csgocasestatsviewerv2.steam.service.DumpService;
import de.cantry.csgocasestatsviewerv2.util.TimeUtils;

import java.util.Scanner;

import static de.cantry.csgocasestatsviewerv2.util.UpdateCheckerUtils.hasNewerVersion;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DumpService dumpService = DumpService.getInstance();
        for (String arg : args) {
            if ("debug".equals(arg)) {
                dumpService.setDebug(true);
            }
        }
        AnalysisService analysisService = AnalysisService.getInstance();

        if (hasNewerVersion()) {
            System.out.println("Get the new version at: https://github.com/cantryDev/CSGOCaseStatsViewerV2/releases/latest");
            System.out.println();
            System.out.println();
        }

        int input;

        if (dumpService.hasDumps()) {
            var savedData = dumpService.getDumpData();
            System.out.println("CSGOCaseStatsViewerV2");
            System.out.println("Menu");
            System.out.printf("Available data from %1$s to %2$s%n", TimeUtils.longToStringDateConverter.format(savedData.getLowestTimestamp() * 1000), TimeUtils.longToStringDateConverter.format(savedData.getHighestTimestamp() * 1000));
            System.out.println("1. -> Dump inventory history (can take some time)");
            System.out.println("2. -> Analyse unboxing history");
            System.out.println("3. -> Analyse case drop history");
            System.out.println("4. -> Analyse Operation drop history");
            System.out.println("Type the number and press enter.");
            input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1:
                    dumpService.dump(savedData);
                    break;
                case 2:
                    analysisService.analyseUnboxings(dumpService.getDumpDirectory());
                    break;
                case 3:
                    analysisService.analyseCaseDrops(dumpService.getDumpDirectory());
                    break;
                case 4:
                    analysisService.analyseOperationDrops(dumpService.getDumpDirectory());
                    break;
            }
        } else {
            System.out.println("Dump inventory history (can take some time)");
            input = 1;
            dumpService.dump(null);
        }
    }

}
