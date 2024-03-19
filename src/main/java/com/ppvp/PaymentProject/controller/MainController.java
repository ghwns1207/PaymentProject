package com.ppvp.PaymentProject.controller;


import com.ppvp.PaymentProject.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping(path = {"/index", "","/"})
    public String mainPage( Model model ,HttpServletRequest request) {
        model.addAttribute("mainMessage", "안녕하세요");
        // 세션에서 저장된 사용자 이름 가져오기
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        if(userName != null && !userName.isEmpty() ){
            // 가져온 사용자 이름을 모델에 추가
            model.addAttribute("userName", userName);
        }
        return "index";
    }

//    @GetMapping("/redIndex")
//    public String redIndex(HttpServletRequest request, HttpServletResponse httpresponse){
//        // 세션에서 JWT 토큰 가져오는 로직
//        HttpSession session = request.getSession();
//        String jwtToken = (String) session.getAttribute("jwtToken");
//        if(jwtToken != null && !jwtToken.isEmpty()){
//            log.info("jwtToken : {}",jwtToken);
//            // 응답 헤더에 JWT 토큰 추가
//            httpresponse.setHeader("Authorization", "Bearer " + jwtToken);
//        }else {
//            log.info("헤더없음");
//        }
//        return "index";
//    }

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

//    @GetMapping("/nicePay")
//    public String nicePay() {return "nicePay";}

    @GetMapping("/cartView")
    public String cartView(){return "cartView";}

    @GetMapping("/ordersheet/pay")
    public String orderPay() { return "orderPay";}

    @GetMapping("/admin/products")
    public String productsPage() { return "productsPage"; }

}
