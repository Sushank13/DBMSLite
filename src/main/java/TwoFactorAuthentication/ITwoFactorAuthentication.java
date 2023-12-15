package TwoFactorAuthentication;

import java.util.List;
import java.util.Map;

/**
 * This is an interface that has abstract methods
 * to be implemented for the two-factor authentication.
 */
public interface ITwoFactorAuthentication
{
    public Map<String,String> loadUserLoginDetails();
    public Map<String,String> loadUserSecurityAnswers();
    public boolean userExists(String userId);
    public boolean loginSuccessful(String userID, String passWord);

}
