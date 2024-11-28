package com.archer.tools.sort;

public class ShellSort {
	
	public static <T> void sort(T[] array, Comparison<T> func) {
		int length = array.length;
		int[] h_arr = generateH(length);
		int h_index = h_arr.length - 1;
		while(h_index >= 0) {
			int h = h_arr[h_index];
			for(int index = 0; index < h; index++) {
				for(int i = h + index; i < length; i = i + h) {
					for(int j = i; j >= h; j = j - h) {
						if(func.compare(array[j], array[j-h]) < 0) {
							T tmp = array[j];
							array[j] = array[j-h];
							array[j-h] = tmp;
						} else {
							break;
						}
					}
				}
			}
			h_index--;
		}
	}
	
	private static int[] generateH(int length) {
		int[] res = new int[(int)(Math.log(2*length+1)/Math.log(3))];
		for(int i = 0; i < res.length; i++) {
			res[i] = (int)((Math.pow(3, i+1)-1)/2);
		}
		return res;
	}
}
