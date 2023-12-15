package SignUp;

/**
 * This is an interface that provides abstract functions to save the user's id , password
 * and security answer when user signs up for the first time.
 */
public interface ISignUp
{
    public boolean saveUserLoginDetails(String userId,String userPassword);
    public boolean saveUserSecurityAnswer(String userId,String securityAnswer);

}
