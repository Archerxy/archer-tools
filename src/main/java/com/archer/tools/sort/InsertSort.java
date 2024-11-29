package com.archer.tools.sort;

public class InsertSort {
	
	public static <T> void sort(T[] array, Comparison<T> func) {
		int length = array.length;
		for(int i = 1; i < length; i++) {
			for(int j = i; j > 0; j--) {
				if(func.compare(array[j], array[j-1]) < 0) {
					T tmp = array[j];
					array[j] = array[j-1];
					array[j-1] = tmp;
				} else {
					break;
				}
			}
		}
	}

}
