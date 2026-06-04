package menu;

import account.UserManager;

import java.util.Scanner;

public class UserMenu {
    private static final String DIVIDER = "-------------------------------";

    private final UserManager um;

    public UserMenu(UserManager um) {
    	this.um = um;
    }
    
    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> editPassword(sc);
                case 2 -> editName(sc);
                case 3 -> editPhoneNumber(sc);
                case 4 -> running = false;
                default -> printInvalidChoice();
            }
        }
    }

    private void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("이용하실 메뉴를 선택해주세요.");
        System.out.println("1. 비밀번호 변경");
        System.out.println("2. 이름 변경");
        System.out.println("3. 전화번호 변경");
        System.out.println("4. 뒤로가기");
    }

    private void editPassword(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("[ 비밀번호 변경 ]");
        
        String newPassword = sc.next();
        sc.nextLine();
        
        um.editPassword(newPassword);
    }
    
    private void editName(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("[ 이름 변경 ]");
        
        String newName = sc.next();
        sc.nextLine();
        
        um.editName(newName);
    }
    
    private void editPhoneNumber(Scanner sc) {
        System.out.println(DIVIDER);
        System.out.println("[ 전화번호 변경 ]");
        
        String newPhoneNumber = sc.next();
        sc.nextLine();
        
        um.editPhoneNumber(newPhoneNumber);
    }
    private void printInvalidChoice() {
        System.out.println(DIVIDER);
        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        System.out.println(DIVIDER);
    }
}
