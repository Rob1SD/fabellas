package groupe_9.com.fabellas.bo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by spyro on 15/01/2018.
 */

@IgnoreExtraProperties
public class User {
    public String UID;
    public String phoneNumber;
    public Integer notation;

    public User(){

    }

    public User(String UID, String phoneNumber, Integer notation){
        this.UID = UID;
        this.phoneNumber = phoneNumber;
        this.notation = notation;
    }
}