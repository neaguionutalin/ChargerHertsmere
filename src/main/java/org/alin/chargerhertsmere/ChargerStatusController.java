package org.alin.chargerhertsmere;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChargerStatusController {

  private final ChargerStatusService chargerStatusService;

  @PutMapping(path = "/api/call")
  public void handleCall() {
    boolean status = chargerStatusService.isMakeCall();
    chargerStatusService.setMakeCall(!status);
  }

  @PutMapping(path = "/api/email")
  public void handleEmail() {
    boolean status = chargerStatusService.isSendEmail();
    chargerStatusService.setSendEmail(!status);
  }

  @GetMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
  public SwiftObject getCall() {
    return SwiftObject.builder()
        .email(chargerStatusService.isSendEmail())
        .phone(chargerStatusService.isMakeCall())
        .build();
  }

  @PostMapping(value = "/sms", produces = MediaType.APPLICATION_XML_VALUE)
  public MessagingResponse smsWebhook(@RequestBody String body) {
    log.info("Body: {}", body);
    return new MessagingResponse.Builder().message(new Message.Builder().body(new Body.Builder("I am replying").build()).build()).build();
  }
}
