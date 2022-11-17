package com.sliit.ssd.client.config;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class HttpUnauthorizedException extends RuntimeException {

}