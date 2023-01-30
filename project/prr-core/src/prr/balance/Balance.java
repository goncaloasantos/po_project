package prr.balance;

public class Balance {
    private long _paid = 0;
    private long _debt = 0;

    public Balance(){}

    public void addDebts(double d){
        _debt += d;
    }

    public void addPaid(double p){
        _paid += p;
        _debt -= p;
    }

    public long getDebt(){
        return _debt;
    }

    public void setDebtTo0(){_debt = 0;}

    public void setPaymentsTo0(){_paid = 0;}

    public long getPaid(){
        return _paid;
    }

    public boolean hasPositiveBalance(){
        return (_paid - _debt) > 0 ;
    }
}
