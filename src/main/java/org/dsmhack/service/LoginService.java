package org.dsmhack.service;

import org.dsmhack.model.LoginToken;
import org.dsmhack.model.User;
import org.dsmhack.repository.EmailSender;
import org.dsmhack.repository.LoginTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class LoginService {
  @Autowired
  private CodeGenerator codeGenerator;

  @Autowired
  private EmailSender emailSender;

  @Autowired
  private LoginTokenRepository loginTokenRepository;

  public void login(User user) {
    String token = codeGenerator.generateLoginToken();
    LoginToken loginToken = new LoginToken();
    loginToken.setToken(token);
    loginToken.setUserGuid(user.getUserGuid());
    loginToken.setTokenExpDate(twentyMinutesFromNow());
    loginTokenRepository.save(loginToken);
    emailSender.sendTo(user.getEmail(), token);
  }

  private LocalDateTime twentyMinutesFromNow() {
    LocalDateTime tokenExpiration = LocalDateTime.now().plus(20, ChronoUnit.MINUTES);
    return tokenExpiration;
  }
}