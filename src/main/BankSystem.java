package main;

import account.*;
import java.util.Scanner;

public class BankSystem {

    private Scanner sc = null;
    private final UserAccountManager uam = new UserAccountManager();
    private boolean loggedIn = false;
    private final String DIVIDER = "-------------------------------";

    public boolean run(Scanner sc) {
    	this.sc = sc;
    	
        uam.createUserAccount("admin", "admin", "관리자", "01012345678");
        System.out.println("은행 시스템이 실행되었습니다.");

        sign();
        
        return loggedIn;
    }

    private void sign() {
        printSignMain();

        while (!loggedIn) {
            if (!sc.hasNextInt()) {
                sc.nextLine(); // 잘못된 입력 제거
                printInvalidChoice();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // 개행문자 처리

            switch (choice) {
                case 1 ->
                    signIn();
                case 2 ->
                    signUp();
                default ->
                    printInvalidChoice();
            }
        }
    }

    private void signIn() {
        System.out.println(DIVIDER);
        System.out.println("로그인을 선택하셨습니다.");
        System.out.println(DIVIDER);

        System.out.print("아이디: ");
        String userId = sc.nextLine().trim();
        System.out.print("비밀번호: ");
        String password = sc.nextLine().trim();
        System.out.println(DIVIDER);

        if (uam.login(userId, password)) {
            System.out.println("로그인 성공!");
            System.out.println(DIVIDER);
            loggedIn = true;
        } else {
            System.out.println("로그인 실패! 아이디 또는 비밀번호가 잘못되었습니다.");
            System.out.println(DIVIDER);
            printSignMain();
        }
    }

    private void signUp() {
    	while(true) {
    		 System.out.println(DIVIDER);
    	     System.out.println("회원가입을 선택하셨습니다.");
    	     System.out.println(DIVIDER);

    	     System.out.print("아이디: ");
    	     String userId = sc.nextLine().trim();
    	     System.out.print("비밀번호: ");
    	     String password = sc.nextLine().trim();
    	     System.out.print("이름: ");
    	     String name = sc.nextLine().trim();
    	     System.out.print("전화번호: ");
    	     String phoneNumber = sc.nextLine().trim();
    	     System.out.println(DIVIDER);

    	     if (isBlank(userId) || isBlank(password) || isBlank(name) || isBlank(phoneNumber)) {
    	         System.out.println("모든 항목을 입력해주세요.");
    	         continue;
    	     } else {
    	         if (uam.createUserAccount(userId, password, name, phoneNumber)) {
    	             System.out.println("회원가입이 완료되었습니다.");
    	             System.out.println(DIVIDER);
    	         } else {
    	             System.out.println("회원가입 실패! 아이디 또는 전화번호가 이미 존재합니다.");
    	             System.out.println(DIVIDER);
    	         }
    	         printSignMain();
    	     }
    	}
    }

    private void printSignMain() {
        System.out.println("메인 메뉴:");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.print("선택: ");
    }

    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다.");
        System.out.println(DIVIDER);
        printSignMain();
    }

    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
