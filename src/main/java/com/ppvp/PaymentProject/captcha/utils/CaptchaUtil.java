//package com.ppvp.PaymentProject.captcha.utils;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class CaptchaUtil {
//
//
//  public void captcaImg(HttpServletRequest request, HttpServletResponse response) {
//    Captcha captcha = new Captcha.Builder(200, 60)
//        .addText(new NumbersAnswerProducer(6))
//        .addNoise().addNoise().addNoise()
//        .addBackground(new GradiatedBackgroundProducer())
//        .addBorder()
//        .build();
//
////        response.setHeader("Cache-Control", "no-cache");
////        response.setDateHeader("Expires", 0);
////        response.setHeader("Pragma", "no-cache");
////        response.setDateHeader("Max-Age", 0);
//
//    request.getSession().setAttribute(Captcha.NAME, captcha);
//    CaptchaServletUtil.writeImage(response, captcha.getImage());
//
//  }
//
//
//
//}
