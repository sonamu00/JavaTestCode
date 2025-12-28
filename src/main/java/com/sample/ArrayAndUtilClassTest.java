package com.sample;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 배열 toString과 util 클래스의 toString 차이점 확인
 * 결과:
 * 1. 배열은 저장된 주소 반환
 * 2. util 클래스는 toString 메서드 호출할 때마다 새로운 문자열 객체 만들어서 반환
 */
public class ArrayAndUtilClassTest {
	public static void main(String args[]) {
		String[] strArray = {"a", "b", "c"};
		ArrayList<String> strList = new ArrayList<>();
		strList.add("a");
		strList.add("b");
		strList.add("c");

		System.out.println("Array = " + strArray.toString());
		System.out.println("List = " + strList.toString());
		System.out.println("ArraysToString = " + Arrays.toString(strArray));

	}
}
