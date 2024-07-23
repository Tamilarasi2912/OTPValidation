package com.tamil.OTPValidation.Controller;

import com.tamil.OTPValidation.Service.SendOtoToMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @Autowired
    private SendOtoToMailService sendOtoToMailService;

    @PostMapping("/send-OTP")
    public String SendOTPToMail(@RequestParam("email") String email)
    {
        sendOtoToMailService.sendOTPService(email);
        return "OTP send Successfully";
    }

    @PostMapping("/validate")
    public String validateOtp(@RequestParam String email, @RequestParam String otp) {
        if (sendOtoToMailService.validateOtp(email, otp))
        {
            sendOtoToMailService.clearOtp(email);
            return "OTP is valid";
        } else
        {
            return "Invalid OTP";
        }
    }
}
