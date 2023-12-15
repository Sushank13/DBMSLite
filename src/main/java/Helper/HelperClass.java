package Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HelperClass
{
    /**
     *This method does the password hashing to secure the passwords
     * @param passWord is the password which will be converted to some hash value for encryption
     * @return the hashed value of the password provided
     * @link https://www.baeldung.com/java-md5
     */
    public String passwordHashing(String passWord)
    {
        try
        {
            /**
             * instantiating the MessageDigest class  with the type of hashing
             * algorithm to be used. In this case, it is MD5.
             */
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            /**
             * converting the password string to a byte array as it is required
             * by the Message Digest instance.
             */
            messageDigest.update(passWord.getBytes());
            /**
             * calculating the hash value of the password
             */
            byte hashPasswordValue[]=messageDigest.digest();
            /**
             * converting the hash value into more readable hexadecimal representation
             */
            String readAbleHashValue=convertByteToHex(hashPasswordValue);
            return readAbleHashValue;
        }
        catch(NoSuchAlgorithmException e)
        {
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * This method converts a byte array into a hexadecimal string
     * @param hashPasswordValue is the hashed password to be converted to hexadecimal
     * @return hexadecimal format of the hashed password
     * @link https://www.baeldung.com/java-string-formatter
     */
    private String convertByteToHex(byte hashPasswordValue[])
    {
        StringBuilder sb=new StringBuilder();
        for(byte b: hashPasswordValue)
        {
            sb.append(String.format("%02x",b));
        }
        String readableHashedValue=sb.toString();
        return readableHashedValue;
    }
}
