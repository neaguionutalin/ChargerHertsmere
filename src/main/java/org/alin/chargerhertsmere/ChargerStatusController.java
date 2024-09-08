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

  @PostMapping(value = "/sms", produces = MediaType.APPLICATION_XML_VALUE)
  public MessagingResponse smsWebhook(@RequestBody String body) {
    boolean phone = chargerStatusService.changeStatus();
    return new MessagingResponse.Builder().message(new Message.Builder().body(new Body.Builder(String.format("Phone is now %s.", phone ? "on": "off")).build()).build()).build();
  }
}
