package com.epam.training.jjp.domain;

import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) {
		List<Integer> proba = new ArrayList<>();
		proba.add(new Integer(12));
		System.out.println(proba.get(0));
		Integer tmp = proba.get(0);
		tmp += Integer.valueOf(10);
		System.out.println(proba.get(0));
	}
}
