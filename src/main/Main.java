package main;

import java.util.Scanner;

import account.AccountManager;
import account.UserManager;
import menu.MainMenu;
import transaction.TransactionManager;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        UserManager um = new UserManager();
        AuthSystem as = new AuthSystem(um);
        AccountManager am = new AccountManager();
        TransactionManager tm = new TransactionManager();
        
        if (as.run(sc)) {
            new MainMenu(am, tm, um).run(sc);
        }
    }
}
