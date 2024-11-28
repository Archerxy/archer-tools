package com.archer.tools.sort;

public class HeapSort {
	
	public static <T> void sort(T[] array, Comparison<T> func) {
	    for(int i = (array.length)/2 - 1; i >= 0; --i) {
	    	adjustHead(array, array.length, i, func);
	    }
	    for(int i = array.length - 1; i >= 1; --i) {
	    	T tmp = array[0];
	    	array[0] = array[i];
	    	array[i] = tmp;
	        adjustHead(array, i, 0, func);
	    }
	}
	
	private static <T> void adjustHead(T[] array, int len, int i, Comparison<T> func) {
	    int parent = i, child = 2*i+1;
	    T tmp = array[i];
	    while (child < len) {
	        if (child + 1 < len && func.compare(array[child], array[child+1]) < 0)
	            ++child;
	        if (func.compare(array[child], tmp) > 0) {
	        	array[parent] = array[child];
	            parent = child;
	            child = 2*child+1;
	        } else
	            break;
	    }
	    array[parent] = tmp;
	}
}
