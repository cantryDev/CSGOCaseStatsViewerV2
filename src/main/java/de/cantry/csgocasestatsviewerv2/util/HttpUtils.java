package de.cantry.csgocasestatsviewerv2.util;

import de.cantry.csgocasestatsviewerv2.exception.GlobalException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpUtils {


    private static HttpClient httpClient;

    public static HttpResponse<String> httpGet(String url, String cookies, boolean followRedirects) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 7; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0")
                .setHeader("Accept-Charset", "UTF-8")
                .setHeader("Accept-Language", "en-US;")
                .setHeader("Cookie", cookies)
                .build();
        try {
            var result = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (followRedirects) {
                var location = result.headers().firstValue("Location");
                return location.map(s -> httpGet(s, cookies, false)).orElse(result);
            }
            return result;
        } catch (IOException | InterruptedException e) {
            throw new GlobalException(String.format("Failed to HTTP-GET to: %1$s", url), e);
        }
    }

    private static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();
        }
        return httpClient;
    }
}
