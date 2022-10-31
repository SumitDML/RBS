package com.dml.project.rbs.dto;

import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class CustomResponseEntity extends ResponseEntity implements Serializable {

    private static final long serialVersionUID = 7156526077883281625L;

    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }

    public CustomResponseEntity(Object body, HttpStatus status) {
        super(body, status);
    }

    public CustomResponseEntity(MultiValueMap headers, HttpStatus status) {
        super(headers, status);
    }

    public CustomResponseEntity(Object body, MultiValueMap headers, HttpStatus status) {
        super(body, (org.springframework.util.MultiValueMap<String, String>) headers, status);
    }
}