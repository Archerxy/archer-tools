package com.archer.tools.psi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiRSAPsi {

	private static final long ALIVE = 100 * 1000;
	private static final String T_NAME = "psi-";
	
	public static byte[][] client0() {
		return RSAPsi.genKeyPair();
	}
	
	/**
	 * return Pair.p0 = uSet, Pair.p1 = rSet
	 * */
	public static Pair serverSend0(byte[] pk, List<byte[]> set) {
		int size = set.size();
		int pNum = getThreadNum(size);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(pNum, pNum, ALIVE, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(), new InnerThreadFactory(T_NAME));
		int block = size / pNum;
		ArrayList<List<byte[]>> blocks = new ArrayList<>(block + 1);
		for(int i = 0; i < block; i++) {
			blocks.add(set.subList(i * pNum, (i + 1) * pNum));
		}
		if(size % pNum > 0) {
			blocks.add(set.subList(pNum * block, set.size()));
		}
		List<Task> tasks = new ArrayList<>(blocks.size());
		for(List<byte[]> s: blocks) {
			Task t = new Task() {
				public void apply() {
					this.pair = RSAPsi.server0(pk, s);
				}
			};
			tasks.add(t);
			pool.execute(t);
		}
		pool.shutdown();
		long totalAlive = block * ALIVE;
		try {
			pool.awaitTermination(totalAlive, TimeUnit.MICROSECONDS);
		} catch (InterruptedException ignore) {}
		return collectPairs(size, tasks);
	}

	/**
	 * return Pair.p0 = zSet, Pair.p1 = bSet
	 * */
	public static Pair clientSend1(byte[] sk, List<byte[]> uSet, List<byte[]> set) {
		int uSize = uSet.size(), size = set.size();
		int pNum = getThreadNum(size);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(pNum, pNum, ALIVE, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(), new InnerThreadFactory(T_NAME));
		int block = size / pNum;
		int uNum = uSize / block;
		ArrayList<Pair> blocks = new ArrayList<>(block + 1);
		for(int i = 0; i < block; i++) {
			blocks.add(new Pair(uSet.subList(i * uNum, (i + 1) * uNum), set.subList(i * pNum, (i + 1) * pNum)));
		}
		blocks.add(new Pair(uSet.subList(uNum * block, uSet.size()), set.subList(pNum * block, set.size())));
		List<Task> tasks = new ArrayList<>(blocks.size());
		for(Pair p: blocks) {
			Task t = new Task() {
				public void apply() {
					this.pair = RSAPsi.client1(sk, p.getP0(), p.getP1());
				}
			};
			tasks.add(t);
			pool.execute(t);
		}
		pool.shutdown();
		long totalAlive = block * ALIVE;
		try {
			pool.awaitTermination(totalAlive, TimeUnit.MICROSECONDS);
		} catch (InterruptedException ignore) {}
		
		return collectPairs(size, tasks);
	}
	
	/**
	 * return psiSet
	 * */
	public static ArrayList<byte[]> serverSend2(byte[] pk, List<byte[]> zSet, List<byte[]> bSet, List<byte[]> rSet) {
		int zSize = zSet.size(), rSize = rSet.size();
		int pNum = getThreadNum(zSize);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(pNum, pNum, ALIVE, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(), new InnerThreadFactory(T_NAME));
		int block = zSize / pNum;
		int rNum = rSize / block;
		ArrayList<Pair> blocks = new ArrayList<>(block + 1);
		for(int i = 0; i < block; i++) {
			blocks.add(new Pair(zSet.subList(i * pNum, (i + 1) * pNum), rSet.subList(i * rNum, (i + 1) * rNum)));
		}
		blocks.add(new Pair(zSet.subList(pNum * block, zSet.size()), rSet.subList(rNum * block, rSet.size())));
		List<Task> tasks = new ArrayList<>(blocks.size());
		for(Pair p: blocks) {
			Task t = new Task() {
				public void apply() {
					this.bytes = RSAPsi.server2(pk, p.getP0(), p.getP1());
				}
			};
			tasks.add(t);
			pool.execute(t);
		}
		pool.shutdown();
		long totalAlive = block * ALIVE;
		try {
			pool.awaitTermination(totalAlive, TimeUnit.MICROSECONDS);
		} catch (InterruptedException ignore) {}
		List<byte[]> aSet = new ArrayList<>(blocks.size());
		for(Task t: tasks) {
			aSet.addAll(t.bytes());
		}
		return RSAPsi.compare(aSet, bSet);
	}
	
	private static Pair collectPairs(int size, List<Task> tasks) {
		ArrayList<byte[]> set0 = new ArrayList<>(size);
		ArrayList<byte[]> set1 = new ArrayList<>(size);
		for(Task t: tasks) {
			Pair p = t.pair();
			set0.addAll(p.getP0());
			set1.addAll(p.getP1());
		}
		return new Pair(set0, set1);
	}
	
	private static int getThreadNum(int size) {
		int p = Runtime.getRuntime().availableProcessors();
		if(p < 2) {
			p = 2;
		}
		int count = size / p;
		if(count > 10240) {
			return p * 2;
		}
		if(count > 5120) {
			return p;
		}
		if(count > 2560) {
			return 2;
		}
		return 1;
	}
	
	static class InnerThreadFactory implements ThreadFactory {

		String name;
		
		public InnerThreadFactory(String name) {
			this.name = name;
		}
		
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, name + r.hashCode());
		}
	}
	
	static abstract class Task implements Runnable {
		Pair pair;
		
		List<byte[]> bytes;
		
		@Override
		public void run() {
			apply();
		}
		
		public Pair pair() {
			return pair;
		}
		
		public List<byte[]> bytes() {
			return bytes;
		}
		
		public abstract void apply();
		
	}
}
