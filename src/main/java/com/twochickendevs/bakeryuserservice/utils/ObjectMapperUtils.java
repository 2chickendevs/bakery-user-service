package com.twochickendevs.bakeryuserservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectMapperUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return MAPPER;
    }
}
