package ru.kpfu.itis.translation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.translation.model.dto.TranslationRequest;
import ru.kpfu.itis.translation.model.dto.TranslationResponse;
import ru.kpfu.itis.translation.service.TranslationService;
import ru.kpfu.itis.translation.utils.SanitizationUtils;

@RestController
@RequestMapping("/translate")
@RequiredArgsConstructor
public class TranslationController {
  private final TranslationService translationService;

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public TranslationResponse translate(
      @RequestBody @Valid TranslationRequest translationRequest, HttpServletRequest request) {
    return translationService.getTranslation(
        SanitizationUtils.getSanitizedString(request.getRemoteAddr()),
        translationRequest.toSanitizedRequest());
  }
}