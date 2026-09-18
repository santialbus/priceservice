package com.santi.priceservice.priceservice.driven.repositories.adapters;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.priceservice.driven.PriceMoJpaRepository;
import com.santi.priceservice.priceservice.driven.repositories.mappers.PriceMapper;
import com.santi.priceservice.priceservice.driven.repositories.models.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    @Mock
    private PriceMoJpaRepository repository;

    @Mock
    private PriceMapper mapper;

    @InjectMocks
    private PriceRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return mapped prices from repository")
    void shouldReturnMappedPricesFromRepository() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceEntity entity = createPriceModel(PriceEntity.class, 2, 1, "25.45");
        Price price = createPriceModel(Price.class, 2, 1, "25.45");

        when(repository.findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                35455L, 1, applicationDate, applicationDate))
                .thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(price);

        List<Price> result = adapter.findPrices(applicationDate, 35455L, 1);

        assertThat(result).containsExactly(price);
        verify(repository).findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                35455L, 1, applicationDate, applicationDate);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return empty list when repository finds no prices")
    void shouldReturnEmptyListWhenRepositoryFindsNoPrices() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        when(repository.findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                35455L, 1, applicationDate, applicationDate))
                .thenReturn(List.of());

        List<Price> result = adapter.findPrices(applicationDate, 35455L, 1);

        assertThat(result).isEmpty();
        verify(repository).findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                35455L, 1, applicationDate, applicationDate);
    }

    private <T> T createPriceModel(Class<T> type, Integer priceList, Integer priority, String price) {
        if (PriceEntity.class.equals(type)) {
            return type.cast(PriceEntity.builder()
                    .priceList(priceList)
                    .priority(priority)
                    .price(new BigDecimal(price))
                    .build());
        }

        if (Price.class.equals(type)) {
            return type.cast(Price.builder()
                    .priceList(priceList)
                    .priority(priority)
                    .price(new BigDecimal(price))
                    .build());
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
