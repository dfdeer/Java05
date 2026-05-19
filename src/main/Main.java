package main;

public class Main {

    public static void main(String[] args) {
        BankSystem bs = new BankSystem();
        bs.run();

        BankMenuScreen bms = new BankMenuScreen();

        if (bs.loggedIn) {
            bms.run();
        }
    }
}
