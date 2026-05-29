package main;

import account.Bank;
import account.BankAccount;
import java.util.ArrayList;
import java.util.Scanner;
import transaction.InterestCalculator;
import transaction.Transaction;
import transaction.TransactionManager;

public class BankMenuScreen {
    private static final String DIVIDER = "-------------------------------";
    private static final String EMPTY_INPUT_MESSAGE = "값을 입력해주세요.";
    private static final int EXIT_MENU = 8;

    private final ArrayList<BankAccount> accountList = new ArrayList<>();
    private final TransactionManager transactionManager = new TransactionManager();
    private String accountNumber = "1000000";

    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = readInt(sc, "선택: ");

            switch (choice) {
                case 1 -> createBankAccount(sc);
                case 2 -> printAccounts();
                case 3 -> deposit(sc);
                case 4 -> withdraw(sc);
                case 5 -> transfer(sc);
                case 6 -> printTransactions();
                case 7 -> applyInterest(sc);
                case EXIT_MENU -> {
                    System.out.println(DIVIDER);
                    System.out.println("프로그램을 종료합니다.");
                    System.out.println(DIVIDER);
                    running = false;
                }
                default -> printInvalidChoice();
            }
        }
    }

    private void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("이용하실 메뉴를 선택해주세요.");
        System.out.println("1. 계좌 생성");
        System.out.println("2. 계좌 조회");
        System.out.println("3. 입금");
        System.out.println("4. 출금");
        System.out.println("5. 계좌 이체");
        System.out.println("6. 거래 내역 조회");
        System.out.println("7. 이자 계산");
        System.out.println("8. 종료");
    }

    private void createBankAccount(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("계좌 생성을 선택하셨습니다.");

        String userId = readRequiredText(sc, "아이디: ");
        Bank bank = readBank(sc);
        long balance = readLong(sc, "초기 입금액: ", 0);

        BankAccount account = new BankAccount(userId, bank, createAccountNumber(), balance);
        accountList.add(account);

        System.out.println(DIVIDER);
        System.out.println("계좌가 생성되었습니다.");
        printAccount(account);
        System.out.println(DIVIDER);
    }

    private void printAccounts() {
        System.out.println(DIVIDER);

        if (accountList.isEmpty()) {
            System.out.println("등록된 계좌가 없습니다.");
            System.out.println(DIVIDER);
            return;
        }

        System.out.println("계좌 목록");
        for (BankAccount account : accountList) {
            printAccount(account);
        }

        System.out.println(DIVIDER);
    }

    private void deposit(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("입금을 선택하셨습니다.");

        BankAccount account = readAccount(sc, "입금할 계좌번호: ");
        if (account == null) {
            return;
        }

        int amount = readInt(sc, "입금 금액: ", 1);
        transactionManager.deposit(account, amount);
        System.out.println(DIVIDER);
    }

    private void withdraw(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("출금을 선택하셨습니다.");

        BankAccount account = readAccount(sc, "출금할 계좌번호: ");
        if (account == null) {
            return;
        }

        int amount = readInt(sc, "출금 금액: ", 1);
        transactionManager.withdraw(account, amount);
        System.out.println(DIVIDER);
    }

    private void transfer(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("계좌 이체를 선택하셨습니다.");

        BankAccount fromAccount = readAccount(sc, "출금 계좌번호: ");
        if (fromAccount == null) {
            return;
        }

        BankAccount toAccount = readAccount(sc, "입금 계좌번호: ");
        if (toAccount == null) {
            return;
        }

        int amount = readInt(sc, "이체 금액: ", 1);
        System.out.print("메모: ");
        String memo = sc.nextLine().trim();

        transactionManager.transfer(fromAccount, toAccount, amount, memo);
        System.out.println(DIVIDER);
    }

    private void printTransactions() {
        System.out.println(DIVIDER);

        ArrayList<Transaction> transactions = transactionManager.getTransactionList();
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
        System.out.println(DIVIDER);
        System.out.println("이자 계산을 선택하셨습니다.");

        BankAccount account = readAccount(sc, "이자를 지급할 계좌번호: ");
        if (account == null) {
            return;
        }

        int accountAgeYear = readInt(sc, "계좌 보유 기간(년): ", 0);
        InterestCalculator.applyYearlyInterest(account, accountAgeYear);
        System.out.println(DIVIDER);
    }

    private BankAccount readAccount(Scanner sc, String prompt) {
        String accountNumber = readRequiredText(sc, prompt);
        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            System.out.println(DIVIDER);
            System.out.println("해당 계좌를 찾을 수 없습니다.");
            System.out.println(DIVIDER);
        }

        return account;
    }

    private BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private Bank readBank(Scanner sc) {
        Bank[] banks = Bank.values();

        while (true) {
            System.out.println("은행을 선택해주세요.");
            for (int i = 0; i < banks.length; i++) {
                System.out.printf("%d. %s (연 %.1f%%)%n", i + 1, banks[i], banks[i].getRate() * 100);
            }

            int choice = readInt(sc, "선택: ");
            if (choice >= 1 && choice <= banks.length) {
                return banks[choice - 1];
            }

            printInvalidChoice();
        }
    }

    private String readRequiredText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(EMPTY_INPUT_MESSAGE);
        }
    }

    private int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printInvalidChoice();
            }
        }
    }

    private int readInt(Scanner sc, String prompt, int minValue) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value >= minValue) {
                return value;
            }

            System.out.printf("%,d 이상의 값을 입력해주세요.%n", minValue);
        }
    }

    private long readLong(Scanner sc, String prompt, long minValue) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                long value = Long.parseLong(input);
                if (value >= minValue) {
                    return value;
                }
                System.out.printf("%,d 이상의 값을 입력해주세요.%n", minValue);
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
    }

    private String createAccountNumber() {
        int countedAccount = Integer.parseInt(accountNumber);
        countedAccount++;
        accountNumber = String.valueOf(countedAccount);
        return accountNumber;
    }

    private void printAccount(BankAccount account) {
        System.out.printf(
            "은행: %s | 계좌번호: %s | 예금주 ID: %s | 잔액: %,d원%n",
            account.getBank(),
            account.getAccountNumber(),
            account.getUserId(),
            account.getBalance()
        );
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}
