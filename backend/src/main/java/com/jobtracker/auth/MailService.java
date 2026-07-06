package com.jobtracker.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final ObjectProvider<JavaMailSender> mailSender;
    private final String smtpHost;

    public MailService(ObjectProvider<JavaMailSender> mailSender, @Value("${spring.mail.host:}") String smtpHost) {
        this.mailSender = mailSender;
        this.smtpHost = smtpHost;
    }

    public void sendPasswordReset(String to, String resetLink) {
        if (!StringUtils.hasText(smtpHost)) {
            log.info("Password reset link for {}: {}", to, resetLink);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset your Job Tracker password");
        message.setText("Use this link to reset your Job Tracker password: " + resetLink);
        mailSender.getObject().send(message);
    }
}
