package com.archer.tools.sort;

import java.util.Arrays;

public class MergeSort {
	
	public static <T> void sort(T[] array, Comparison<T> func){
	    T[] tmp = Arrays.copyOf(array, array.length);
	    sort(0,array.length-1, array, tmp, func);
	}
	
	private static <T> void sort(int left,int right, T[] array, T[] tmp, Comparison<T> func){
	    if(left<right){
	        int mid = (left+right)/2;
	        sort(left, mid, array, tmp, func);
	        sort(mid+1, right, array, tmp, func);
	        merge(left, mid, right, array, tmp, func);
	    }
	}
	
	private static <T> void merge(int left, int mid, int right, T[] array, T[] tmp, Comparison<T> func){
	    int i = left;
	    int j = mid+1;
	    int t = 0;
	    while (i<=mid && j<=right){
	        if(func.compare(array[i], array[j]) <= 0){
	        	tmp[t++] = array[i++];
	        } else {
	        	tmp[t++] = array[j++];
	        }
	    }
	    while(i<=mid){
	    	tmp[t++] = array[i++];
	    }
	    while(j<=right){
	    	tmp[t++] = array[j++];
	    }
	    t = 0;
	    while(left <= right){
	        array[left++] = tmp[t++];
	    }
	}
}
