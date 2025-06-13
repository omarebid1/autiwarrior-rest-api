package com.autiwarrior.service;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Autowired
    private TransactionalEmailsApi brevoApi;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @PostConstruct
    public void init() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey(apiKey);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Password Reset Request for Autiwarrior";

        String body = "<html>" +
                "<head>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                "  .container { max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }" +
                "  .header { background: #0073e6; color: #ffffff; padding: 15px; text-align: center; font-size: 20px; font-weight: bold; border-radius: 8px 8px 0 0; }" +
                "  .content { padding: 20px; font-size: 16px; color: #333333; }" +
                "  .highlight { background: #ffe6e6; padding: 10px; border-left: 5px solid #ff0000; font-weight: bold; }" +
                "  .token { color: #ff0000; font-size: 22px; font-weight: bold; text-align: center; display: block; }" +
                "  .footer { text-align: center; padding: 15px; font-size: 14px; color: #777777; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>Password Reset Request</div>" +
                "    <div class='content'>" +
                "      <p><strong>Hello,</strong></p>" +
                "      <p>We received a request to reset the password for your account associated with <strong>" + to + "</strong>.</p>" +
                "      <div class='highlight'>" +
                "        <p>🔒 Security Advisory:</p>" +
                "        <ul>" +
                "          <li>This token is valid for <strong>15 minutes</strong> only.</li>" +
                "          <li>For your protection, we recommend:</li>" +
                "          <ul>" +
                "            <li>Changing your password immediately after reset.</li>" +
                "            <li>Enabling two-factor authentication.</li>" +
                "          </ul>" +
                "        </ul>" +
                "      </div>" +
                "      <p>📌 <strong>Action Required:</strong></p>" +
                "      <p>To complete the password reset, use this token:</p>" +
                "      <p class='token'>" + token + "</p>" +
                "      <p>❌ <strong>Unauthorized Request?</strong></p>" +
                "      <p>If you did not initiate this request, please contact our Security Team immediately:</p>" +
                "      <p>Email: <a href='mailto:autiwarrior@hotmail.com'>autiwarrior@hotmail.com</a></p>" +
                "    </div>" +
                "    <div class='footer'>Best regards, <br><strong>Corporate Security Team</strong> <br>Autiwarrior</div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(senderEmail);
        sender.setName(senderName);

        SendSmtpEmailTo recipient = new SendSmtpEmailTo();
        recipient.setEmail(to);

        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(sender);
        sendSmtpEmail.setTo(List.of(recipient));
        sendSmtpEmail.setSubject(subject);
        sendSmtpEmail.setHtmlContent(body); // Use HTML content for better styling

        try {
            brevoApi.sendTransacEmail(sendSmtpEmail);
            System.out.println("✅ Password reset email sent successfully to " + to);
        } catch (ApiException e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}