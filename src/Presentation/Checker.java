package Presentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

    public static boolean isValid(Object obj) {
        if (!passNull(obj))
            return false;

        if (obj instanceof String) {
            String objString = (String) obj;

            if (!passEmptyString(objString))
                return false;

            if (!passIllegalCharacter(objString))
                return false;

            if (!passLength(objString))
                return false;

        }

        if (obj instanceof Integer || obj instanceof Double) {
            if (!passNegativeNumber(obj))
                return false;
        }

        return true;

    }

    public static boolean isValidNumber(String number){
        if(!isValid(number)) return false;
        for (int i = 0; i <number.length() ; i++) {
            char c = number.charAt(i);
            if(!(c >= '0'&& c <= '9'))
                return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password){
        if(password.length()<6) return false;
        if(!isValid(password))return false;
        boolean hasUpperCase = false, hasLowerCase= false, hasNumber= false;
        for (int i = 0; i <password.length() ; i++) {
            char c = password.charAt(i);
            if(c>='a'&&c<='z') hasLowerCase=true;
            if(c>='A'&&c<='Z') hasUpperCase = true;
            if(c>='0'&&c<='9') hasNumber = true;
        }
        return hasLowerCase&&hasUpperCase&&hasNumber;
    }

    public static boolean isValidEmailAddress(String address){
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);
        return matcher.find();
    }

    private static boolean passNull(Object obj)
    {
        if (obj == null)
            return false;

        return true;
    }

    private static boolean passEmptyString(String st)
    {
        if (st.equals(""))
            return false;

        return true;
    }

    private static boolean passLength(String st)
    {
        if (st.length() > 256)
            return false;

        return true;
    }

    private static boolean passIllegalCharacter(String st)
    {

        if(st.contains("~")||st.contains("|")||st.contains(":")||st.contains(","))return false;


        //if(st.matches(".*[&].*"))
        //    return false;

        return true;
    }

    private static boolean passNegativeNumber(Object obj)
    {
        if (obj instanceof Integer)
            if (((int)obj) < 0)
                return false;

        if (obj instanceof Double)
            if (((double)obj) < 0)
                return false;

        return true;
    }

}
