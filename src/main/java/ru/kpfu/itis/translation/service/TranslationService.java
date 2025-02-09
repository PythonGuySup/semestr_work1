package ru.kpfu.itis.translation.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kpfu.itis.translation.exception.YandexApiTranslationException;
import ru.kpfu.itis.translation.model.dto.TranslationRequest;
import ru.kpfu.itis.translation.model.dto.TranslationResponse;
import ru.kpfu.itis.translation.model.dto.YandexResponse;
import ru.kpfu.itis.translation.model.dto.enums.LanguageCode;
import ru.kpfu.itis.translation.model.repository.TranslationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {
  @Value("${yandex.api.key}")
  private String apiKey;

  @Value("${yandex.cloud.folder.id}")
  private String cloudFolderId;

  private final String yandexUrl = "https://translate.api.cloud.yandex.net/translate/v2/translate";
  private final TranslationRepository translationRepository;
  private final RestTemplate restTemplate;
  private final ExecutorService executorService;

  public TranslationResponse getTranslation(
      String remoteAddress, TranslationRequest translationRequest) {
    String[] words = translationRequest.textToTranslate().split("//s+");
    // List of result of async computations with return type of String
    List<Future<YandexResponse>> futures = new ArrayList<>();

    for (String word : words) {
      Callable<YandexResponse> task =
          () -> getTranslationForWord(word, translationRequest.targetedLanguage());
      futures.add(executorService.submit(task));
    }

    StringBuilder translatedText = new StringBuilder();
    for (Future<YandexResponse> yandexResponseFuture : futures) {
      try {
        translatedText.append(yandexResponseFuture.get().text()).append(" ");
      } catch (ExecutionException e) {
        log.error("Error during translation: cannot access result of async computation", e);
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        log.error("Error during translation: async computation has been interrupted", e);
        throw new RuntimeException(e);
      }
    }

    // Saving user's info into the database
    translationRepository.saveLog(
        remoteAddress, translationRequest.textToTranslate(), translatedText.toString());

    return new TranslationResponse(
        translatedText.toString().trim(), translationRequest.targetedLanguage());
  }

  private YandexResponse getTranslationForWord(String word, LanguageCode targetedLanguage) {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("targetLanguageCode", targetedLanguage.toString());
    requestBody.put("folderId", cloudFolderId);
    requestBody.put("texts", Collections.singletonList(word));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


    ResponseEntity<YandexResponse> response = restTemplate.postForEntity(
            yandexUrl,
            requestEntity,
            YandexResponse.class
    );

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      return response.getBody();
    } else {
      log.error(
          "Error occurred in Yandex API: {}, {}", response.getStatusCode(), response.getBody());
      throw new YandexApiTranslationException(
          "Error occurred in Yandex API: " + response.getStatusCode() + ", " + response.getBody());
    }
  }
}
