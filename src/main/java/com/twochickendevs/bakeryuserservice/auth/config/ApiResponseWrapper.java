package com.twochickendevs.bakeryuserservice.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twochickendevs.bakeryuserservice.auth.model.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // if already wrapped, leave it alone
        if (body instanceof ApiResponse) {
            return body;
        }

        // determine real status code from servlet response (default to 200)
        int statusCode = HttpStatus.OK.value();
        if (response instanceof ServletServerHttpResponse) {
            int sc = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
            if (sc > 0) statusCode = sc;
        }

        ApiResponse<Object> api = new ApiResponse<>(statusCode, body);

        // If the message converter selected is a String converter (or content-type is plain text),
        // we must return a String; otherwise StringHttpMessageConverter will try to cast and fail.
        boolean stringConverter =
                StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType) ||
                        MediaType.TEXT_PLAIN.includes(selectedContentType);

        if (stringConverter) {
            try {
                // serialize wrapper to JSON string so StringHttpMessageConverter can write it
                return mapper.writeValueAsString(api);
            } catch (Exception ex) {
                throw new HttpMessageNotWritableException("Failed to write ApiResponse as String", ex);
            }
        }

        // normal case: return object for JSON converters (MappingJackson2HttpMessageConverter etc)
        return api;
    }
}