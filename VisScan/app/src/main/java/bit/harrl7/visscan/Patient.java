package bit.harrl7.visscan;

import java.util.Date;

/**
 * Created by aberhjk1 on 5/06/2017.
 */



// This class holds a static variable for the Users Identification Number/String
public class Patient
{
    public static String getUserID()
    {
        return userID;
    }

    public static void setUserID(String userID)
    {
        Patient.userID = userID;
    }

    public static String getDOB()
    {
        return dob;
    }

    public static void setDate(String bday)
    {
        Patient.dob = bday;
    }

    public static String userID;

    public static String dob;
}
