package com.santi.priceservice.priceservice.driving.mappers;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.priceservice.driving.models.PriceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceResponseMapper {

    PriceResponse toResponse(Price price);
}