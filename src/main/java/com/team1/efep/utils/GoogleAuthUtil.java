package com.team1.efep.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class GoogleAuthUtil {
    private static final String GOOGLE_OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";

    public static String generateGoogleLoginUrl(Map<String, String> params) throws UnsupportedEncodingException {
        String clientId = params.get("client_id");
        String redirectUri = params.get("redirect_uri");
        String scope = params.get("scope");
        String responseType = params.getOrDefault("response_type", "code");
        String state = params.getOrDefault("state", "state123");
        String prompt = params.getOrDefault("prompt", "consent");
        String accessType = params.getOrDefault("access_type", "online");

        StringBuilder urlBuilder = new StringBuilder(GOOGLE_OAUTH_URL);
        urlBuilder.append("?client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
        urlBuilder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
        urlBuilder.append("&response_type=").append(URLEncoder.encode(responseType, "UTF-8"));
        urlBuilder.append("&scope=").append(URLEncoder.encode(scope, "UTF-8"));
        urlBuilder.append("&state=").append(URLEncoder.encode(state, "UTF-8"));
        urlBuilder.append("&prompt=").append(URLEncoder.encode(prompt, "UTF-8"));
        urlBuilder.append("&access_type=").append(URLEncoder.encode(accessType, "UTF-8"));

        return urlBuilder.toString();
    }
}
