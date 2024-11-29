package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.CommandLine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegisterConfirmation(String to) {
        try {
            sendMail(to, "Confirmation of your registration", "Soldier put bayonet!", "Welcome on the great crusade against the xenos.");
        } catch (Exception ignored) {
        }
    }

    public void sendCartPayValidation(String to, Cart cart) {
        try {
            StringBuilder content = new StringBuilder("Your command contain:\n");
            for (CommandLine commandLine : cart.getCommandLines()) {
                content.append("* ").append(commandLine.getProduct().getName()).append(" x ").append(commandLine.getQuantity()).append("\n");
            }
            sendMail(to, "Confirmation of you cart", "Your have successfully paid your command!", content.toString());
        } catch (Exception ignored) {
        }
    }

    private void sendMail(String to, String subject, String title, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);

        String body = """
            <html>
            <body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9;'>
                <div style='max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 5px;'>
                    <h2 style='color: #333;'>%s</h2>
                    <p style='font-size: 14px; color: #999;'> %s </p>
                    <p style='font-size: 14px; color: #555;'>For the god emperor of humanity!</p>
                </div>
            </body>
            </html>
            """.formatted(title, content);
        helper.setText(body, true);

        mailSender.send(mimeMessage);
    }
}
