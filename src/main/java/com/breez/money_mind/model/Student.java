package com.breez.money_mind.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {

	private int id;
	private String name;
	private int marks;

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				", marks=" + marks +
				'}';
	}
}
