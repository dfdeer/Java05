package main;

import account.AccountManager;
import account.UserManager;
import transaction.TransactionManager;
import ui.AuthSwingUI;
import ui.MainSwingUI;

public class Main {

    public static void main(String[] args) {
        UserManager um = new UserManager();
        AuthSwingUI authUI = new AuthSwingUI(um);
        AccountManager am = new AccountManager();
        TransactionManager tm = new TransactionManager();
        
        if (authUI.run()) {
            new MainSwingUI(am, tm, um).setVisible(true);
        }
    }
}