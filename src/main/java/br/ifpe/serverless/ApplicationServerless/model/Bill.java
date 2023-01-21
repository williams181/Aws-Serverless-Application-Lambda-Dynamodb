package br.ifpe.serverless.ApplicationServerless.model;

import com.google.gson.Gson;

public class Bill {
	
	private int id;
	private String name;
	private double price;
	
	public Bill(int id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public Bill(String json) {
		Gson gson = new Gson();
		Bill temBill = gson.fromJson(json, Bill.class);
		this.id = temBill.id;
		this.name = temBill.name;
		this.price = temBill.price;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getValor() {
		return price;
	}
	
	public void setValor(double price) {
		this.price = price;
	}

}
