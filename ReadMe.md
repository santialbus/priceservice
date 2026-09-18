# Price Service

## Descripción

Este proyecto implementa un servicio REST para la consulta del precio aplicable a un producto de una determinada cadena en una fecha concreta.

La solución parte del escenario planteado en el test técnico, en el que la tabla `PRICES` contiene las diferentes tarifas de un producto, junto con su periodo de aplicación y prioridad.

### Datos de ejemplo

| BRAND_ID | START_DATE          | END_DATE            | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
| -------: | ------------------- | ------------------- | ---------: | ---------: | -------: | ----: | ---- |
|        1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 |          1 |      35455 |        0 | 35.50 | EUR  |
|        1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 |          2 |      35455 |        1 | 25.45 | EUR  |
|        1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 |          3 |      35455 |        1 | 30.50 | EUR  |
|        1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 |          4 |      35455 |        1 | 38.95 | EUR  |

### Campos

* **BRAND_ID**: identificador de la cadena. En los datos de ejemplo, `1` corresponde a ZARA.
* **START_DATE / END_DATE**: intervalo de fechas durante el que la tarifa es aplicable.
* **PRICE_LIST**: identificador de la tarifa de precios.
* **PRODUCT_ID**: identificador del producto.
* **PRIORITY**: prioridad de aplicación de la tarifa. Cuando existen varias tarifas aplicables simultáneamente, se selecciona la de mayor prioridad.
* **PRICE**: precio final de venta.
* **CURR**: código ISO de la moneda.

## Objetivo

El servicio expone un endpoint REST que permite consultar el precio aplicable proporcionando:

* Fecha de aplicación.
* Identificador del producto.
* Identificador de la cadena.

La respuesta contiene:

* Identificador del producto.
* Identificador de la cadena.
* Tarifa aplicada.
* Fecha de inicio y fin de aplicación.
* Precio final.
* Moneda.

La aplicación utiliza **H2 como base de datos en memoria**, inicializada con los datos proporcionados en el enunciado.

## Validación

Se han implementado tests de integración para validar los cinco escenarios solicitados en el test técnico, además de un caso adicional para comprobar el comportamiento cuando no existe ninguna tarifa aplicable.

El proyecto también incorpora tests unitarios y **mutation testing con PIT** para validar la calidad y efectividad de la suite de pruebas.

## Valoración

La solución se ha desarrollado teniendo en cuenta los tres aspectos indicados en el enunciado:

* Diseño y construcción del servicio.
* Calidad y mantenibilidad del código.
* Correctitud y cobertura de las pruebas.


## Arquitectura

Esta hecho en arquitectura hexagonal, con el objetivo de separar la lógica de negocio de los detalles de infraestructura y permitir una mayor flexibilidad y facilidad de mantenimiento.

## Estructura del proyecto

```text
priceservice/
├── application/
│   └── models/
│   └── exceptions/
│   └── ports/
│   └── services/
├── driving/
│   └── api-rest/
│       └── controllers/
│       └── handlers/
│       └── mappers/
│       └── models/
├── driven/
│   └── repository-sql/
├── boot/
└── pom.xml
```

## Technologias


- **Java 21**
- **Spring Boot 4.0.8**
- **Spring Web MVC**
- **Spring Data JPA**
- **H2 Database**
- **Maven**
- **JUnit 5**
- **Mockito**
- **MapStruct**
- **Lombok**
- **PIT Mutation Testing**

## API

### Consulta de precio

**GET** `/prices`

#### Parámetros

| Parámetro | Tipo | Descripción |
|---|---|---|
| `applicationDate` | `LocalDateTime` | Fecha y hora para la que se solicita el precio |
| `productId` | `Long` | Identificador del producto |
| `brandId` | `Integer` | Identificador de la cadena |

### Ejemplo de petición

```http
Caso 1: 
Get: http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1

{
    "id": 1,
    "brandId": 1,
    "startDate": "2020-06-14T00:00:00",
    "endDate": "2020-12-31T23:59:59",
    "priceList": 1,
    "productId": 35455,
    "priority": 0,
    "price": 35.50,
    "currency": "EUR"
}

Caso 2:
Get: http://localhost:8080/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1
{
    "id": 2,
    "brandId": 1,
    "startDate": "2020-06-14T15:00:00",
    "endDate": "2020-06-14T18:30:00",
    "priceList": 2,
    "productId": 35455,
    "priority": 1,
    "price": 25.45,
    "currency": "EUR"
}

Caso 3:
Get: http://localhost:8080/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1

{
    "id": 1,
    "brandId": 1,
    "startDate": "2020-06-14T00:00:00",
    "endDate": "2020-12-31T23:59:59",
    "priceList": 1,
    "productId": 35455,
    "priority": 0,
    "price": 35.50,
    "currency": "EUR"
}

Caso 4: 
Get: http://localhost:8080/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1
{
    "id": 3,
    "brandId": 1,
    "startDate": "2020-06-15T00:00:00",
    "endDate": "2020-06-15T11:00:00",
    "priceList": 3,
    "productId": 35455,
    "priority": 1,
    "price": 30.50,
    "currency": "EUR"
}

Caso 5:
Get: http://localhost:8080/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1

{
    "id": 4,
    "brandId": 1,
    "startDate": "2020-06-15T16:00:00",
    "endDate": "2020-12-31T23:59:59",
    "priceList": 4,
    "productId": 35455,
    "priority": 1,
    "price": 38.95,
    "currency": "EUR"
}

Caso 6: 
Get/Error: http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=99999&brandId=1

{
    "detail": "No applicable price found",
    "instance": "/prices",
    "status": 404,
    "title": "Not Found"
}

```
