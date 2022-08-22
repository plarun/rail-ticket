package reference.httpclient;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client(8060);
            client.createUser("plarun", "reference/password");
            client.loginUser("plarun", "reference/password");
            client.accessResource("plarun");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
