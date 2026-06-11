package transaction;

import account.Account;
import java.util.ArrayList;

public class TransactionManager {

	private ArrayList<Transaction> transactionList = new ArrayList<>();

	// 입금
    public boolean deposit(Account account, int amount) {
        
        // 1. 입력값 검증
        if (account == null || amount <= 0) {
            System.out.println("잘못된 입력입니다. 입금 금액은 1원 이상이어야 합니다.");
            return false;
        }

        // 2. 오버플로우 방지
        long expectedBalance = account.getBalance() + amount;
        if (expectedBalance < 0) {
            System.out.println("계좌의 최대 보관 한도를 초과하여 입금할 수 없습니다.");
            return false;
        }

        // 3. 잔액 증가
        account.addBalance(amount);

        // 4. 거래 기록 저장
        Transaction transaction = new Transaction(
            "입금", 
            amount, 
            account.getBalance(),
            null,
            account.getAccountNumber(),
            null
        );
        transactionList.add(transaction);

        // 5. 완료 메시지 출력
        System.out.println("입금이 완료되었습니다. 현재 잔액: " + account.getBalance() + "원");
        
        return true;
    }

	// 출금
    public boolean withdraw(Account account, int amount) {
        if (account == null || amount <= 0) {
            System.out.println("잘못된 입력입니다.");
            return false;
        }
        if (account.getBalance() < amount) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }
        account.addBalance(-amount);
        Transaction transaction = new Transaction(
        	"출금",
        	amount,
        	account.getBalance(),
        	account.getAccountNumber(),
            null,
            null
        );
        transactionList.add(transaction);
        System.out.println("출금이 완료되었습니다. 현재 잔액: " + account.getBalance() + "원");
        return true;
    }

	// 송금
	public boolean transfer(
		Account fromAccount,
		Account toAccount,
		int amount,
		String memo
	) {

		// 입력값 검증
		if (fromAccount == null || toAccount == null || amount <= 0) {
			System.out.println("잘못된 입력입니다.");
			return false;
		}

		// 같은 계좌 송금 방지
		if (
			fromAccount.getAccountNumber()
				.equals(toAccount.getAccountNumber())
		) {
			System.out.println("같은 계좌로 송금할 수 없습니다.");
			return false;
		}

		// 출금자의 잔액 확인
		if (fromAccount.getBalance() < amount) {
			System.out.println("잔액이 부족합니다.");
			return false;
		}

		// 출금자 잔액 차감
		fromAccount.addBalance(-amount);

		// 수취인 잔액 증가
		toAccount.addBalance(amount);

		// 송금 기록 저장
		String transferMemo = memo == null ? "" : memo.trim();
		transactionList.add(new Transaction(
			    "출금", amount, fromAccount.getBalance(),
			    fromAccount.getAccountNumber(), toAccount.getAccountNumber(), transferMemo
			));
			transactionList.add(new Transaction(
			    "입금", amount, toAccount.getBalance(),
			    fromAccount.getAccountNumber(), toAccount.getAccountNumber(), transferMemo
			));

		System.out.println("송금이 완료되었습니다.");

		return true;
	}

	// 거래 기록 조회
	public ArrayList<Transaction> getTransactionList(String accountNumber) {
	    ArrayList<Transaction> result = new ArrayList<>();
	    for (Transaction tx : transactionList) {
	        if (accountNumber.equals(tx.getFromAccountNumber()) || accountNumber.equals(tx.getToAccountNumber())) {
	            result.add(tx);
	        }
	    }
	    return result;
	}
}