package pro.squadup.services;

import pro.squadup.models.PasswordReset;
import pro.squadup.repositories.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

// Mail service to send password reset tokens to user if they forgot password
@Service("mailService")
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // local variable for email that email service sends from
    @Value("${spring.mail.from}")
    private String from;


    // Prepares and sends password recovery email
    public void passwordReset(PasswordReset passwordReset) throws MessagingException {
        String processedHTMLTemplate = this.constructHTMLTemplate(passwordReset.getToken());

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, "UTF-8");

        msg.setFrom(from);
        msg.setTo(passwordReset.getEmail());
        msg.setSubject("Password Reset Request");
        msg.setText(processedHTMLTemplate, true);

        try {
            this.emailSender.send(mimeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    // Constructs the HTML template and returns as String
    private String constructHTMLTemplate(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        return templateEngine.process("emails/reset", context);
    }

}