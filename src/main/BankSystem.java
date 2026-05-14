package main;

import ui.*;

public class BankSystem {

    public static void run() {
        System.out.println("은행 시스템이 실행되었습니다.");

        new MainScreen();

        while (true) {
            if (signSystem.login) {
                System.out.println("로그인 성공!");
				// 로그인 성공 후 로직 추가
				break;
            } else {
                //System.out.println("로그인 실패!");
				try {
					Thread.sleep(1000); // 1초 마다 로그인 상태 확인
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    }
}
