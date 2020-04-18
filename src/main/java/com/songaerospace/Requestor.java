package com.songaerospace;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Requestor {
    // A class specifically handles interaction with http://deckofcardsapi.com/
    private HttpClient request_client;
    private CookieManager cookie_manager;

    private static final String API_URL = "https://deckofcardsapi.com/api/deck/";

    public Requestor() {
        this.cookie_manager = new CookieManager();
        this.request_client = HttpClient.newBuilder()
                .cookieHandler(this.cookie_manager)
                .build();
    }

    public HttpResponse PostRequest(String api_url, String post_param) throws IOException, InterruptedException
    {
        // Make an API call using POST method

        // build the request body
        ObjectMapper object_mapper = new ObjectMapper();
        String request_body = object_mapper.writeValueAsString(post_param);
        // build the request header
        CookieStore cookies = this.cookie_manager.getCookieStore();
        ArrayList<HttpCookie> cookie_list = new ArrayList(cookies.getCookies());
        String header_cookies;
        if (cookie_list.size() > 0)
        {
            // remove brackets for use in request header
            header_cookies = cookie_list.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining(";", "", ""));
        }
        else
        {
            // empty value for cookie header
            header_cookies = "";
        }
        // build the POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api_url))
                .header("Referer", API_URL)
                .header("cookie", header_cookies)
                .POST(HttpRequest.BodyPublishers.ofString(request_body))
                .build();
        // send the request and retrieve the response
        HttpResponse<String> response = this.request_client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse GetRequest(String api_url) throws IOException, InterruptedException
    {
        // Make an API call using GET method

        // build an API call
        HttpRequest api_request = HttpRequest.newBuilder()
                .uri(URI.create(api_url))
                .GET()
                .build();
        // send the GET request and retrieve the response
        HttpResponse<String> call_response = this.request_client.send(api_request,
                HttpResponse.BodyHandlers.ofString());
        return call_response;
    }

    public static String getApiUrl() {
        return API_URL;
    }
}
