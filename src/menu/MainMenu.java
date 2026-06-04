package menu;

import account.UserManager;
import account.AccountManager;

import java.util.Scanner;
import transaction.TransactionManager;

public class MainMenu {
    private static final String DIVIDER = "-------------------------------";

    private final AccountManager am;
    private final TransactionManager tm;
    private final UserManager um;

    public MainMenu(AccountManager am, TransactionManager tm, UserManager um) {
    	this.am = am;
    	this.tm = tm;
    	this.um = um;
    }
    
    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = sc.nextInt();

            switch (choice) {
            	case 1 -> new AccountMenu(am, um).run(sc);
            	case 2 -> new TransactionMenu(am, tm).run(sc);
            	case 3 -> new UserMenu(um).run(sc);
                case 4 -> running = false;
                default -> printInvalidChoice();
            }
        }
    }

    private void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("이용하실 메뉴를 선택해주세요.");
        System.out.println("1. 계좌 관리");
        System.out.println("2. 거래 하기");
        System.out.println("3. 내 정보 관리");
        System.out.println("4. 로그아웃");
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}
