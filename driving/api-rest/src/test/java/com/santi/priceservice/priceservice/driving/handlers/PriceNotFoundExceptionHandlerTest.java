package com.santi.priceservice.priceservice.driving.handlers;

import static org.assertj.core.api.Assertions.assertThat;

import com.santi.priceservice.application.exceptions.PriceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;

@ExtendWith(MockitoExtension.class)
class PriceNotFoundExceptionHandlerTest {

    @InjectMocks
  private PriceNotFoundExceptionHandler priceNotFoundExceptionHandler;

  @Test
  @DisplayName("Should return not found problem detail when price is not found")
  void shouldReturnNotFoundProblemDetailWhenPriceIsNotFound() {
    PriceNotFoundException exception = new PriceNotFoundException("No applicable price found");

    ProblemDetail result = priceNotFoundExceptionHandler.handlePriceNotFound(exception);

    assertThat(result.getStatus()).isEqualTo(404);
    assertThat(result.getDetail()).isEqualTo("No applicable price found");
    assertThat(result.getTitle()).isEqualTo("Not Found");
  }
}
