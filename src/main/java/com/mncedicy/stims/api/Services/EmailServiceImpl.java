package com.mncedicy.stims.api.Services;

import com.twilio.Twilio;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@SuppressWarnings("ALL")
@Component
public class EmailServiceImpl {
    private final JavaMailSender emailSender;



    public EmailServiceImpl(){
        emailSender = getJavaMailSender();
    }




    public String sendSimpleSMS(String phoneNumber, String message) {


        String apiKey = "752f7316-c658-4a77-acd0-d68c733f67f0";
        String apiSecret = "436b29a8-fafb-45fe-9fd2-328e11df264f";
        String accountApiCredentials = apiKey + ":" + apiSecret;

        Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] credentialBytes = accountApiCredentials.getBytes(StandardCharsets.UTF_8);
        String base64Credentials = base64Encoder.encodeToString(credentialBytes);
       // String base64Credentials = "bW5jZWRpY3k6MDAwMzgxNTBNbmNl";

        HttpClient client = HttpClient.newHttpClient();
        String requestBody = "{ \"messages\" : [ { \"content\" : \""+message+"\", \"destination\" : \""+phoneNumber+"\" } ] }";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://rest.smsportal.com/BulkMessages"))
                    .header("Authorization", String.format("Basic %s", base64Credentials))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Success:");
                System.out.println(response.body());

            } else {
                System.out.println("Failure:");
                System.out.println(response.body());
            }
            System.out.println(base64Credentials);
            System.out.println(requestBody);
            return response.body();
        } catch (URISyntaxException e) {
            return e.getMessage();
        }catch (Exception e) {
            System.out.println("Something went wrong during the network request.");
            return e.getMessage();
        }



    }

    public String checkBalanceSMS() {


        String apiKey = "mncedicy";
        String apiSecret = "00038150Mnce";
        String accountApiCredentials = apiKey + ":" + apiSecret;

        Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] credentialBytes = accountApiCredentials.getBytes(StandardCharsets.UTF_8);
        String base64Credentials = base64Encoder.encodeToString(credentialBytes);
        // String base64Credentials = "bW5jZWRpY3k6MDAwMzgxNTBNbmNl";

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://rest.smsportal.com/Balance"))
                    .header("Authorization", String.format("Basic %s", base64Credentials))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Success:");
                System.out.println(response.body());

            } else {
                System.out.println("Failure:");
                System.out.println(response.body());
            }
            System.out.printf("Basic %s%n", base64Credentials);
            System.out.println(response.body());
            return response.body();
        } catch (URISyntaxException e) {
            return e.getMessage();
        }catch (Exception e) {
            System.out.println("Something went wrong during the network request.");
            return e.getMessage();
        }



    }


    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mncedicy@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }


    public void sendMessageWithInputStreamAttachment(
            String[] to, String subject, String text, String[] attachmentName) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("mncedicy@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            for(String fileStr: attachmentName)
            {
                FileSystemResource file = new FileSystemResource(new File(fileStr));
                helper.addAttachment(file.getFilename(), file);
            }

            emailSender.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("mncedicy@gmail.com");
        mailSender.setPassword("tquuorpctunbcgrq");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
