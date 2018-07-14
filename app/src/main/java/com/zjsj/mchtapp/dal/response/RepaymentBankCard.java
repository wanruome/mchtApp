package com.zjsj.mchtapp.dal.response;

public class RepaymentBankCard {

    public String cardIndex;
    public String accountNo;
    public String mobileNo;
    public String name;
    public String idcardNo;
    public String bankCardType;
    public String bankName;
    public String area;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepaymentBankCard that = (RepaymentBankCard) o;

        return cardIndex != null ? cardIndex.equals(that.cardIndex) : that.cardIndex == null;
    }

    @Override
    public int hashCode() {
        return cardIndex != null ? cardIndex.hashCode() : 0;
    }
}
