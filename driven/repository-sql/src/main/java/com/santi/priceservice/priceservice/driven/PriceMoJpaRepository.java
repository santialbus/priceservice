package com.santi.priceservice.priceservice.driven;

import com.santi.priceservice.priceservice.driven.repositories.models.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceMoJpaRepository extends JpaRepository<PriceEntity, Long> {

    List<PriceEntity> findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long productId,
            Integer brandId,
            LocalDateTime applicationDate,
            LocalDateTime applicationDateEnd
    );
}