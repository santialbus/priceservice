package com.santi.priceservice.application.ports.driven;

import com.santi.priceservice.application.models.Price;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findPrices(LocalDateTime applicationDate, Long productId, Integer brandId);
}