package org.alin.chargerhertsmere;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.*;

import java.util.Objects;
import java.util.Properties;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerStatusService {

  private final RestTemplate restTemplate;
  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private ResponseObject activeResponseObject = null;

  @Scheduled(cron = "0 0/1 * * * ?")
  @EventListener(ApplicationStartedEvent.class)
  public void checkStatus() throws JsonProcessingException {
    ResponseObject response =
        restTemplate.getForObject(
            "https://charge.pod-point.com/ajax/pods/1545", ResponseObject.class);
    log.info("Object: {}", OBJECT_MAPPER.writeValueAsString(response));
    if (!Objects.equals(response, activeResponseObject)) {
      activeResponseObject = response;
      String to = "ineagu01@mail.bbk.ac.uk";
      String from = "neagu_ionutalin@icloud.com";
      String password = "nomr-vulh-jlcj-mxsp";
      String host = "smtp.mail.me.com";
      Properties properties = System.getProperties();

      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.starttls.required", "true");
      properties.put("mail.smtp.auth", "true");

      Session session =
          Session.getInstance(
              properties,
              new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(from, password);
                }
              });
      try {
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));
        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // Set Subject: header field
        message.setSubject(String.format("Charge A %s, Charger B %s", response.getPods().get(0).getStatuses().get(0).getLabel(), response.getPods().get(0).getStatuses().get(1).getLabel()));
        // Now set the actual message
        message.setText(OBJECT_MAPPER.writeValueAsString(response));
        System.out.println("sending...");
        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
        mex.printStackTrace();
      }
    }
  }
}
