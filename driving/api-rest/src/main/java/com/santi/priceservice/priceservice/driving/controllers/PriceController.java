package com.santi.priceservice.priceservice.driving.controllers;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.application.ports.driving.GetPriceUseCasePort;
import com.santi.priceservice.priceservice.driving.mappers.PriceResponseMapper;
import com.santi.priceservice.priceservice.driving.models.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final GetPriceUseCasePort getPriceUseCase;
    private final PriceResponseMapper mapper;

    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
                                                 @RequestParam Long productId, @RequestParam Integer brandId) {
        log.info("Controller: PriceController received request for productId: {}, brandId: {}, applicationDate: {}",
                productId, brandId, applicationDate);
        Price price = getPriceUseCase.getPrice(applicationDate, productId, brandId);
        return ResponseEntity.ok(mapper.toResponse(price));
    }
}
