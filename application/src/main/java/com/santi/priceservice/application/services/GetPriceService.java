package com.santi.priceservice.application.services;

import com.santi.priceservice.application.exceptions.PriceNotFoundException;
import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.application.ports.driven.PriceRepositoryPort;
import com.santi.priceservice.application.ports.driving.GetPriceUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class GetPriceService implements GetPriceUseCasePort {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price getPrice(LocalDateTime applicationDate, Long productId, Integer brandId) {
        return priceRepositoryPort.findPrices(applicationDate, productId, brandId)
                .stream().max(Comparator.comparing(Price::getPriority))
                .orElseThrow(() -> new PriceNotFoundException("No applicable price found"));
    }
}