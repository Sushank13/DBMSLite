package SignUp;

import Helper.HelperClass;
import Logger.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class implements the ISignUp interface to store the user login and security answer
 * when user signs up for the first time.
 */
public class SignUp implements ISignUp
{
    HelperClass helperClass;
    ILogger iLogger;

    /**
     * This constructor of SignUp initializes the Helper class and the Logger class
     */
    public SignUp()
    {
        helperClass=new HelperClass();
        iLogger=new Logger();
    }

    /**
     * This method stores the user id and a hashed password to a file
     * @param userId is the user id provided by the user
     * @param userPassword is the password provided by the user
     * @return true if the user login details are saved
     */
    @Override
    public boolean saveUserLoginDetails(String userId,String userPassword)
    {
        String hashedUserPassword=helperClass.passwordHashing(userPassword);
        String userLoginDetails="UserId:"+userId+"|"+"userPassword:"+hashedUserPassword+".";
        String userLoginDetailsFilePath="userLoginDetails.txt";
        String info;
        try(FileWriter fileWriter=new FileWriter(userLoginDetailsFilePath,true))
        {
          fileWriter.write(userLoginDetails+"\n");
          info= LocalDateTime.now()+":User "+userId+" login details stored\n";
          iLogger.log(info);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            info=LocalDateTime.now()+":User "+userId+" could not be created\n";
            iLogger.log(info);
            return false;
        }
        return true;
    }

    /**
     * This method stores the user's security answer into a file
     * @param userId is the user id provided by the user
     * @param securityAnswer is the answer provided by the user to the question asked
     * @return true if the user's security answer is saved
     */
    @Override
    public boolean saveUserSecurityAnswer(String userId,String securityAnswer)
    {
        String userSecurityAnswer="UserId:"+userId+"|"+"SecurityAnswer:"+securityAnswer+".";
        String userSecurityAnswerFilePath="userSecurityAnswers.txt";
        String info;
        try(FileWriter fileWriter=new FileWriter(userSecurityAnswerFilePath,true))
        {
            fileWriter.write(userSecurityAnswer+"\n");
            info=LocalDateTime.now()+":User "+userId+"'s security answered stored\n";
            iLogger.log(info);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            info=LocalDateTime.now()+":User "+userId+"'s security answered could not be stored\n";
            iLogger.log(info);
            return false;
        }
        return true;
    }

}
