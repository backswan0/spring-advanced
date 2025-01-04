package org.example.expert.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.expert.common.config.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

  @InjectMocks
  private PasswordEncoder passwordEncoder;

  @Test
  void matches_메서드가_정상적으로_동작한다() {
    // given
    String rawPassword = "testPassword";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    // when
    boolean matches = passwordEncoder.matches(
        rawPassword,
        encodedPassword
    );

    // then
    assertTrue(matches);
  }

  @Test
  void matches_메서드가_잘못된_비밀번호를_매칭하지_않는다() {
    // given
    String rawPassword = "testPassword";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    // when
    boolean mismatches = passwordEncoder.matches(
        "wrongPassword",
        encodedPassword
    );

    // then
    assertFalse(mismatches);
  }
}