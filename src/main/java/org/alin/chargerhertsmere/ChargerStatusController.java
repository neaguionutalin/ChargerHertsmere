package org.alin.chargerhertsmere;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChargerStatusController {

  private final ChargerStatusService chargerStatusService;

  @PutMapping(path = "/api/call")
  public void handleCall() {
    boolean status = chargerStatusService.isMakeCall();
    if (status) chargerStatusService.setMakeCall(false);
    else chargerStatusService.setMakeCall(true);
  }

  @PutMapping(path = "/api/email")
  public void handleEmail() {
    boolean status = chargerStatusService.isSendEmail();
    if (status) chargerStatusService.setSendEmail(false);
    else chargerStatusService.setSendEmail(true);
  }

  @GetMapping(path = "/api")
  public SwiftObject getCall() {
    return SwiftObject.builder()
        .email(chargerStatusService.isSendEmail())
        .phone(chargerStatusService.isMakeCall())
        .build();
  }
}
