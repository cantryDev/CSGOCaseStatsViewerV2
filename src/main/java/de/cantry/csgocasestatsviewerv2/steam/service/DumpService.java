package de.cantry.csgocasestatsviewerv2.steam.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.cantry.csgocasestatsviewerv2.exception.GlobalException;
import de.cantry.csgocasestatsviewerv2.model.DumpModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static de.cantry.csgocasestatsviewerv2.util.HttpUtils.httpGet;
import static de.cantry.csgocasestatsviewerv2.util.RegexUtils.regexFindAll;
import static de.cantry.csgocasestatsviewerv2.util.RegexUtils.regexFindFirst;
import static de.cantry.csgocasestatsviewerv2.util.TimeUtils.longToStringDateConverter;
import static de.cantry.csgocasestatsviewerv2.util.TimeUtils.stringToLongDateConverter;

public class DumpService {

    private static DumpService instance;

    private final File dumpDirectory = new File("data/");

    private final Gson gson;

    private String cookies;

    private String inventoryUrl;

    private String sessionid;

    public DumpService() {
        this.gson = new Gson();
        if (!dumpDirectory.exists()) {
            dumpDirectory.mkdir();
        }
    }

    public boolean hasDumps() {
        return dumpDirectory.exists() && Objects.requireNonNull(dumpDirectory.listFiles((dir, name) -> name.endsWith("dump"))).length > 0;
    }

    public void dump(DumpModel dumpModel) {
        requireCookies();
        long start = 0;
        long current = 0;
        long end = 0;
        String s = "0";
        String time_frac = "0";

        if (dumpModel == null) {
            System.out.println("Start getting Data from End (Newest) to Start (Oldest)");
            start = 9999999999L;
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select time:");
            System.out.printf("1) From %1$s To %2$s%n", longToStringDateConverter.format(new Date(dumpModel.getLowestTimestamp() * 1000)), "Start (Oldest)");
            System.out.printf("2) From %1$s To %2$s%n", "Today (Newest)", longToStringDateConverter.format(new Date(dumpModel.getHighestTimestamp() * 1000)));
            System.out.println("3) Start from manual Cursor");
            do {
                switch (scanner.nextLine()) {
                    case "1":
                        start = dumpModel.getLowestTimestamp();
                        end = 0;
                        if (dumpModel.getCursor() != null) {
                            if (dumpModel.getCursor().has("s")) {
                                s = dumpModel.getCursor().get("s").getAsString();
                            }
                            if (dumpModel.getCursor().has("time_frac")) {
                                time_frac = dumpModel.getCursor().get("time_frac").getAsString();
                            }

                        }
                        break;
                    case "2":
                        start = 9999999999L;
                        end = dumpModel.getHighestTimestamp();
                        break;
                    case "3":
                        System.out.println("Enter Cursor:");

                        var cursorInput = scanner.nextLine();
                        var cursor = gson.fromJson(cursorInput, JsonObject.class);
                        if (!cursor.has("s") || !cursor.has("time_frac") || !cursor.has("time")) {
                            throw new GlobalException("Cursor input invalid. Input:" + cursorInput);
                        }
                        s = cursor.get("s").getAsString();
                        time_frac = cursor.get("time_frac").getAsString();
                        start = cursor.get("time").getAsLong();
                        break;
                }
            } while (start == 0);
        }
        current = start;

        int errorCounter = 0;

        while (current > end) {
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            var dumpResult = dumpFromTime(current, time_frac, s);

            if (dumpResult.getLowestTimestamp() != 0 && dumpResult.getHighestTimestamp() != 0 && dumpResult.isSuccess()) {
                System.out.printf("Fetched Data from %1$s to %2$s%n", longToStringDateConverter.format(new Date(dumpResult.getHighestTimestamp() * 1000)), longToStringDateConverter.format(new Date(dumpResult.getLowestTimestamp() * 1000)));
                errorCounter = 0;
                safeDump(dumpResult.getSource());
            } else {
                errorCounter++;
                System.out.println("No Data found.... Retrying (" + errorCounter + ") /" + 5);
                if (errorCounter >= 5) {
                    throw new GlobalException("Couldnt find more data exiting");
                }
                continue;
            }

            if (dumpResult.getCursor() != null) {
                s = dumpResult.getCursor().get("s").getAsString();
                time_frac = dumpResult.getCursor().get("time_frac").getAsString();
                current = dumpResult.getCursor().get("time").getAsLong();
            } else {
                System.out.println("Failed to find cursor for more data.");
                System.out.println("If it should find more data after: " + longToStringDateConverter.format(new Date(dumpResult.getLowestTimestamp() * 1000)));
                System.out.println("Please restart the scanning after getting the cursor manual from: " + inventoryUrl + "?start_time=" + dumpResult.getLowestTimestamp());
                return;
            }
        }

    }

    private void safeDump(String source) {
        File resultFile = new File(dumpDirectory.getAbsolutePath() + File.separator + UUID.randomUUID() + ".dump");
        try {
            Files.write(resultFile.toPath(), source.getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new GlobalException("Failed to write dump: " + resultFile.getPath(), e);
        }
    }

    public DumpModel dumpFromTime(long time, String time_frac, String s) {

        String requestUrl = inventoryUrl + "?ajax=1&cursor%5Btime%5D=" + time + "&cursor%5Btime_frac%5D=" + time_frac + "&cursor%5Bs%5D=" + s + "&sessionid=" + sessionid + "&app%5B%5D=" + 730;

        var steamResponse = httpGet(requestUrl, cookies, false);

        return getDumpModelFromJSONSource(steamResponse.body());
    }

    public DumpModel getDumpModelFromJSONSource(String source) {
        DumpModel dumpModel = new DumpModel();
        dumpModel.setSource(source);

        JsonObject steamResponse;
        try {
            steamResponse = gson.fromJson(source, JsonObject.class);
        } catch (Exception e) {
            throw new GlobalException("Failed to parse data from source", e);
        }
        List<Long> currentTimestamps = new ArrayList<>();

        List<String> dates = regexFindAll("tradehistory_date\">([^<]+)<", steamResponse.get("html").getAsString());
        List<String> times = regexFindAll("tradehistory_timestamp\">([^<]+)<", steamResponse.get("html").getAsString());
        if (dates.size() == times.size() && dates.size() != 0) {
            for (int i = 0; i < dates.size(); i++) {
                String dateString = (dates.get(i).replace("\t", "").replace("\r", "").replace("\n", "") + " " + times.get(i).toUpperCase().replace("\t", "").replace("\r", "").replace("\n", ""));
                ZonedDateTime dateTime = ZonedDateTime.parse(dateString, stringToLongDateConverter.withZone(ZoneId.of("GMT")));
                currentTimestamps.add(dateTime.toInstant().getEpochSecond());
            }
        }

        dumpModel.setSuccess("True".equalsIgnoreCase(steamResponse.get("success").getAsString()));

        if (currentTimestamps.size() > 0) {
            dumpModel.setLowestTimestamp(currentTimestamps.stream().min(Long::compare).get());
            dumpModel.setHighestTimestamp(currentTimestamps.stream().max(Long::compare).get());
            dumpModel.setCursor(steamResponse.get("cursor") == null ? null : steamResponse.get("cursor").getAsJsonObject());
            return dumpModel;
        }

        throw new GlobalException("Failed to parse data from source");
    }

    public DumpModel getDumpData() {
        List<DumpModel> allDumpModels = new ArrayList<>();
        DumpModel dumpModel = new DumpModel();

        Arrays.stream(dumpDirectory.listFiles((dir, name) -> name.endsWith("dump"))).forEach(dumpFile -> {
            DumpModel currentModel;
            try {
                currentModel = getDumpModelFromJSONSource(String.join("", Files.readAllLines(dumpFile.toPath())));
            } catch (IOException e) {
                throw new GlobalException("Failed to read source from file: " + dumpFile.toPath(), e);
            }
            allDumpModels.add(currentModel);
        });

        if (allDumpModels.size() > 0) {
            dumpModel.setLowestTimestamp(allDumpModels.stream().min(Comparator.comparingLong(DumpModel::getLowestTimestamp)).get().getLowestTimestamp());
            dumpModel.setHighestTimestamp(allDumpModels.stream().max(Comparator.comparingLong(DumpModel::getHighestTimestamp)).get().getHighestTimestamp());
            dumpModel.setCursor(allDumpModels.stream().min(Comparator.comparingLong(DumpModel::getLowestTimestamp)).get().getCursor());
            return dumpModel;
        }
        throw new GlobalException("Failed to get information about old data.");
    }

    public void requireCookies() {
        if (cookies == null) {
            System.out.println("Why does it need your cookies?");
            System.out.println("It needs your cookies to request your inventory history from steamcommunity.com/my/inventoryhistory");
            System.out.println("If you dont know how to get your cookies check https://github.com/cantryDev/CSGOCaseStatsViewer for instructions");
            System.out.println("Please paste your cookies and press enter");
            Scanner in = new Scanner(System.in);
            setCookies(in.nextLine());
        }
        try {
            var base = httpGet("https://steamcommunity.com/my", cookies, true);

            setInventoryUrl(regexFindFirst("href=\"(https://steamcommunity.com/[^/]+/[^/]+/inventory/)\"", base.body()).replace("inventory", "inventoryhistory"));
            setSessionid(regexFindFirst("g_sessionID = \"([^\"]+)\"", base.body()));

            System.out.println("InventoryUrl:" + getInventoryUrl());
            System.out.println("SessionId:" + getSessionid());
        } catch (Exception e) {
            throw new GlobalException("Failed to retrieve all needed account information. Cookies invalid?", e);
        }

    }

    public File getDumpDirectory() {
        return dumpDirectory;
    }

    public String getInventoryUrl() {
        return inventoryUrl;
    }

    public void setInventoryUrl(String inventoryUrl) {
        this.inventoryUrl = inventoryUrl;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public void setCookies(String cookies) {
        if (!cookies.startsWith("Cookie")) {
            cookies = "Cookies: " + cookies;
        }
        this.cookies = cookies;
    }

    public static DumpService getInstance() {
        if (instance == null) {
            instance = new DumpService();
        }
        return instance;
    }


}
