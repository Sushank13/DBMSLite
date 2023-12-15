package TwoFactorAuthentication;

import Helper.HelperClass;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * This class provides the actual implementation of the ITwoFactorAuthentication interface.
 */
public class TwoFactorAuthentication implements ITwoFactorAuthentication
{
    private Map<String,String> userLoginDetails;
    private Map<String,String> userSecurityAnswers;
    HelperClass helperClass;

    /**
     * The constructor initializes the two Maps: userLoginDetails and userSecurityAnswers
     * with values of the userid and their corresponding passwords and security
     * answers,respectively.
     * It also initializes the Helper class.
     */
    public TwoFactorAuthentication()
    {
        userLoginDetails=new HashMap<>();
        userSecurityAnswers=new HashMap<>();
        userLoginDetails=loadUserLoginDetails();
        userSecurityAnswers=loadUserSecurityAnswers();
        helperClass=new HelperClass();
    }

    /**
     * This method reads the user login details from a file and stores it in a map
     * @return a map that has user ids and their corresponding password
     */
    @Override
    public Map<String,String> loadUserLoginDetails()
    {
        String userLoginDetailsFilePath="userLoginDetails.txt";
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(userLoginDetailsFilePath)))
        {
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                String userLoginDetail[]=line.split("\\.");
                    for (String eachUserLoginDetail : userLoginDetail)
                    {
                        String splitUserLoginDetail[] = eachUserLoginDetail.split("\\|");
                        if(splitUserLoginDetail.length>=2)
                        {
                            String userIdPart[] = splitUserLoginDetail[0].split(":");
                            String passwordPart[] = splitUserLoginDetail[1].split(":");
                            String userId = userIdPart[1];
                            String passWord = passwordPart[1];
                            userLoginDetails.put(userId, passWord);
                        }
                }

            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());

        }
        return userLoginDetails;
    }
    /**
     * This method reads the user security answer details from a file and stores it in a map
     * @return a map that has user security answer details
     */
    @Override
    public Map<String,String> loadUserSecurityAnswers()
    {
        String userSecurityAnswersFilePath="userSecurityAnswers.txt";
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(userSecurityAnswersFilePath)))
        {
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                String userSecurityAnswer[]=line.split("\\.");
                for(String eachUserSecurityAnswer:userSecurityAnswer )
                {
                    String splitUserSecurityAnswer[]=eachUserSecurityAnswer.split("\\|");
                    if(splitUserSecurityAnswer.length>=2)
                    {
                        String userIdPart[] = splitUserSecurityAnswer[0].split(":");
                        String answerPart[] = splitUserSecurityAnswer[1].split(":");
                        String userId = userIdPart[1];
                        String passWord = answerPart[1];
                        userSecurityAnswers.put(userId, passWord);
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return userSecurityAnswers;
    }

    /**
     * This method checks if the user id provided exists or not.
     * @param userID is the user id entered by the user
     * @return true if the user id is present else return false
     */
    @Override
    public boolean userExists(String userID)
    {
        if(userLoginDetails==null)
        {
            return false;
        }
        for(String userId:userLoginDetails.keySet())
        {
            if(userId.equals(userID))
            {
                return true;
            }
        }
        return false;
    }
    /**
     *
     * @param userID is the user id entered by the user
     * @param passWord is the password entered by the user
     * @return true if the two-factor authentication is successful, else return false
     */
    public boolean loginSuccessful(String userID, String passWord)
    {
        if(userExists(userID)==false)
        {
            System.out.println("This user does not exist, please provide the correct User ID");
            return false;
        }
        String userPassword=userLoginDetails.get(userID);
        String enteredPassword=helperClass.passwordHashing(passWord);
        boolean passwordVerified=userPassword.equals(enteredPassword);
        if(passwordVerified)
        {
            System.out.println("Answer to the Security Question:");
            System.out.println("What is your mother's maiden name?");
            Scanner scanner=new Scanner(System.in);
            String answer=scanner.nextLine();
            boolean answerResult=isAnswerVerified(userID,answer);
            if (answerResult)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * This method checks if the security answer provided by the user matches the stored answer
     * @param userID is the user id entered by the user
     * @param answer is the answer entered by the user to the security question
     * @return true if the answer matches else return false
     */
    private boolean isAnswerVerified(String userID,String answer)
    {
        String storedAnswer=userSecurityAnswers.get(userID);
        if(answer.equals(storedAnswer))
        {
            return true;
        }
        return false;
    }

}
