package transaction;

import account.BankAccount;
import account.Bank;

public class InterestCalculator {

	// 우대 이자율
    private static final double BONUS_RATE = 0.001;

    // 1년에 한 번 이자 지급
    public static void applyYearlyInterest(
        BankAccount account,
        int accountAgeYear
    ) {

        if (account == null) {
            System.out.println("계좌 정보가 존재하지 않습니다.");
            return;
        }
        
        long balance = account.getBalance();
        
        Bank bank = account.getBank();
        double rate = bank.getRate();
        if (accountAgeYear >= 1) {
            rate += BONUS_RATE;
        }

        long interest = (long)(balance * rate);
        
        if (interest <= 0) return;

        // 잔액 증가
        account.addBalance(interest);

        // 결과 출력
        System.out.println("===== 연 이자 지급 완료 =====");
        System.out.println("은행: " + account.getBank());
        System.out.println("적용 이자율: " + (rate * 100) + "%");
        System.out.println("지급 이자: " + interest + "원");
        System.out.println("현재 잔액: " + account.getBalance() + "원");
    }
}