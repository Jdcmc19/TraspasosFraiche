package Classes;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.Workbook;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


public class Correo {

    public static void enviarCorreo(String correo, String pass, String destino, Workbook file,String trasp){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        // Session session = Session.getDefaultInstance(props, null);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(correo, pass);
                    }
                });


        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(correo));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            msg.setSubject("TRASPASO DE "+ trasp);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Buenas.\nPor favor realizar estos traspasos entre las tiendas indicadas en el archivo.\nGracias!\n\n\nEste correo fue generado automaticamente, por lo tanto no responda al mismo. Cualquier consulta al correo: alemaco@racsa.co.cr");

            MimeBodyPart attachmentBodyPart= new MimeBodyPart();


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            file.write(bos);

            DataSource source = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName("TRASPASO DE "+trasp+".xlsx"); // ex : "test.pdf"

            multipart.addBodyPart(textBodyPart);  // add the text part
            multipart.addBodyPart(attachmentBodyPart); // add the attachement part

            msg.setContent(multipart);


            Transport.send(msg);
            bos.close();
        } catch (MessagingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ocurrió un error");
            alert.setContentText(e.toString());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    alert.close();
                }
            });
            LOGGER.log(Level.SEVERE,"Error mientras se enviaba el email",e);
        } catch (IOException ioe){
            ioe.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ocurrió un error");
            alert.setContentText(ioe.toString());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    alert.close();
                }
            });
        }
    }
}
