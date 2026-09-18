package com.santi.priceservice.priceservice.driving.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.priceservice.driving.models.PriceResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceResponseMapperTest {

    private final PriceResponseMapper mapper = new PriceResponseMapperImpl();

    @Test
    @DisplayName("Should map price domain to price response")
    void shouldMapPriceDomainToPriceResponse() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);

        Price price = Price.builder()
                .id(1L)
                .brandId(1)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        PriceResponse result = mapper.toResponse(price);

        assertThat(result.getProductId()).isEqualTo(35455L);
        assertThat(result.getBrandId()).isEqualTo(1);
        assertThat(result.getPriceList()).isEqualTo(2);
        assertThat(result.getStartDate()).isEqualTo(startDate);
        assertThat(result.getEndDate()).isEqualTo(endDate);
        assertThat(result.getPrice()).isEqualByComparingTo("25.45");
        assertThat(result.getCurrency()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("Should return null when price is null")
    void shouldReturnNullWhenPriceIsNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }
}
