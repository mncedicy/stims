package com.mncedicy.stims.api.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String fromWhatsAppNumber;

    public TwilioService() {
        // Initialize Twilio with account credentials
       // Twilio.init("AC1d70acfa8bfd8cbd7487749f7947600d", "33f05dadba1ee6defdb851244a0d88e8");
        Twilio.init("SK5f0ab7d0ce694faade4a0f8051efcdd0", "5LREB9k0gl4RO10bymTPdYELojheFy8U");
       // Twilio.init("AC15bc555790807ac97ff6a1bf3ebc9f73", "952e3d743a7935f5091d6ed7f77a38ae");
    }

    public String sendWhatsAppMessage(String to, String messageBody) {
        // Send a message via Twilio's API
        if(to.charAt(0)=='0')
            to = to.substring(1);
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:+27"+to),   // Recipient's WhatsApp number
                        new PhoneNumber("whatsapp:+14155238886"), // Twilio WhatsApp number
                        messageBody)                         // Message body
                        .create();

        return message.getSid(); // Return message SID to track status
    }
}