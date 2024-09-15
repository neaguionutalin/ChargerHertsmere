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

  @PutMapping(path = "/api/call", produces = MediaType.APPLICATION_JSON_VALUE)
  public SwiftObject handleCall() {
    boolean status = chargerStatusService.isMakeCall();
    chargerStatusService.setMakeCall(!status);
    return SwiftObject.builder().phone(!status).build();
  }

  @GetMapping(path = "/api/call", produces = MediaType.APPLICATION_JSON_VALUE)
  public SwiftObject getObject() {
    return SwiftObject.builder().phone(chargerStatusService.isMakeCall()).build();
  }

  @PostMapping(value = "/sms", produces = MediaType.APPLICATION_XML_VALUE)
  public MessagingResponse smsWebhook() {
    boolean phone = chargerStatusService.changeStatus();
    return new MessagingResponse.Builder().message(new Message.Builder().body(new Body.Builder(String.format("Phone is now %s.", phone ? "on": "off")).build()).build()).build();
  }
}
