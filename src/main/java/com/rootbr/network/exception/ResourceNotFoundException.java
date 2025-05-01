package com.rootbr.network.exception;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, String fieldName, Object fieldValue) {
    super(String.format("%s not found with %s: '%s'", resourceType, fieldName, fieldValue));
  }
}
