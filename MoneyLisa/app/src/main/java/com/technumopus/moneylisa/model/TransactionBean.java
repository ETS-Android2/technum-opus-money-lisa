package com.technumopus.moneylisa.model;

public class TransactionBean {

    public int tid;
    public String tDate;
    public String tTime;
    public String title;
    public String notes;
    public String dateTrans;
    public String cat;
    public boolean rec;
    public String recStartDate;
    public String recFrequency;
    public String mop;
    public float amount;
    public String currency;
    public String txnType;
    public String txnDetails;
    public String nxtRecDate;
    public String borrowedFrom;
    public float thresholdAmt;



    public TransactionBean() {
    }

    public TransactionBean(String title, String notes) {
        this.title = title;
        this.notes = notes;
    }

    public TransactionBean(String title, float amount, String txnType) {
        this.title = title;
        this.amount = amount;
        this.txnType = txnType;
    }

    // setters

    public void set_tDate(String tDate) {
        this.tDate = tDate;
    }
    public void set_tTime(String tTime) {
        this.tTime = tTime;
    }
    public void set_title(String title) {
        this.title = title;
    }
    public void set_notes(String notes) {
        this.notes = notes;
    }
    public void set_dateTrans(String dateTrans) {
        this.dateTrans = dateTrans;
    }
    public void set_cat(String cat) {
        this.cat = cat;
    }
    public void set_rec(boolean rec) {
        this.rec = rec;
    }
    public void set_recStartDate(String recStartDate) {
        this.recStartDate = recStartDate;
    }
    public void set_recFrequency(String recFrequency) {
        this.recFrequency = recFrequency;
    }
    public void set_mop(String mop) {
        this.mop = mop;
    }
    public void set_amount(float amount) {
        this.amount = amount;
    }
    public void set_currency(String currency) {
        this.currency = currency;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }
    public void set_borrowedFrom(String borrowedFrom) {
        this.borrowedFrom = borrowedFrom;
    }

    // getters

    public String get_tDate() {
        return this.tDate;
    }
    public String get_tTime() {
        return this.tTime;
    }
    public String get_title() {
        return this.title;
    }
    public String get_notes() {
        return this.notes;
    }
    public String get_dateTrans() {
        return this.dateTrans;
    }
    public String get_cat() {
        return this.cat;
    }
    public boolean get_rec() {
        return this.rec;
    }
    public String get_recStartDate() {
        return this.recStartDate;
    }
    public String get_recFrequency() {
        return this.recFrequency;
    }
    public String get_mop() {
        return this.mop;
    }
    public float get_amount() {
        return this.amount;
    }
    public String get_currency() {
        return this.currency;
    }

    public String get_borrowedFrom() {
        return this.borrowedFrom;
    }

    public String getTxnType() {
        return txnType;
    }

    public String getTxnDetails() {
        return txnDetails;
    }

    public void setTxnDetails(String txnDetails) {
        this.txnDetails = txnDetails;
    }

    public String getNxtRecDate() {
        return nxtRecDate;
    }

    public void setNxtRecDate(String nxtRecDate) {
        this.nxtRecDate = nxtRecDate;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public float getThresholdAmt() {
        return thresholdAmt;
    }

    public void setThresholdAmt(float thresholdAmt) {
        this.thresholdAmt = thresholdAmt;
    }


    @Override
    public String toString() {
        return "TransactionBean{" +
                "tid=" + tid +
                ", tDate='" + tDate + '\'' +
                ", tTime='" + tTime + '\'' +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", dateTrans='" + dateTrans + '\'' +
                ", cat='" + cat + '\'' +
                ", rec=" + rec +
                ", recStartDate='" + recStartDate + '\'' +
                ", recFrequency='" + recFrequency + '\'' +
                ", mop='" + mop + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", txnType='" + txnType + '\'' +
                ", txnDetails='" + txnDetails + '\'' +
                ", nxtRecDate='" + nxtRecDate + '\'' +
                ", thresholdAmt=" + thresholdAmt + '\'' +
                ", borrowedFrom=" + borrowedFrom +
                '}';
    }
}
