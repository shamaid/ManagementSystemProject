package Service;

import Domain.HasPage;
import Domain.UserFactory;
import Logger.Logger;
import Domain.Role;
import Domain.User;

public class PersonalPageSystem {

    public PersonalPageSystem() {
    }

    public boolean uploadToPage(String userId, String data){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("HasPage");
            if (role instanceof HasPage) {
                ((HasPage) role).uploadToPage(data);
                Logger.logEvent(user.getID() + " (User)", "Uploaded data to personal page");
                return true;
            }
        }
        Logger.logError(user.getID() + "No personal page");

        return false;
    }

    public String viewPage(String userId, String pageId) {
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("HasPage");
            if (role instanceof HasPage) {
                Logger.logEvent(user.getID() + " (User)", "Viewed page " + pageId);
                return ((HasPage)role).viewPage(pageId);
            }
        }
        Logger.logError("Viewing personal page failed");
        return null;
    }
}
