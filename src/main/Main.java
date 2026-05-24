package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        BankSystem bs = new BankSystem();
        BankMenuScreen bms = new BankMenuScreen();

        if (bs.run(sc)) {
            bms.run(sc);
        }
    }
}
