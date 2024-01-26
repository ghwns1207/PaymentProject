package com.ppvp.PaymentProject.captcha.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RecaptchaResponse {

    private boolean success;
    private double score;
    private String action;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    private String hostname;
    @JsonProperty("error-codes")
    private String[] errorCodes;

}
