package org.alin.chargerhertsmere;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerStatusService {

  private static final String ACCOUNT_SID = "ACd9da66e6d9a951a280ee9bd384595fff";

  @Value("${app.auth_token}")
  private String AUTH_TOKEN;
  @Value("${app.email_password}")
  private String EMAIL_TOKEN;

  private final RestTemplate restTemplate;
  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  @Getter private ResponseObject activeResponseObject = null;
  @Getter @Setter private boolean makeCall = false;

  public boolean changeStatus() {
    this.makeCall = !this.makeCall;
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    com.twilio.rest.api.v2010.account.Message message =
        com.twilio.rest.api.v2010.account.Message.creator(
                new com.twilio.type.PhoneNumber("+447490927845"),
                new com.twilio.type.PhoneNumber("+447588667442"),
                String.format("Phone is now %s.", this.makeCall? "on": "off"))
            .create();
    log.info("Changed Status: {}", message.getBody());
    return this.makeCall;
  }

  @Scheduled(cron = "0 0/1 * * * ?")
  @EventListener(ApplicationStartedEvent.class)
  public void checkStatus() throws JsonProcessingException, URISyntaxException {
    ResponseObject response =
        restTemplate.getForObject(
            "https://charge.pod-point.com/ajax/pods/1545", ResponseObject.class);
    if (!Objects.equals(response, activeResponseObject) && this.makeCall) {
      String to = "ineagu01@mail.bbk.ac.uk";
      String from = "neagu_ionutalin@icloud.com";
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
                  return new PasswordAuthentication(from, EMAIL_TOKEN);
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
        message.setSubject(
            String.format(
                "Charge A %s, Charger B %s",
                response.getPods().get(0).getStatuses().get(0).getLabel(),
                response.getPods().get(0).getStatuses().get(1).getLabel()));
        // Now set the actual message
        message.setText(OBJECT_MAPPER.writeValueAsString(response));
        log.info("sending...");
        // Send message
        Transport.send(message);
        log.info("Sent message successfully....");
      } catch (MessagingException mex) {
        mex.printStackTrace();
      }
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      Call call =
          Call.creator(
                  new PhoneNumber("+447490927845"),
                  new PhoneNumber("+447588667442"),
                  new URI("http://demo.twilio.com/docs/voice.xml"))
              .create();
      log.info("Call made: {}", call.getStatus());
    }
    activeResponseObject = response;
  }
}
