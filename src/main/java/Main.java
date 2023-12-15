import DBMSLite.IQuery;
import DBMSLite.Query;
import Logger.ILogger;
import Logger.Logger;
import SignUp.ISignUp;
import SignUp.SignUp;
import TwoFactorAuthentication.ITwoFactorAuthentication;
import TwoFactorAuthentication.TwoFactorAuthentication;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        ILogger iLogger=new Logger();
        ITwoFactorAuthentication iTwoFactorAuthentication=new TwoFactorAuthentication();
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter user id: ");
        String userID=scanner.nextLine();
        System.out.println("Enter user password: ");
        String userPassword=scanner.nextLine();
        boolean userExists=iTwoFactorAuthentication.userExists(userID);
        boolean loginStatus=false;
        if(userExists==false) /**User does not exist */
        {
            System.out.println("This user id does not exist. Please first sign up.");
            ISignUp iSignUp=new SignUp();
            System.out.println("Please enter the user id again.");
            userID=scanner.nextLine();
            System.out.println("Please enter the password again.");
            userPassword=scanner.nextLine();
            boolean signUpStatus=iSignUp.saveUserLoginDetails(userID,userPassword);
            if(signUpStatus==true)
            {
                String info= LocalDateTime.now()+":User "+userID+" login information saved successfully.\n";
                iLogger.log(info);
                System.out.println("Please answer the following security question:");
                System.out.println("What is your mother's maiden name?");
                String securityAnswer=scanner.nextLine();
                boolean signUpAnswerStatus=iSignUp.saveUserSecurityAnswer(userID,securityAnswer);
                if(signUpAnswerStatus==true)
                {
                    info= LocalDateTime.now()+":User "+userID+" security answer saved successfully.\n";
                    iLogger.log(info);
                    System.out.println("Sign Up done successfully. Please login now.");
                    // initialising the TwoFactorAuthentication class again so that the recently saved date can
                    // be retrieved.
                    iTwoFactorAuthentication=new TwoFactorAuthentication();
                    System.out.println("Please enter the user id again:");
                    userID=scanner.nextLine();
                    System.out.println("Please enter the password again:");
                    userPassword=scanner.nextLine();
                    loginStatus=iTwoFactorAuthentication.loginSuccessful(userID,userPassword);
                }
                else
                {
                    info= LocalDateTime.now()+":User "+userID+" security answer could not be saved successfully.\n";
                    iLogger.log(info);
                    System.out.println("Sign Up could not be done successfully. Please sign up again.\n");
                }

            }
        }
        else
        {
            loginStatus=iTwoFactorAuthentication.loginSuccessful(userID,userPassword);
        }
        if(loginStatus==true)
        {
            String info=LocalDateTime.now()+":User "+userID+" logged in successfully.\n";
            iLogger.log(info);
            boolean continueExecution=true;
            while(continueExecution)
            {
                System.out.println("Enter your SQL query");
                info=LocalDateTime.now()+":Query entered by user "+ userID+"\n";
                iLogger.log(info);
                String queryInput=scanner.nextLine().toLowerCase();
                IQuery iQuery=new Query();
                String queryType=iQuery.checkQueryType(queryInput);
                info=LocalDateTime.now()+":Query entered by user "+userID+" is of type "+queryType+"\n";
                iLogger.log(info);
                switch (queryType)
                {
                    case "create":
                        if(queryInput.contains("database"))
                        {
                            boolean dataBaseCreation=iQuery.createDataBase(queryInput);
                            if(dataBaseCreation)
                            {
                                System.out.println("Database created successfully!");
                            }
                            else
                            {
                                System.out.println("Database could not be created.");
                            }
                            break;
                        }
                        boolean tableCreationStatus=iQuery.createTable(queryInput);
                        if(tableCreationStatus)
                        {
                            System.out.println("Table created successfully!");
                        }
                        else
                        {
                            System.out.println("Table could not be created.");
                        }
                        break;
                    case "select":
                        iQuery.selectFromTable(queryInput);
                        break;
                    case "insert":
                        boolean insertionStatus=iQuery.insertInTable(queryInput);
                        if(insertionStatus)
                        {
                            System.out.println("Record inserted successfully!");
                        }
                        else
                        {
                            System.out.println("Record could not be inserted successfully!");
                        }
                        break;
                    case "update":
                        boolean updateStatus=iQuery.updateRecords(queryInput);
                        if(updateStatus)
                        {
                            System.out.println("Record(s) updated successfully!");
                        }
                        else
                        {
                            System.out.println("Record(s) could not be updated successfully!");
                        }
                        break;
                    case "delete":
                        boolean deletionStatus=iQuery.deleteFromTable(queryInput);
                        if(deletionStatus)
                        {
                            System.out.println("Record(s) deleted successfully!");
                        }
                        else
                        {
                            System.out.println("Record(s) could not be deleted successfully!");
                        }
                        break;
                    case "start":
                        boolean transactionStatus=iQuery.transaction(queryInput);
                        if(transactionStatus==true)
                        {
                            System.out.println("Transaction completed successfully!");
                        }
                        else
                        {
                            System.out.println("Transaction could not be completed successfully due to rollback!");
                        }
                        break;
                    case "begin":
                        transactionStatus=iQuery.transaction(queryInput);
                        if(transactionStatus)
                        {
                            System.out.println("Transaction completed successfully!");
                        }
                        else
                        {
                            System.out.println("Transaction could not be completed successfully due to rollback!");
                        }
                        break;
                    default:
                        System.out.println("Not a SQL query");
                        break;
                }
                System.out.println("Do you wish to continue? Y/N");
                String answer=scanner.nextLine().toLowerCase();
                if(answer.equals("n"))
                {
                    continueExecution=false;
                }
            }
        }
        else
        {
            System.out.println("Please login again.");
        }

    }
}
