package Domain;

import Data.Database;

public class HasPage extends Role{

    private PersonalPage page;

    public HasPage(PersonalPage page) {
        this.page = page;
        this.myRole = "HasPage";
    }

    public void uploadToPage(String data)
    {
        page.addData(data);
        Database.updateObject(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HasPage;
    }

    public String viewPage(String pageId) {
        PersonalPage personalPage = Database.getPersonalPage(pageId);
        if (personalPage!=null)
            return personalPage.toString();
        return null;
    }

    public PersonalPage getPage() {
        return page;
    }
}
