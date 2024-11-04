package com.security.backend.dtos.Response;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

        private String path;
        private String timestamp;
        private boolean success;
        private Integer statusCode;
        private String message;
        private T data;
        private Map<String, String> errors;

        public static String getCurrentTime() {
                return DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd")
                                .format(Instant.now().atZone(ZoneId.systemDefault()));
        }

        public static <T> ResponseEntity<ApiResponse<T>> success(
                        Integer statusCode,
                        T data) {
                return ResponseEntity.status(statusCode)
                                .body(new ApiResponse<>(getCurrentPath(), getCurrentTime(), true,
                                                statusCode, "No message sent.", data, null));

        }

        public static <T> ResponseEntity<ApiResponse<T>> success(
                        String message,
                        Integer statusCode,
                        T data) {
                return ResponseEntity.status(statusCode)
                                .body(new ApiResponse<>(getCurrentPath(), getCurrentTime(), true,
                                                statusCode, message, data, null));

        }

        public static <T> ResponseEntity<ApiResponse<T>> successWithHeaders(
                        String message,
                        Integer statusCode,
                        T data,
                        HttpHeaders headers) {

                return ResponseEntity.status(statusCode).headers(headers)
                                .body(new ApiResponse<>(getCurrentPath(), getCurrentTime(), true, statusCode, message,
                                                data, null));
        }

        public static <T> ResponseEntity<ApiResponse<T>> failure(
                        String message,
                        Integer statusCode,
                        Map<String, String> errors) {

                return ResponseEntity.status(statusCode)
                                .body(new ApiResponse<>(getCurrentPath(), getCurrentTime(), false, statusCode, message,
                                                null,
                                                errors));
        }

        private static String getCurrentPath() {
                try {
                        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                                        .getRequestAttributes();
                        if (attrs != null) {
                                return attrs.getRequest().getRequestURI();
                        }
                } catch (Exception ex) {
                        return "Could not get the request path.";
                }
                return "N/A";
        }
}
