package com.santi.priceservice.priceservice.driven.repositories.mappers;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.priceservice.driven.repositories.models.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PriceMapperTest {

    private final PriceMapper mapper = new PriceMapperImpl();

    @Test
    @DisplayName("Should map price entity to price domain")
    void shouldMapPriceEntityToPriceDomain() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);

        PriceEntity entity = PriceEntity.builder()
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

        Price result = mapper.toDomain(entity);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBrandId()).isEqualTo(1);
        assertThat(result.getStartDate()).isEqualTo(startDate);
        assertThat(result.getEndDate()).isEqualTo(endDate);
        assertThat(result.getPriceList()).isEqualTo(2);
        assertThat(result.getProductId()).isEqualTo(35455L);
        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getPrice()).isEqualByComparingTo("25.45");
        assertThat(result.getCurrency()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("Should return null when entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }
}