package com.archer.tools.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class XlsxReader {

	private static final String XL = "xl/sharedStrings.xml";
	private static final String SHEET_START = "xl/worksheets/";
	private static final String SHEET_END = ".xml";

	private static final int DEFAULT_SIZE = 2 * 1024 * 1024;
	
	private static final int CAP = 5;
	
	public static List<Sheet> read(String path) throws IOException {
		List<Sheet> sheets = new ArrayList<>(CAP);
		Map<String, byte[]> sheetConetntMap = new TreeMap<>();
		try(ZipFile zip = new ZipFile(path);
			FileInputStream fIn = new FileInputStream(path)) {
			try(ZipInputStream zipIn = new ZipInputStream(fIn, StandardCharsets.UTF_8)) {
				ZipEntry entry;
				byte[] strData = null;
				while((entry = zipIn.getNextEntry()) != null) {
					if(XL.equals(entry.getName())) {
						InputStream entryStream = zip.getInputStream(entry);
						strData = new byte[entryStream.available()];
						entryStream.read(strData);
					}
					if(entry.getName().startsWith(SHEET_START) && 
					   entry.getName().endsWith(SHEET_END)) {
						byte[] sheetData = new byte[DEFAULT_SIZE];
						InputStream entryStream = zip.getInputStream(entry);
						int count = 0, off = 0;
						while((count = entryStream.read(sheetData, off, sheetData.length - off)) > 0) {
							off += count;
							if(off >= sheetData.length) {
								byte[] t = new byte[sheetData.length << 1];
								System.arraycopy(sheetData, 0, t, 0, off);
								sheetData = t;
							}
						}
						
						String sheetName = entry.getName()
								.replace(SHEET_START, "")
								.replace(SHEET_END, "");
						sheetConetntMap.put(sheetName, Arrays.copyOfRange(sheetData, 0, off));
						sheets.add(new Sheet(sheetName));
					}
				}
				if(strData == null || sheets.size() == 0) {
					throw new RuntimeException("parse failed.");
				}
				String[] strings = parseStrings(strData);
				for(Sheet s: sheets) {
					List<Row> result = 
							parseSheet(sheetConetntMap.get(s.getName()), strings);
					s.rows(result);
				}
			}
		}
		return sheets;
	}
	
	private static final int SST_S = 1; 
	private static final int COUNT_S = 2; 
	private static final int TS_S = 3; 
	private static final int TE_S = 4; 

	private static final char[] SST = {'<','s','s','t'};
	private static final char[] COUNT = {'c','o','u','n','t','=','"'};
	private static final char[] TS = {'<','t','>'};
	private static final char[] TE = {'<','/','t','>'};
	
	private static String[] parseStrings(byte[] data) {
		char[] chars = new String(data, StandardCharsets.UTF_8).toCharArray();
		int i = 0, state = 0, countStart = 0, count = 0;
		for(; i < chars.length; i++) {
			if(state < SST_S && i < chars.length - SST.length) {
				boolean ok = true;
				for(int j = 0; j < SST.length; j++) {
					if(chars[i + j] != SST[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += SST.length;
					state = SST_S;
				}
			}
			if(state == SST_S && i < chars.length - COUNT.length) {
				boolean ok = true;
				for(int j = 0; j < COUNT.length; j++) {
					if(chars[i + j] != COUNT[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += COUNT.length;
					countStart = i;
					state = COUNT_S;
				}
			}
			if(state == COUNT_S && chars[i] == COUNT[COUNT.length - 1]) {
				String countStr = new String(Arrays.copyOfRange(chars, countStart, i));
				count = Integer.parseInt(countStr);
				break ;
			}
		}
		
		String[] strings = new String[count];
		int strStart = 0, strEnd = 0, index = 0;
		state = TS_S;
		for(; i < chars.length; i++) {
			if(state == TS_S && i < chars.length - TS.length) {
				boolean ok = true;
				for(int j = 0; j < TS.length; j++) {
					if(chars[i + j] != TS[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += TS.length;
					strStart = i;
					state = TE_S;
				}
			}
			if(state == TE_S && i < chars.length - TE.length) {
				boolean ok = true;
				for(int j = 0; j < TE.length; j++) {
					if(chars[i + j] != TE[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					strEnd = i;
					strings[index++] = new String(Arrays.copyOfRange(chars, strStart, strEnd));
					i += TE.length;
					state = TS_S;
				}
			}
		}
		return strings;
	}
	
	

	private static final char[] ROWS = {'<','r','o','w',' ','r','=','"'};
	private static final char[] C = {'<','c',' ','r','=','"'};
	private static final char[] VS = {'<','v','>'};
	private static final char[] VE = {'<','/','v','>'};
	private static final char[] C_STR = {'t','=','"','s','"'};

	private static final char MIN_CHAR = 'A';
	private static final  char MAX_CHAR = 'Z';
	
	private static final int[] N_CHAR = new int[MAX_CHAR+1];
	private static final int RADIX = 26;
	
	static {
		for(int i = MIN_CHAR; i < MAX_CHAR + 1; i++) {
			N_CHAR[i] = i - MIN_CHAR;
		}
	}
	

	private static final int ROWS_S = 1; 
	private static final int C_S = 2; 
	private static final int VS_S = 3; 
	private static final int VE_S = 4; 
	
	private static List<Row> parseSheet(byte[] data, String[] strs) {
		char[] chars = new String(data, StandardCharsets.UTF_8).toCharArray();
		int start = 0, end = 0, state = ROWS_S;
		boolean cStr = false;
		List<Row> result = new LinkedList<>();
		Row curRow = null;
		Cell curCell = null;
		char[] nextCol = null;
		for(int i = 0; i < chars.length; i++) {
			if(i < chars.length - ROWS.length) {
				boolean ok = true;
				for(int j = 0; j < ROWS.length; j++) {
					if(chars[i + j] != ROWS[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += ROWS.length;
					int rs = i;
					for(; i < chars.length - ROWS.length; i++) {
						if(chars[i] == ROWS[ROWS.length - 1]) {
							break;
						}
					}
					int row = Integer.parseInt(new String(Arrays.copyOfRange(chars, rs, i)));
					curRow = new Row().line(row).cells(new LinkedList<>());
					result.add(curRow);
					state = C_S;
					nextCol = null;
				}
			}
			if(state == C_S && i < chars.length - C.length) {
				cStr = false;
				boolean ok = true;
				for(int j = 0; j < C.length; j++) {
					if(chars[i + j] != C[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += C.length;

					int rs = i;
					for(; i < chars.length - C.length; i++) {
						if(chars[i] == C[C.length - 1]) {
							break;
						}
					}
					char[] nameChars = Arrays.copyOfRange(chars, rs, i);
					int colE = i;
					for(int j = 0; j < nameChars.length; j++) {
						if(MIN_CHAR > nameChars[j] || MAX_CHAR < nameChars[j] ) {
							colE = j;
							break;
						}
					}
					char[] curCol = Arrays.copyOfRange(nameChars, 0, colE);
					nextCol = getNextCol(nextCol);
					int loss = cmp(nextCol, curCol);
					while(loss > 0) {
						curRow.cells().add(null);
						loss--;
					}
					nextCol = curCol;
					String cellName = new String(nameChars);
					curCell = new Cell().name(cellName);
					curRow.cells().add(curCell);
					
					for(; i < chars.length - C_STR.length; i++) {
						if(chars[i] == VS[VS.length - 1]) {
							break;
						}
						boolean strOk = true;
						for(int j = 0; j < C_STR.length; j++) {
							if(chars[i + j] != C_STR[j]) {
								strOk = false;
								break;
							}
						}
						if(strOk) {
							i += C_STR.length;
							cStr = true;
							break;
						}
					}
					state = VS_S;
				}
			}
			if(state == VS_S && i < chars.length - VS.length) {
				boolean ok = true;
				for(int j = 0; j < VS.length; j++) {
					if(chars[i + j] != VS[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					i += VS.length;
					start = i;
					state = VE_S;
				}
			}
			if(state == VE_S && i < chars.length - VE.length) {
				boolean ok = true;
				for(int j = 0; j < VE.length; j++) {
					if(chars[i + j] != VE[j]) {
						ok = false;
						break;
					}
				}
				if(ok) {
					end = i;
					String v = new String(Arrays.copyOfRange(chars, start, end));
					if(cStr) {
						int index = Integer.parseInt(v);
						curCell.value(strs[index]);
					} else {
						curCell.value(v);
					}
					i += VE.length;
					state = C_S;
				}
			}
		}
		return result;
	}
	
	private static char[] getNextCol(char[] chars) {
		if(chars == null) {
			return new char[] {'A'};
		}
		char last = chars[chars.length - 1];
		if(last >= MAX_CHAR) {
			if(chars.length == 1) {
				return new char[] {'A', 'A'};
			} else {
				int add = 1;
				chars[chars.length - 1] = 'A';
				for(int i = chars.length - 2; i >= 0; i--) {
					if(chars[i] + add > MAX_CHAR) {
						chars[i] = 'A';
						add  = 1;
					} else {
						chars[i] += 1;
						add = 0;
						break;
					}
				}
				if(add == 1) {
					char[] newChars = new char[chars.length + 1];
					newChars[0] = 'A';
					System.arraycopy(chars, 0, newChars, 1, chars.length);
					chars = newChars;
				}
				return chars;
			}
		} else {
			chars[chars.length - 1] += 1;
			return chars;
		}
	}
	
	private static int cmp(char[] min, char[] max) {
		int i = 0, minN = 0, maxN = 0;
		for(; i < min.length; i++) {
			minN += RADIX * minN + N_CHAR[min[i]];
			maxN += RADIX * maxN + N_CHAR[max[i]];
		}

		for(; i < max.length; i++) {
			maxN += RADIX * maxN + N_CHAR[max[i]];
		}
		return maxN - minN;
	}
}
