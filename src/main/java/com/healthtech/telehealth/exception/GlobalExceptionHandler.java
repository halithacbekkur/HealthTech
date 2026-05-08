package com.healthtech.telehealth.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Kullanici bulunamadi hatasi (404)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Randevu bulunamadi hatasi (404)
    @ExceptionHandler(AppointmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Gecersiz durum gecisi hatasi (400)
    @ExceptionHandler(InvalidStatusTransitionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidStatusTransition(InvalidStatusTransitionException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Yetkisiz erisim hatasi (403)
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Gecersiz kimlik bilgileri hatasi (401)
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Email zaten kayitli hatasi (409)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Veritabani butunluk hatasi (409) — unique constraint ihlali
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildErrorResponse("Veri bütünlüğü hatası: Tekrarlanan kayıt", HttpStatus.CONFLICT);
    }

    // Validasyon hatalari (400) — form dogrulama
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Error");
        response.put("errors", fieldErrors);
        return response;
    }

    // Genel RuntimeException yakalayici (500)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Yardimci: Standart hata yaniti olusturur
    private Map<String, Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return response;
    }
}