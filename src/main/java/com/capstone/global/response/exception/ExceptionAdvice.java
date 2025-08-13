package com.capstone.global.response.exception;

import com.capstone.global.response.ApiResponse;
import com.capstone.global.response.ErrorInfoDto;
import com.capstone.global.response.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("요청 파라미터가 올바르지 않습니다.");

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(), message, null));
    }
    // @Valid 어노테이션을 통한 검증 실패 시 발생하는 예외를 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage())
                            .orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", "
                                    + newErrorMessage);
                });

        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY,
                ErrorStatus.valueOf("_BAD_REQUEST"), request, errors);
    }

    // 모든 Exception 클래스 타입의 예외 처리 (500번대)
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {

        String errorMessage = e.getMessage();
        String errorPoint = Objects.isNull(e.getStackTrace()) ? "No Stack Trace Error."
                : e.getStackTrace()[0].toString();
        return handleExceptionInternalFalse(e, ErrorStatus.INTERNAL_SERVER_ERROR,
                HttpHeaders.EMPTY, ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus(), request,
                e.getMessage());
    }

    // 사용자 정의 예외 처리 (400번대)
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException e, HttpServletRequest request) {
        ErrorInfoDto errorInfo = e.getErrorReasonHttpStatus();
        ApiResponse<Object> body = ApiResponse.onFailure(
                errorInfo.code(),
                errorInfo.message(),
                e.getDetail() // null이 아니면 추가 정보
        );
        return ResponseEntity.status(errorInfo.httpStatus()).body(body);
    }
    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorInfoDto info,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(info.code(), info.message(),
                null);
        WebRequest webRequest = new ServletWebRequest(request);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                info.httpStatus(),
                webRequest
        );
    }


    // 공통 예외 처리 메소드
    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e,
                                                                ErrorStatus errorCommonStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),
                errorCommonStatus.getMessage(), errorPoint);
        log.error(errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    // 서버 에러 처리 메소드
    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers,
                                                               ErrorStatus errorCommonStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),
                errorCommonStatus.getMessage(), errorArgs);
        log.error(errorArgs.toString());
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    // 검증 실패에 대한 처리 메소드
    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e,
                                                                     HttpHeaders headers, WebRequest request, String message) {
        ApiResponse<Object> body = ApiResponse.onFailure(ErrorStatus.BAD_REQUEST.getCode(),
                message, null);
        log.error(message);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                ErrorStatus.BAD_REQUEST.getHttpStatus(),
                request
        );
    }
}