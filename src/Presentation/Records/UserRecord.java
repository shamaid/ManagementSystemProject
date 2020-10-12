package Presentation.Records;

public class UserRecord implements Record {

    private String id;
    private String name;
    private String roles;

    public UserRecord(String userToString) {
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoles() {
        return roles;
    }
}
