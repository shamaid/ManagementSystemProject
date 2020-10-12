package Domain;

public class MailSender {

    private static boolean reallySend = false;

    public static boolean send(String to, String messageToSend){
        if(reallySend){
            String mailSenderFile = "./MailSender/MailSender.exe";

            try
            {
                String subject = "New message from Team 5 Football Management System" ;
                new ProcessBuilder(mailSenderFile,to,subject,messageToSend).start();
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    public static void setReallySend(boolean reallySend) {
        MailSender.reallySend = reallySend;
    }

}
