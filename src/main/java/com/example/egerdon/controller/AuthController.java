package com.example.egerdon.controller;

import com.example.egerdon.service.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 인증 관련 컨트롤러
 */
@Controller
public class AuthController {

    /**
     * 홈페이지
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 사용자 대시보드
     */
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails != null) {
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("nickname", principalDetails.getNickname());
        }
        return "dashboard";
    }
}