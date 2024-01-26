package com.ppvp.PaymentProject.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping(path = {"/", ""})
    public String mainPage(Model model) {
        model.addAttribute("mainMessage", "안녕하세요");
        return "hello";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/reCaptchaTest")
    public String reCaptchaTest() {return "reCaptchaTest";}

    @GetMapping("/product")
    public String productPage() {
        return "product";
    }

    @GetMapping("/kakaoPay")
    public String kakaopayPage(){return "kakaoPay";}

    @GetMapping("/kakaoMap")
    public String kakaoMapPage(){return "kakaoMap";}

    @GetMapping("/kakaofail")
    public String kakaofailPage(){return "kakaofail";}

    @GetMapping("/kakaocancel")
    public String kakaocancelPage(){return "kakaocancel";}

    @GetMapping("/kakaosuccess")
    public String kakaosuccessPage(){return "kakaosuccess";}

    @GetMapping("/googleReCaptcha")
    public String googleReCaptcha() {return "googleReCaptcha";}

    @GetMapping("/reCaptcha")
    public String reCaptcha() {return "reCaptcha";}

    @GetMapping("/maison")
    public String maison() {return "maison";}


}
