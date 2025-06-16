package com.rootbr.network.adapter.in.rest.server;

public enum HttpMethod {
  DELETE,
  GET,
  HEAD,
  OPTIONS,
  PATCH,
  POST,
  PUT,
  TRACE,
  ;

  public static HttpMethod of(final String value) {
    if (value.charAt(0) =='G') {
      return GET;
    } else if (value.startsWith("PO")) {
      return POST;
    } else if (value.startsWith("PU")) {
      return PUT;
    } else if (value.charAt(0) =='D') {
      return DELETE;
    } else if (value.charAt(0) =='H') {
      return HEAD;
    } else if (value.charAt(0) =='O') {
      return OPTIONS;
    } else if (value.startsWith("PA")) {
      return PATCH;
    } else if (value.charAt(0) =='T') {
      return TRACE;
    }
    throw new IllegalArgumentException("Unknown HTTP method: " + value);
  }
}
