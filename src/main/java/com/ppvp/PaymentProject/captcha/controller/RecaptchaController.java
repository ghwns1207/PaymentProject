package com.ppvp.PaymentProject.captcha.controller;


import com.ppvp.PaymentProject.captcha.model.FormData;
import com.ppvp.PaymentProject.captcha.model.RecaptchaResponse;

import com.ppvp.PaymentProject.captcha.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class RecaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/VerifyRecaptcha")
    public ResponseEntity<RecaptchaResponse> processForm(@RequestBody FormData formData) {
        // Verify the reCAPTCHA token
        RecaptchaResponse isValid = captchaService.verifyRecaptcha(formData.getToken());

//        if (isValid) {
//            return ResponseEntity.ok(new RecaptchaResponse(true));
//        } else {
//            return ResponseEntity.ok(new RecaptchaResponse(false));
//        }
        return ResponseEntity.ok(isValid);
    }

//    private RecaptchaResponse verifyRecaptcha(String recaptchaToken) {
//        // Send the token to Google for verification
//        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add("secret", recaptchaSecretKey);
//        parameters.add("response", recaptchaToken);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        RecaptchaResponse recaptchaResponse = restTemplate.postForObject(verifyUrl, request, RecaptchaResponse.class);
//        log.info("parameters :{}", parameters);
//
//        log.info("recaptchaResponse : {}", recaptchaResponse);
//        log.info("errorCodes : {}", recaptchaResponse.getErrorCodes());
//
//        if(recaptchaResponse != null && recaptchaResponse.isSuccess()&& recaptchaResponse.getScore() >= RECAPTCHA_THRESHOLD){
//            return recaptchaResponse;
//        }else {
//            return recaptchaResponse;
//        }
//
//    }
}
