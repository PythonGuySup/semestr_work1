package ru.kpfu.itis.translation.model.repository;

import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TranslationRepository {

  private final JdbcTemplate jdbcTemplate;

  public void saveLog(String ip, String originalText, String translatedText) {
    String sql =
        "INSERT INTO TranslationLogs (ip_address, original_text, translated_text, created_at) "
            + "VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(
        sql, ip, originalText, translatedText, new Timestamp(System.currentTimeMillis()));
  }
}