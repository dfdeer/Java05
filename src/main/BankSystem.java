package main;

import account.*;
import java.util.Scanner;

public class BankSystem {

    private static UserAccountManager UAM = new UserAccountManager();

    public static void run() {
        System.out.println("은행 시스템이 실행되었습니다.");

        UAM.createUserAccount("admin", "1111", "익명", "01011111111");

        /**
         * *new MainScreen();
         *
         *          *while (true) { if (signSystem.login) { System.out.println("로그인
         * 성공!"); // 로그인 성공 후 로직 추가 break; } else { //System.out.println("로그인
         * 실패!"); try { Thread.sleep(1000); // 1초 마다 로그인 상태 확인 } catch
         * (InterruptedException e) { e.printStackTrace(); } } }**
         */
        Scanner sc = new Scanner(System.in);

        printLoginMain();

        OUTER:
        while (true) {
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("로그인을 선택하셨습니다.");
                    System.out.println("-------------------------------");
                    System.out.print("아이디: ");
                    String userId = sc.next();
                    System.out.print("비밀번호: ");
                    String password = sc.next();
                    System.out.println("-------------------------------");
                    if (UAM.login(userId, password)) {
                        System.out.println("로그인 성공!");
                        System.out.println("-------------------------------");
                        // 로그인 성공 후 로직 추가
                        break OUTER;
                    } else {
                        System.out.println("로그인 실패! 아이디 또는 비밀번호가 잘못되었습니다.");
                        System.out.println("-------------------------------");
                        printLoginMain();
                    }
                    // 로그인 로직 추가
                    break;
                case 2:
                    System.out.println("회원가입을 선택하셨습니다.");
                    System.out.println("-------------------------------");
                    // 회원가입 로직 추가
                    break OUTER;
                default:
                    System.out.println("-------------------------------");
                    System.out.println("잘못된 선택입니다.");
                    System.out.println("-------------------------------");
                    printLoginMain();
                    break;
            }
        }
    }

    public static void printLoginMain() {
        System.out.println("메인 메뉴:");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.print("선택: ");
    }
}
