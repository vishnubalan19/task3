package com.bankapplication.account;
public class Account{
	private int id;
	private int balance;
	private long accountNo;
	public void setAccountNo(long accountNo){
		this.accountNo = accountNo;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setBalance(int balance){
		this.balance = balance;
	}
	public long getAccountNo(){
		return this.accountNo;
	}
	public int getId(){
		return this.id;
	}
	public int getBalance(){
		return this.balance;
	}
}