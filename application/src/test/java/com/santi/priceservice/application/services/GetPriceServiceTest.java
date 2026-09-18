package com.santi.priceservice.application.services;


import com.santi.priceservice.application.exceptions.PriceNotFoundException;
import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.application.ports.driven.PriceRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private GetPriceService getPriceService;


    @Test
    @DisplayName("Should return price with highest priority")
    void shouldReturnPriceWithHighestPriority() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price standardPrice = createPrice(1, 0, "35.50");
        Price priorityPrice = createPrice(2, 1, "25.45");

        when(priceRepositoryPort.findPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of(standardPrice, priorityPrice));

        Price result = getPriceService.getPrice(applicationDate, 35455L, 1);

        assertThat(result.getPriceList()).isEqualTo(2);
        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getPrice()).isEqualByComparingTo("25.45");

        verify(priceRepositoryPort).findPrices(applicationDate, 35455L, 1);
    }

    @Test
    @DisplayName("Should return the only applicable price")
    void shouldReturnOnlyApplicablePrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        Price price = createPrice(1, 0, "35.50");

        when(priceRepositoryPort.findPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of(price));

        Price result = getPriceService.getPrice(applicationDate, 35455L, 1);

        assertThat(result).isSameAs(price);
    }

    @Test
    @DisplayName("Should throw exception when no applicable price exists")
    void shouldThrowExceptionWhenNoApplicablePriceExists() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(priceRepositoryPort.findPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of());

        assertThatThrownBy(() -> getPriceService.getPrice(applicationDate, 35455L, 1))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessage("No applicable price found");
    }

    private Price createPrice(Integer priceList, Integer priority, String price) {
        return Price.builder()
                .priceList(priceList)
                .priority(priority)
                .price(new BigDecimal(price))
                .build();
    }

    @Test
    @DisplayName("Should select the price with the highest priority")
    void shouldSelectPriceWithHighestPriority() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price lowPriorityPrice = createPrice(1, 0, "35.50");
        Price highPriorityPrice = createPrice(2, 1, "25.45");

        when(priceRepositoryPort.findPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of(lowPriorityPrice, highPriorityPrice));

        Price result = getPriceService.getPrice(applicationDate, 35455L, 1);

        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getPriceList()).isEqualTo(2);
        assertThat(result.getPrice()).isEqualByComparingTo("25.45");
    }

}
