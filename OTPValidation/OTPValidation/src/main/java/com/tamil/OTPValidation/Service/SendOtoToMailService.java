package com.tamil.OTPValidation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SendOtoToMailService
{
    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void sendOTPService(String email) {
        String otp = generateotp();
        try
        {
            sendOTPToMail(email, otp);
            otpStorage.put(email, otp);
            scheduler.schedule(() -> otpStorage.remove(email), 10, TimeUnit.MINUTES);

        } catch (MessagingException e)
        {
            throw new RuntimeException("unable to send OTP");
        }

       }


    private String sendOTPToMail(String email,String otp) throws MessagingException
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("OTP VALIDATION");
        mimeMessageHelper.setText("OTP Received for verification "+otp);
        javaMailSender.send(mimeMessage);
        return otp;
    }
    private String generateotp()
    {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

}

