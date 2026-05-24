package main;

import java.util.Scanner;

public class BankMenuScreen {
    private static final String DIVIDER = "-------------------------------";

    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();

            if (!sc.hasNextInt()) {
                sc.nextLine();
                printInvalidChoice();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> printPreparing("계좌 조회");
                case 2 -> printPreparing("입금");
                case 3 -> printPreparing("출금");
                case 4 -> printPreparing("계좌 이체");
                case 5 -> {
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
        System.out.println("1. 계좌 조회");
        System.out.println("2. 입금");
        System.out.println("3. 출금");
        System.out.println("4. 계좌 이체");
        System.out.println("5. 종료");
        System.out.print("선택: ");
    }

    private void printPreparing(String menuName) {
        System.out.println(DIVIDER);
        System.out.println(menuName + " 기능은 준비 중입니다.");
        System.out.println(DIVIDER);
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}