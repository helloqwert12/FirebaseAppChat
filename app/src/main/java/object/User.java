package object;

public class User {
    private String name;
    private String userid;
    private static User instance = null;

    public  User(){

    }

    public static User getInstance(){
        if (instance == null)
            instance = new User();
        return instance;
    }

    public String getName(){
        return name;
    }

    public void setName(String value){
        name = value;
    }

    public String getUserid() { return userid; }

    public void setUserid(String value) { userid = value; }
}
