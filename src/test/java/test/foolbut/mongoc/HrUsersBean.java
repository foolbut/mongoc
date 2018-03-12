package test.foolbut.mongoc;

import self.foolbut.mongoc.annotation.Collection;

@Collection("hr_staff_user")
public class HrUsersBean {

    private String loginName;
    private String name;
    private String email;
    private Boolean administrator; //����Ա

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }
}

