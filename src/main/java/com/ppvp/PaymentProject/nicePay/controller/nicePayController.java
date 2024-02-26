package com.ppvp.PaymentProject.nicePay.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ppvp.PaymentProject.nicePay.model.NicePayReadyAuthResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Controller
@RequestMapping("/niceApi")
public class nicePayController {

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${nicepay.client.key}")
  private String CLIENT_ID;
  @Value("${nicepay.secret.key}")
  private String SECRET_KEY;

  @GetMapping("/nicePay")
  public String indexDemo(Model model){
    UUID id = UUID.randomUUID();
    model.addAttribute("orderId", id);
    model.addAttribute("clientId", CLIENT_ID);
    return "/nicePay";
  }

  @GetMapping("/cancel")
  public String cancelDemo(){
    return "/cancel";
  }

  @PostMapping("/serverAuth")
  public String requestPayment(@RequestBody NicePayReadyAuthResult nicePayReadyAuthResult,
      Model model) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()));
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> AuthenticationMap = new HashMap<>();
    AuthenticationMap.put("amount", String.valueOf(nicePayReadyAuthResult.getAmount()));

    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap), headers);

    ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
        "https://sandbox-api.nicepay.co.kr/v1/payments/" + nicePayReadyAuthResult.getTid(), request, JsonNode.class);

    JsonNode responseNode = responseEntity.getBody();
    String resultCode = responseNode.get("resultCode").asText();
    model.addAttribute("resultMsg", responseNode.get("resultMsg").asText());

    System.out.println(responseNode.toPrettyString());

    if (resultCode.equalsIgnoreCase("0000")) {
      // 결제 성공 비즈니스 로직 구현
      log.info("responseNode : {}", responseNode);
    } else {
      // 결제 실패 비즈니스 로직 구현
      log.error("responseNode : {}", responseNode);
    }

    return "/response";
  }

  @PostMapping("/cancelAuth")
  public String requestCancel(
      @RequestParam(name = "tid") String tid,
      @RequestParam(name = "amount") String amount,
      Model model) throws Exception {

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()));
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> AuthenticationMap = new HashMap<>();
    AuthenticationMap.put("amount", amount);
    AuthenticationMap.put("reason", "test");
    AuthenticationMap.put("orderId", UUID.randomUUID().toString());

    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap), headers);

    ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
        "https://sandbox-api.nicepay.co.kr/v1/payments/"+ tid +"/cancel", request, JsonNode.class);

    JsonNode responseNode = responseEntity.getBody();
    String resultCode = responseNode.get("resultCode").asText();
    model.addAttribute("resultMsg", responseNode.get("resultMsg").asText());

    System.out.println(responseNode.toPrettyString());

    if (resultCode.equalsIgnoreCase("0000")) {
      // 취소 성공 비즈니스 로직 구현
    } else {
      // 취소 실패 비즈니스 로직 구현
    }

    return "/response";
  }

  @PostMapping("/hook")
  public ResponseEntity<String> hook(@RequestBody HashMap<String, Object> hookMap) throws Exception {
    String resultCode = hookMap.get("resultCode").toString();

    System.out.println(hookMap);

    if(resultCode.equalsIgnoreCase("0000")){
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

}
