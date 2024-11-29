package com.archer.tools.sort;

public class QuickSort {
	
	public static <T> void sort(T[] array, Comparison<T> func) {
		loop(0, array.length-1, array, func);
	}
	
	private static <T> void loop(int low,int high, T[] array, Comparison<T> func) {
		if(low < high) {
			int left = low, right = high;
			T key = array[low];
			while(left < right) {
				for(; left < right; right--) { 
					if(func.compare(array[right], key) < 0) {
						array[left] = array[right];
						break;
					}
				}
				for(; left < right; left++) {
					if(func.compare(array[left], key) > 0) {
						array[right] = array[left];
						break;
					}
				}
			}
			array[right] = key;
			loop(low, left - 1, array, func);
			loop(right + 1, high, array, func);
		}
	}
}
