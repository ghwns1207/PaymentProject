package com.ppvp.PaymentProject.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/product")
    public String productPage() {
        return "product";
    }


}
