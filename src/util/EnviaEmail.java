/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.JOptionPane;
import modelo.Email;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;


/**
 *
 * @author Daniel
 */
public class EnviaEmail {

    public boolean enviar(Email e) throws EmailException {

        try {
            MultiPartEmail email = new MultiPartEmail(); //Classe responsável por enviar o email
            int porta = getPorta(e.getDestinatario() + "");
            String portaSmtp = Integer.toString(porta); //converte a porta para String

            email.setSmtpPort(porta); //porta para envio
            email.setAuthenticator(new DefaultAuthenticator(e.getRemetente(), e.getSenha())); //autenticação da conta
            email.setDebug(true);
            String hostName = getHost(e.getDestinatario() + "");
            email.setHostName(hostName); //varia de cada servidor
            //propriedades para o envio  do email
            email.getMailSession().getProperties().put("mail.smtps.auth", true);
            email.getMailSession().getProperties().put("mail.debug", "true");
            email.getMailSession().getProperties().put("mail.smtps.port", portaSmtp);
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.port", portaSmtp);
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable", true);
            email.getMailSession().getProperties().put("mail.smtp.ssl.trust", hostName);
            System.out.println("host name " + hostName);
            System.out.println("Remetente " + e.getRemetente());
            System.out.println("Destinatario" + e.getDestinatario());
            email.setFrom(e.getRemetente(), e.getRemetente()); //email e nome de quem está enviando o email
            email.setSubject(e.getAssunto()); //Assunto do email
            email.setMsg(e.getMensagem()); //Mensagem do email
            email.addTo(e.getDestinatario()); //destinatário do email
            email.setTLS(true); //define o método de criptografia
            System.out.println(e.getCaminhoAnexo());
             if (!e.getCaminhoAnexo().equals("")) {
                System.out.println("passou aqui ..");
                EmailAttachment anexo1 = new EmailAttachment(); //Classe para anexar arquivos
                anexo1.setPath(e.getCaminhoAnexo()); //Incluindo diretório do anexo
                anexo1.setDisposition(EmailAttachment.ATTACHMENT); //Informando um email que tem anexo
                email.attach(anexo1); //Atribuindo os anexos ao email
                email.send();//envia o email                
            } else {
                email.send();//envia o email
            }
           JOptionPane.showMessageDialog(null, "Email enviado com sucesso");
            System.out.println("Email Enviado com Sucesso !!!");
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("Erro + " + ex.getMessage());
            return false;
        }

    }

    private static String getHost(String servidor) {
        String host = "";
        if (servidor.contains("gmail.com")) {
            host = "smtp.gmail.com";
        } else if (servidor.contains("bol.com")) {
            host = "smtps.bol.com.br";
        } else if (servidor.contains("bol.com")) {
            host = "smtp.ibest.com.br";
        } else if (servidor.contains("ig")) {
            host = "smtp.ig.com.br";
        } else if (servidor.contains("hotmail") || servidor.contains("otlook")) {
            host = "smtp.live.com";
        }
        return host;
    }

    private static int getPorta(String servidor) {
        int porta;
        if (servidor.contains("otlook") || servidor.contains("hotmail")) {
            porta = 25;
        } else {
            porta = 587;
        }
        return porta;
    }
}
