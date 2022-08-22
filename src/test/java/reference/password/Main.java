package reference.password;

public class Main {
    public static void main(String[] args) {
        PasswordAuthentication auth = new PasswordAuthentication();
        String token = auth.hash("0=!g1%*&Wc".toCharArray());
        boolean isValid = auth.authenticate("0=!g1%*&Wc".toCharArray(), token);
        System.out.println(isValid + " " + token);
    }
}
