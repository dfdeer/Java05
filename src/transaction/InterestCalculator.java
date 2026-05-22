package transaction;

import account.BankAccount;
import account.BankList;

public class InterestCalculator {

    // 이자 계산
    public static long calculateInterest(
        BankAccount account,
        int accountAgeYear
    ) {

        // 계좌 확인
        if (account == null) {
            System.out.println("계좌 정보가 존재하지 않습니다.");
            return 0;
        }

        long balance = account.getBalance();

        double rate = 0.0;

        // 은행별 기본 이자율
        if (account.getBankName() == BankList.SHINHAN) {

            rate = InterestManager.getShinhanRate();

        } else if (account.getBankName() == BankList.KAKAO) {

            rate = InterestManager.getKakaoRate();

        } else {

            System.out.println("이자율이 등록되지 않은 은행입니다.");
            return 0;
        }

        // 1년 이상 사용 시 우대 이자율 추가
        if (accountAgeYear >= 1) {

            rate += InterestManager.getBonusRate();

        }

        // 최종 이자 계산
        long interest = (long)(balance * rate);

        return interest;
    }

    // 1년에 한 번 이자 지급
    public static void applyYearlyInterest(
        BankAccount account,
        int accountAgeYear
    ) {

        if (account == null) {
            System.out.println("계좌 정보가 존재하지 않습니다.");
            return;
        }

        long interest =
            calculateInterest(account, accountAgeYear);

        // 이자가 없으면 종료
        if (interest <= 0) {
            return;
        }

        // 잔액 증가
        account.addBalance((int)interest);

        // 적용된 최종 이자율 계산용
        double finalRate = 0.0;

        if (account.getBankName() == BankList.SHINHAN) {

            finalRate = InterestManager.getShinhanRate();

        } else if (account.getBankName() == BankList.KAKAO) {

            finalRate = InterestManager.getKakaoRate();

        }

        // 우대 금리 포함
        if (accountAgeYear >= 1) {

            finalRate += InterestManager.getBonusRate();

        }

        // 결과 출력
        System.out.println("===== 연 이자 지급 완료 =====");
        System.out.println("은행: " + account.getBankName());
        System.out.println("적용 이자율: " + (finalRate * 100) + "%");
        System.out.println("지급 이자: " + interest + "원");
        System.out.println("현재 잔액: " + account.getBalance() + "원");
    }
}