package com.hiendinh.ecommerce.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> maps
) {
}
