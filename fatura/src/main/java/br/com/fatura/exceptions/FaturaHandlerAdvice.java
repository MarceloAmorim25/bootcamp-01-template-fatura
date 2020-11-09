package br.com.fatura.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FaturaHandlerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadrao> handleResponseStatusException(ResponseStatusException responseStatusException) {

        Collection<String> mensagens = new ArrayList<>();

        mensagens.add(responseStatusException.getReason());
        ErroPadrao erroPadronizado = new ErroPadrao(mensagens);

        return ResponseEntity.status(responseStatusException.getStatus()).body(erroPadronizado);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> handle(MethodArgumentNotValidException methodArgumentNotValidException) {

        Collection<String> mensagens = new ArrayList<>();
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            String message = String.format("Campo %s %s", fieldError.getField(), fieldError.getDefaultMessage());
            mensagens.add(message);
        });

        ErroPadrao erroPadronizado = new ErroPadrao(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);

    }

}
