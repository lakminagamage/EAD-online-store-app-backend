package com.ead.order_service.helper;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class RequestHelper {

    public static int SendGetRequest(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (var response = httpClient.execute(httpGet)) {
                return response.getCode();
            }
        }
    }

    public static int SendPostRequest(String url, String body) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(body));
            try (var response = httpClient.execute(httpPost)) {
                return response.getCode();
            }
        }
    }

    public static int SendPutRequest(String url, String body) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setEntity(new StringEntity(body));
            try (var response = httpClient.execute(httpPut)) {
                return response.getCode();
            }
        }
    }

    public static int SendDeleteRequest(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(url);
            try (var response = httpClient.execute(httpDelete)) {
                return response.getCode();
            }
        }
    }

    public static int SendPatchRequest(String url, String body) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setEntity(new StringEntity(body));
            try (var response = httpClient.execute(httpPatch)) {
                return response.getCode();
            }
        }
    }
}
