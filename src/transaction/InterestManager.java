package transaction;

public class InterestManager {

    public static final double SHINHAN_RATE = 0.03;
    public static final double KAKAO_RATE = 0.04;
    public static final double BONUS_RATE = 0.001;

    // 은행 이름으로 이자율 반환
    public static double getRate(String bankName) {

        if (bankName.equals("SHINHAN")) {

            return SHINHAN_RATE;

        } else if (bankName.equals("KAKAO")) {

            return KAKAO_RATE;

        }

        return 0.0;
    }
}