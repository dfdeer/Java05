package account;

public enum Bank {
	JAVA(0.03),
	CODE(0.01),
	BIT(0.07),
	OVER(0.05),
	LOOP(0.04),
	NULL(0.02);
	
	private final double rate;
	
	Bank(double rate){
		this.rate = rate;
	}
	
	public double getRate() {
        return rate;
    }
}
