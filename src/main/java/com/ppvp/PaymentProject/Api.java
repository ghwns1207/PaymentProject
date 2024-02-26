package com.ppvp.PaymentProject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Api<T> {

  private String resultCode;
  private String resultMessage;

  private T data;

  private Error error;
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Error{
    private List<String> errorMessage;

  }

}