package com.santi.priceservice.priceservice.driven.repositories.mappers;

import com.santi.priceservice.application.models.Price;
import com.santi.priceservice.priceservice.driven.repositories.models.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    Price toDomain(PriceEntity entity);
}