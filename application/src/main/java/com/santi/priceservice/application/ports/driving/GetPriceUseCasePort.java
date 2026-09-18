package com.santi.priceservice.application.ports.driving;

import com.santi.priceservice.application.models.Price;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GetPriceUseCasePort {

    Price getPrice(LocalDateTime applicationDate, Long productId, Integer brandId);
}