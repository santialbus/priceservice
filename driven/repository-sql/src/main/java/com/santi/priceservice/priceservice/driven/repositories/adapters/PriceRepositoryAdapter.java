package com.santi.priceservice.priceservice.driven.repositories.adapters;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.application.ports.driven.PriceRepositoryPort;
import com.santi.priceservice.priceservice.driven.PriceMoJpaRepository;
import com.santi.priceservice.priceservice.driven.repositories.mappers.PriceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceMoJpaRepository repository;
    private final PriceMapper mapper;

    @Override
    public List<Price> findPrices(LocalDateTime applicationDate, Long productId, Integer brandId) {
        log.info("Repository: PriceRepositoryAdapter retrieving prices for productId: {}, brandId: {}, applicationDate: {}",
                productId, brandId, applicationDate);
        return repository
                .findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(productId, brandId,
                        applicationDate,
                        applicationDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}