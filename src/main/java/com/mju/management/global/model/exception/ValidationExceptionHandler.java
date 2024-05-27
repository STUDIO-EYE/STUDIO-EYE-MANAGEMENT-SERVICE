package com.mju.management.global.model.exception;

import com.mju.management.global.model.result.CommonResult;
import com.mju.management.global.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.mju.management.global.model.exception.ExceptionList.INVALID_INPUT_VALUE;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationExceptionHandler {

	private final ResponseService responseService;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public CommonResult handleBindException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder builder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append(" [");
			builder.append(fieldError.getField());
			builder.append("] -> ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(" 입력된 값: [");
			builder.append(fieldError.getRejectedValue());
			builder.append("] ");
		}
		return responseService.getFailResult(INVALID_INPUT_VALUE.getCode(), builder.toString());
	}

}
