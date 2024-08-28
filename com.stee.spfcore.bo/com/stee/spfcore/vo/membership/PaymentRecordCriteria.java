package com.stee.spfcore.vo.membership;

public class PaymentRecordCriteria {
    
    private String accrualMonth;
    
    private String balance;

    public String getAccrualMonth() {
        return accrualMonth;
    }

    public void setAccrualMonth(String accrualMonth) {
        this.accrualMonth = accrualMonth;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "PaymentRecordCriteria [accrualMonth=" + accrualMonth
                + ", balance=" + balance + "]";
    }
    
    
}
