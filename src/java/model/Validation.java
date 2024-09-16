
package model;


public class Validation {
    
    public static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");
    }

    public static boolean isDouble(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("^\\d+(\\.\\d{2})?$");
    }

    public static boolean isInteger(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("^\\d+$");
    }
    
}
