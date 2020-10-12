package Service;

import Domain.*;

import java.util.List;

public class NotificationSystem {

    public NotificationSystem() {
    }

    public static boolean notifyUser(User user, String message) {

        if(!(Server.sendNotification(user.getID(),message)))
            return false;

        return true;
    }

    public static List<String> getAllNotification(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            user.getMessageBox();
        }
        return null;
    }
}
