package transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
	private String transactionType;    // 거래 종류 (출금, 입금, 송금 등)
	private long amount;    // 거래 금액
	private LocalDateTime transactionDate;    // 거래일시
	private long afterBalance; // 거래 후 잔액
	private String fromAccountNumber; // 송금 출금 계좌 번호
	private String toAccountNumber;   // 송금 입금 계좌 번호
	private String memo;             // 송금 메모

	public Transaction(String transactionType, long amount, long afterBalance) {
		this(transactionType, amount, afterBalance, null, null, null);
	}

	public Transaction(
		String transactionType,
		long amount,
		long afterBalance,
		String fromAccountNumber,
		String toAccountNumber,
		String memo
	) {
		this.transactionDate = LocalDateTime.now();
		this.transactionType = transactionType;
		this.amount = amount;
		this.afterBalance = afterBalance;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.memo = memo == null ? "" : memo;
	}

	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("[%s] |종류: %s |금액: %,d원 |잔액: %,d원", transactionDate.format(formatter), transactionType, amount, afterBalance));

		if (fromAccountNumber != null && toAccountNumber != null) {
			builder.append(String.format(" |출금계좌: %s |입금계좌: %s", fromAccountNumber, toAccountNumber));
		}
		if (!memo.isBlank()) {
			builder.append(String.format(" |메모: %s", memo));
		}
		return builder.toString();
	}
	
	public String getDate() {
	    return transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public String getType() {
	    return transactionType;
	}

	public long getAmount() {
	    return amount;
	}

	public long getAfterBalance() {
	    return afterBalance;
	}

	public String getFromAccountNumber() {
	    return fromAccountNumber;
	}

	public String getToAccountNumber() {
	    return toAccountNumber;
	}

	public String getMemo() {
	    return memo;
	}
}
