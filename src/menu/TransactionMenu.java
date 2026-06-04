package menu;

import account.Account;
import account.AccountManager;

import java.util.ArrayList;
import java.util.Scanner;
import transaction.InterestCalculator;
import transaction.Transaction;
import transaction.TransactionManager;

public class TransactionMenu {
    private static final String DIVIDER = "-------------------------------";

    private final AccountManager am;
    private final TransactionManager tm;
    
    private Account currentAccount = null;

    public TransactionMenu(AccountManager am, TransactionManager tm) {
    	this.am = am;
    	this.tm = tm;
    }
    
    private boolean inputAccountNumber(Scanner sc) {
    	System.out.println("이용할 계좌 번호를 입력하세요: ");
    	String accountNumber = sc.next();
    	sc.nextLine();
    	
    	currentAccount = am.findBankAccount(accountNumber);
    	if (currentAccount == null) {
    		System.out.println("계좌를 찾을 수 없습니다.");
        	return false;
    	}
    	return true;
    }
    public void run(Scanner sc) {

        boolean running = inputAccountNumber(sc);

        while (running) {
            printMenu();

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> deposit(sc);
                case 2 -> withdraw(sc);
                case 3 -> transfer(sc);
                case 4 -> printTransactions();
                case 5 -> applyInterest(sc);
                case 6 -> running = false;
                default -> printInvalidChoice();
            }
        }
    }

    private void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("이용하실 메뉴를 선택해주세요.");
        System.out.println("1. 입금");
        System.out.println("2. 출금");
        System.out.println("3. 이체");
        System.out.println("4. 거래 내역");
        System.out.println("5. 이자 계산");
        System.out.println("6. 뒤로가기");
    }

    private void deposit(Scanner sc) {
        if (currentAccount == null) return;
        
        System.out.println(DIVIDER);
        System.out.println("입금을 선택하셨습니다.");

        System.out.print("입금 금액: ");
        int amount = sc.nextInt();
        
       if (tm.deposit(currentAccount, amount)) {
        	System.out.printf("%d 원을 입금하였습니다.%n", amount);
        } else {
        	System.out.println("입금에 실패하였습니다.");
        }
        
        System.out.println(DIVIDER);
    }

    private void withdraw(Scanner sc) {
        if (currentAccount == null) return;
        
        System.out.println(DIVIDER);
        System.out.println("출금을 선택하셨습니다.");

        System.out.print("출금 금액: ");
        int amount = sc.nextInt();
        
        if (tm.withdraw(currentAccount, amount)) {
        	System.out.printf("%d 원을 출금하였습니다.%n", amount);
        } else {
        	System.out.println("출금에 실패하였습니다.");
        }
        System.out.println(DIVIDER);
    }

    private void transfer(Scanner sc) {
        if (currentAccount == null) return;
        
        System.out.println(DIVIDER);
        System.out.println("계좌 이체를 선택하셨습니다.");

        System.out.print("이체할 계좌번호: ");
        String toAccountNumber = sc.next();
        Account toAccount = am.findBankAccount(toAccountNumber);
        if (toAccount == null) {
            return;
        }

        System.out.print("이체 금액: ");
        int amount = sc.nextInt();
        
        System.out.print("메모: ");
        String memo = sc.nextLine().trim();

        if (tm.transfer(currentAccount, toAccount, amount, memo)) {
        	System.out.printf("%s 계좌에 %d 원을 이체하였습니다.%n", toAccount, amount);
        } else {
        	System.out.println("이체에 실패하였습니다.");
        }
        
        System.out.println(DIVIDER);
    }

    private void printTransactions() {
        System.out.println(DIVIDER);

        ArrayList<Transaction> transactions = tm.getTransactionList();
        if (transactions.isEmpty()) {
            System.out.println("거래 내역이 없습니다.");
            System.out.println(DIVIDER);
            return;
        }

        System.out.println("거래 내역");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println(DIVIDER);
    }

    private void applyInterest(Scanner sc) {
        if (currentAccount == null) return;
        
        System.out.println(DIVIDER);
        System.out.println("이자 계산을 선택하셨습니다.");
        
        System.out.print("계좌 보유 기간(년): ");
        int accountAgeYear = sc.nextInt();
        InterestCalculator.applyYearlyInterest(currentAccount, accountAgeYear);
        System.out.println(DIVIDER);
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}
