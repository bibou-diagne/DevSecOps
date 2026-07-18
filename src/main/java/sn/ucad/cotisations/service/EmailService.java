package sn.ucad.cotisations.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

/**
 * Envoi de notifications email (Jakarta Mail / Angus).
 *
 * Configuration via le fichier classpath {@code email.properties}.
 * Désactivé par défaut : si {@code mail.enabled=false} ou si le SMTP n'est pas
 * renseigné, les appels sont sans effet (l'application n'est jamais bloquée).
 * L'envoi est effectué dans un thread séparé (non bloquant) et toute erreur
 * d'envoi est seulement journalisée.
 */
public class EmailService {

    private static boolean enabled;
    private static String host;
    private static String port;
    private static String user;
    private static String password;
    private static String from;

    static {
        Properties cfg = new Properties();
        try (InputStream in = EmailService.class.getClassLoader()
                .getResourceAsStream("email.properties")) {
            if (in != null) {
                cfg.load(in);
                enabled  = Boolean.parseBoolean(cfg.getProperty("mail.enabled", "false"));
                host     = trimToNull(cfg.getProperty("mail.smtp.host"));
                port     = cfg.getProperty("mail.smtp.port", "587");
                user     = trimToNull(cfg.getProperty("mail.smtp.user"));
                password = cfg.getProperty("mail.smtp.password", "");
                from     = cfg.getProperty("mail.from", user);
            }
        } catch (Exception e) {
            enabled = false;
            System.err.println("[EmailService] Configuration introuvable : " + e.getMessage());
        }
    }

    /** Envoie un email HTML de façon asynchrone et non bloquante. No-op si non configuré. */
    public void envoyer(String destinataire, String sujet, String corpsHtml) {
        if (!enabled || host == null || user == null
                || destinataire == null || destinataire.isBlank()) {
            return;
        }
        Thread t = new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(destinataire));
                message.setSubject(sujet, "UTF-8");
                message.setContent(corpsHtml, "text/html; charset=UTF-8");

                Transport.send(message);
            } catch (Exception e) {
                System.err.println("[EmailService] Échec d'envoi à "
                        + destinataire + " : " + e.getMessage());
            }
        }, "email-sender");
        t.setDaemon(true);
        t.start();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}
