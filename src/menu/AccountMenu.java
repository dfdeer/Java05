package menu;

import account.Bank;
import account.UserManager;
import account.Account;
import account.AccountManager;

import java.util.Scanner;

public class AccountMenu {
    private static final String DIVIDER = "-------------------------------";

    private final AccountManager am;
    private final UserManager um;

    public AccountMenu(AccountManager am, UserManager um) {
    	this.am = am;
    	this.um = um;
    }
    
    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();
            
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createBankAccount(sc);
                case 2 -> printAccounts();
                case 3 -> deleteBankAccount(sc);
                case 4 -> running = false;
                default -> printInvalidChoice();
            }
        }
    }

    private void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("이용하실 메뉴를 선택해주세요.");
        System.out.println("1. 계좌 생성");
        System.out.println("2. 계좌 조회");
        System.out.println("3. 계좌 제거");
        System.out.println("4. 뒤로가기");
    }

    private void createBankAccount(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("계좌 생성을 선택하셨습니다.");

        // 아이디 가져오기
        String userId = um.getCurrentUser().getUserId();
        
        // 은행 목록 출력 및 선택
        Bank[] banks = Bank.values();
        for (int i = 0; i < banks.length; i++) {
            System.out.printf("%d. %s%n", i + 1, banks[i]);
        }
        System.out.println("이용하실 은행을 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();
        Bank bank = banks[choice - 1];
        
        // 초기 금액 입력
        System.out.println("초기 금액을 입력하세요: ");
        long balance = sc.nextLong();

        if (am.createBankAccount(userId, bank, balance)) {
        	System.out.println(DIVIDER);
            System.out.println("계좌가 생성되었습니다.");
            System.out.println(DIVIDER);
        } else {
        	System.out.println(DIVIDER);
            System.out.println("계좌를 생성하지 못 했습니다.");
            System.out.println(DIVIDER);
        }
    }
    
    private void printAccount(Account account) {
        System.out.printf(
            "은행: %s | 계좌번호: %s | 예금주 ID: %s | 잔액: %,d원%n",
            account.getBank(),
            account.getAccountNumber(),
            account.getUserId(),
            account.getBalance()
        );
    }
    
    private void printAccounts() {
        System.out.println(DIVIDER);
        System.out.println("계좌 목록");
        
        boolean hasAccount = false;
        String userId = um.getCurrentUser().getUserId();
        for (Account account : am.getAccountList()) {
        	if (account.getUserId().equals(userId)) {
        		printAccount(account);
        		hasAccount = true;
        	}
        }
        
        if (!hasAccount) {
            System.out.println("조회할 계좌가 없습니다.");
        }
        
        System.out.println(DIVIDER);
    }

    private void deleteBankAccount(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("[ 계좌 제거 ]");
        System.out.println("제거할 계좌를 입력하세요: ");
        String accountNumber = sc.next();
        sc.nextLine();
        
        if (am.deleteBankAccount(accountNumber)) {
        	System.out.println(DIVIDER);
            System.out.println("계좌가 제거되었습니다.");
            System.out.println(DIVIDER);
        } else {
        	System.out.println(DIVIDER);
            System.out.println("계좌를 제거하지 못 했습니다.");
            System.out.println(DIVIDER);
        }
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}
