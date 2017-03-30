package com.pisight.pimoney1.beans;

public class Person {
	
	private String name = null;
	private int age;
	private String adress = null;
	
	public Person(String name, int age, String address){
		this.name = name;
		this.age = age;
		this.adress = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}

}
