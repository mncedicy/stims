package com.mncedicy.stims.api.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mncedicy.stims.api.Classes.IKhokha.PayRequest;
import com.mncedicy.stims.api.Classes.IKhokha.PayResponse;
import com.mncedicy.stims.api.Classes.Response;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class IKhokhaPayService {

    @Value("${ikhokha.application.url}")
    private String BASE_URL;
    @Value("${ikhokha.application.id}")
    private String APP_ID;
    @Value("${ikhokha.application.key}")
    private String APP_KEY;


    public IKhokhaPayService() {


    }

    public Response payment(PayRequest payRequest) {
        Response response = new Response();
        response.setStatus("Error");
        String endpoint = BASE_URL+"/payment";
        Gson gson=new Gson();
        System.out.println(endpoint);
        try {
            String requestedUrl = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
            URI uri = new URI(requestedUrl);
            String requestedUrlBase = uri.resolve("/").toString();

            payRequest.entityID = APP_ID;
            payRequest.paymentReference = payRequest.externalTransactionID+"";
            if(!requestedUrlBase.contains("localhost")) {
                payRequest.requesterUrl = requestedUrlBase;
                payRequest.urls.callbackUrl = requestedUrlBase + "configuration/payment/webhook/callback";
                payRequest.urls.successPageUrl = requestedUrlBase + "payment/success";
                payRequest.urls.failurePageUrl = requestedUrlBase + "payment/failure";
                payRequest.urls.cancelUrl = requestedUrlBase + "payment/cancel";
            }

            String requestBody = gson.toJson(payRequest);
            System.out.println(requestBody);

            String payloadToSign = CreatePayloadToSign(endpoint, requestBody);
            System.out.println(payloadToSign);
            String signature = calculateHmacSha256(payloadToSign, APP_KEY);
            System.out.println(signature);

            HttpClient client = HttpClient.newHttpClient();


            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("IK-APPID", APP_ID)
                    .header("IK-SIGN", signature)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                System.out.println("Success");
                response.setStatus("Success");
                response.setMessage("Payment link created successfully");
                PayResponse payResponse = gson.fromJson(httpResponse.body(),PayResponse.class);
                response.setData(payResponse);
            } else {
                System.out.println("Error httpResponse");
                response.setMessage(httpResponse.body());
                response.setMessage(httpResponse.statusCode()+"");
            }
            System.out.println(httpResponse.body());

        }catch (URISyntaxException e) {
            System.out.println("Error URISyntaxException");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Exception");
            System.out.println(e.getMessage());
        }

        return response;

    }



    public Response getPaymentStatus(String paylinkId) {
        Response response = new Response();
        response.setStatus("Error");
        String endpoint = BASE_URL+"/getStatus/"+paylinkId;
        Gson gson=new Gson();
        System.out.println(endpoint);
        try {

            String path = getPath(endpoint);
            String signature = calculateHmacSha256(path, APP_KEY);
            System.out.println(signature);

            HttpClient client = HttpClient.newHttpClient();


            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("IK-APPID", APP_ID)
                    .header("IK-SIGN", signature)
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                System.out.println("Success");
                response.setStatus("Success");
                response.setMessage("Status fetched successfully");
                PayResponse payResponse = gson.fromJson(httpResponse.body(),PayResponse.class);
                response.setData(payResponse);
            } else {
                System.out.println("Error httpResponse");
                response.setMessage(httpResponse.body());
                response.setMessage(httpResponse.statusCode()+"");
            }
            System.out.println(httpResponse.body());

        }catch (URISyntaxException e) {
            System.out.println("Error URISyntaxException");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Exception");
            System.out.println(e.getMessage());
        }

        return response;

    }


    public Response getPaymentHistory(String startDate,String endDate) {
        Response response = new Response();
        response.setStatus("Error");
        String endpoint = BASE_URL+"/payments/history?startDate="+startDate+"&endDate="+endDate;
        Gson gson=new Gson();
        System.out.println(endpoint);
        try {

            String path = getPath(endpoint);
            String signature = calculateHmacSha256(path, APP_KEY);
            System.out.println(signature);

            HttpClient client = HttpClient.newHttpClient();


            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("IK-APPID", APP_ID)
                    .header("IK-SIGN", signature)
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                System.out.println("Success");
                response.setStatus("Success");
                response.setMessage("Status fetched successfully");
                Type listType = new TypeToken<List<PayResponse>>() {}.getType();
                List<PayResponse> payResponse = gson.fromJson(httpResponse.body(),listType);
                response.setData(payResponse);
            } else {
                System.out.println("Error httpResponse");
                response.setMessage(httpResponse.body());
                response.setMessage(httpResponse.statusCode()+"");
            }
            System.out.println(httpResponse.body());

        }catch (URISyntaxException e) {
            System.out.println("Error URISyntaxException");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Exception");
            System.out.println(e.getMessage());
        }

        return response;

    }





    static String getPath(String urlString) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String path = url.getPath();
        System.out.println(path);
        return path;
    }

    static String CreatePayloadToSign(String urlString, String body) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String path = url.getPath();
            // Directly use the body string that was already JSON serialized.
            String fullPayload = path + body;
        System.out.println(path);
            return JsStringEscape(fullPayload);
    }
    static String JsStringEscape(String str)
    {
        // Correctly escaping backslashes, quotes, and other necessary characters.
        str = str.replace("\\", "\\\\"); // Escape backslashes first to prevent double escaping
        str = str.replace("\"", "\\\""); // Escape double quotes
        str = str.replaceAll( "\u0000", "\\0"); // Null character if necessary
        return str;
    }
    public static String calculateHmacSha256(String data, String key){

        // Specify the HMAC-SHA256 algorithm
        String algorithm = "HmacSHA256";

        try {
            // Convert the secret key string to a SecretKeySpec object
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);

            // Get an instance of the Mac class with the specified algorithm
            Mac mac = Mac.getInstance(algorithm);

            // Initialize the Mac instance with the secret key
            mac.init(secretKeySpec);

            // Compute the HMAC signature for the data
            byte[] hmacSha256Bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Encode the resulting bytes to Base64 for easier representation
            //  return Base64.getEncoder().encodeToString(hmacSha256Bytes);

            // Convert the byte array to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hmacSha256Bytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();


        }catch (NoSuchAlgorithmException e){
            return e.getMessage();
        }
        catch (InvalidKeyException e){
            return e.getMessage();
        }

    }


}