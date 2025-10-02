package com.mncedicy.stims.api.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mncedicy.stims.api.Classes.Response;
import com.mncedicy.stims.api.Classes.Yoco.LineItem;
import com.mncedicy.stims.api.Classes.Yoco.PayRequest;
import com.mncedicy.stims.api.Model.infringement_notice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
public class YocoPayService {

    @Value("${yoco.application.url}")
    private String base_url;
    @Value("${yoco.public.key}")
    private String public_key;
    @Value("${yoco.secret.key}")
    private String secret_key;


    public YocoPayService() {


    }

    public Response createCheckout(List<infringement_notice> notices) {
        Response response = new Response();
        response.setStatus("Error");
        String endpoint = base_url+"/api/checkouts";
        Gson gson=new Gson();
        try {

            String requestedUrl = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
            URI uri = new URI(requestedUrl);
            String requestedUrlBase = uri.resolve("/").toString();

            PayRequest payRequest = new PayRequest();
            payRequest.amount = (int)(notices.stream()
                    .mapToDouble(infringement_notice::getInfringement_notice_final_amount)
                    .sum()*100);
            payRequest.successUrl = requestedUrlBase + "payment/success";
            payRequest.failureUrl = requestedUrlBase + "payment/failure";
            payRequest.cancelUrl = requestedUrlBase + "payment/cancel";

            payRequest.metadata.uuid = notices.get(0).infringement_notice_uuid;
            payRequest.metadata.clientId = notices.get(0).infringement_notice_client_id;
            payRequest.metadata.cellphoneNumber = notices.get(0).infringement_notice_cellphone;
            payRequest.metadata.name = notices.get(0).infringement_notice_name;
            payRequest.metadata.description = "Infringement Notice Payment";
            payRequest.metadata.items = notices.size();
            payRequest.metadata.noticeId = notices.get(0).infringement_notice_id;
            payRequest.metadata.noticeReference = notices.get(0).infringement_notice_reference;
            payRequest.metadata.referenceType = "Notice";

            for(infringement_notice notice : notices){
                LineItem item = new LineItem();
                item.displayName = notice.infringement_notice_reference;
                item.uuid = notice.infringement_notice_uuid;
                item.quantity = 1;
                item.description = notice.infringement_notice_registration;
                item.pricingDetails.price = (int)(notice.infringement_notice_final_amount*100);
                payRequest.lineItems.add(item);
            }

            String requestBody = gson.toJson(payRequest);
            System.out.println(requestBody);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("Authorization", "Bearer " + secret_key)
                  //  .header("Idempotency-Key", payRequest.metadata.uuid+"_"+payRequest.amount)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                System.out.println("Success");
                response.setStatus("Success");
                response.setMessage("Payment link created successfully");
                payRequest = gson.fromJson(httpResponse.body(),PayRequest.class);
                response.setData(payRequest);
            } else {
                System.out.println("Error httpResponse");
                response.setMessage(httpResponse.body());
            }
            System.out.println(httpResponse.body());

        }catch (URISyntaxException e) {
            System.out.println("Error URISyntaxException");
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Exception");
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
        }

        return response;

    }


}