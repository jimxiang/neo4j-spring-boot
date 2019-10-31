package com.service.guarantee.utils;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("restriction")
public class HttpClientService {
    private static Logger logger = Logger.getLogger(HttpClientService.class);
    private static final int retryCount = 3;

    private String AUTH = "Authorization";
    private String BASIC = null;

    private HttpClientBuilder httpClientBuilder;
    private CloseableHttpClient closeableHttpClient;

    public HttpClientService() {
        if (closeableHttpClient == null) {
            createCloseableHttpClientWithBasicAuth();
        }
    }

    public String doGetMethod(String host, int port, String URI) throws ParseException, IOException {
        String result = "";
        HttpResponse httpResponse = null;
        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet("http://" + host + ":"
                + String.valueOf(port) + URI);
        httpGet.setHeader(AUTH, BASIC);
        httpResponse = closeableHttpClient.execute(httpGet);
        entity = httpResponse.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
        }

        return result;
    }

    public String doPostMethod(String host, int port, String URI, String name,
                               String value) throws ParseException, IOException {
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://" + host + ":"
                + String.valueOf(port) + URI);
        String resStr = "";
        httpPost.setHeader(AUTH, BASIC);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(name, value));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters, Charset
                .forName("UTF-8")));
        response = closeableHttpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            resStr = EntityUtils.toString(entity, "UTF-8");
        }

        return resStr;
    }

    public String doPostMethod(String host, int port, String URI, String value) throws ParseException, IOException {
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://" + host + ":"
                + String.valueOf(port) + URI);
        String resStr = "";
        httpPost.setHeader(AUTH, BASIC);
        httpPost.setEntity(new ByteArrayEntity(value.getBytes("UTF-8")));
        response = closeableHttpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            resStr = EntityUtils.toString(entity, "UTF-8");
        }

        return resStr;
    }

    public String doPostMethod(String requestUrl, String value) throws ParseException, IOException {
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://" + requestUrl);
        String resStr = "";
        httpPost.setHeader(AUTH, BASIC);
        httpPost.setEntity(new ByteArrayEntity(value.getBytes("UTF-8")));
        response = closeableHttpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            resStr = EntityUtils.toString(entity, "UTF-8");
        }

        return resStr;
    }

    public void closeHttpClient() {
        if (closeableHttpClient != null) {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createCloseableHttpClient() {
        if (closeableHttpClient == null) {
            httpClientBuilder = HttpClientBuilder.create();
            closeableHttpClient = httpClientBuilder.build();
        }
    }

    public void createCloseableHttpClientWithBasicAuth() {
        httpClientBuilder = HttpClientBuilder.create();
        CredentialsProvider provider = new BasicCredentialsProvider();
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
                AuthScope.ANY_REALM);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                "root", "superuser");
        provider.setCredentials(scope, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(provider);
        // closeableHttpClient = httpClientBuilder.build();
        // 添加重试机制
        HttpRequestRetryHandlerImpl httpRequestRetryHandler = new HttpRequestRetryHandlerImpl();
        closeableHttpClient = httpClientBuilder.setRetryHandler(
                httpRequestRetryHandler).build();
    }

    public void createAuthHeader(String userName, String password) {
        BASIC = "BASIC " + base64Encode(userName + ":" + password);
    }

    private String base64Encode(String inputString) {
        byte[] bytes = inputString.getBytes();
        return new BASE64Encoder().encode(bytes);
    }

    class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {

            if (executionCount <= retryCount) {
                logger.error(String.format(
                        "Have Retried Count: %d, Total Retry Count %d.",
                        executionCount, retryCount));
                return true;
            } else if (executionCount > retryCount) {
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                return false;
            }
            if (exception instanceof ConnectException) {
                return true;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                return true;
            }

            return false;
        }
    }
}
