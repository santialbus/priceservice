package com.santi.priceservice.priceservice.driving.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.application.ports.driving.GetPriceUseCasePort;
import com.santi.priceservice.priceservice.driving.mappers.PriceResponseMapper;
import com.santi.priceservice.priceservice.driving.models.PriceResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

    @Mock
    private GetPriceUseCasePort getPriceUseCase;

    @Mock
    private PriceResponseMapper mapper;

    @InjectMocks
    private PriceController controller;

    @Test
    @DisplayName("Should return price response")
    void shouldReturnPriceResponse() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Price price = createPrice();
        PriceResponse response = createResponse();

        when(getPriceUseCase.getPrice(applicationDate, 35455L, 1)).thenReturn(price);
        when(mapper.toResponse(price)).thenReturn(response);

        ResponseEntity<PriceResponse> result = controller.getPrice(applicationDate, 35455L, 1);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response);
        verify(getPriceUseCase).getPrice(applicationDate, 35455L, 1);
        verify(mapper).toResponse(price);
    }

    private Price createPrice() {
        return Price.builder()
                .id(1L)
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();
    }

    private PriceResponse createResponse() {
        return PriceResponse.builder()
                .productId(35455L)
                .brandId(1)
                .priceList(2)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();
    }
}
