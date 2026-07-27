import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.Properties;

public class JavaMailReceiveDemo {
    public static void main(String[] args) throws Exception {
        String username = "your-email@gmail.com";
        String appPassword = "your-app-password";

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.getMessages();

        int count = Math.min(messages.length, 5);
        for (int i = messages.length - count; i < messages.length; i++) {
            System.out.println("Subject: " + messages[i].getSubject());
        }

        inbox.close(false);
        store.close();
    }
}
