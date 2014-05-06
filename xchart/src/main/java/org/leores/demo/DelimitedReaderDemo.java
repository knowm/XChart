package org.leores.demo;

import java.io.FileNotFoundException;
import java.util.List;

import org.leores.util.DelimitedReader;
import org.leores.util.Demo;
import org.leores.util.Logger;
import org.leores.util.U;
import org.leores.util.able.Processable1;

public class DelimitedReaderDemo extends Demo {

	public void delimitedReader() {
		String sFile = fi("dreader.csv");
		String[] rowToStart1 = { "data", "start1" };
		String[] columnsToRead = { "c4", "c1" };
		String[] rowToEnd = { "data", "end" };
		String[] rowToStart2 = { "data", "start2" };
		String[] rowToStart = { "data", "start\\d" };//Regex matching 
		String[] row;
		try {
			DelimitedReader dr;

			log("Read from file directly:");
			dr = new DelimitedReader(sFile);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			dr.close();

			dr = new DelimitedReader(sFile);
			log("Read from file directly but stops at: " + U.toStr(rowToEnd));
			dr.prep(null, null, rowToEnd);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			log("Continue reading from : " + U.toStr(rowToStart1) + " and stops at: " + U.toStr(rowToEnd));
			dr.prep(rowToStart1, null, rowToEnd);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			log("Continue reading from " + U.toStr(rowToStart2) + " and only read some columns (in a differet order c4 before c1) and stops at: " + U.toStr(rowToEnd));
			dr.prep(rowToStart2, columnsToRead, rowToEnd);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			dr.close();

			log("Read using a loop & regex matching for multiple blocks");
			dr = new DelimitedReader(sFile);
			while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
				while ((row = dr.readValidRow()) != null) {
					log(row);
				}
				log("----------");
			}
			dr.close();

			log("Read columns with same rowToStart and rowToEnd");
			dr = new DelimitedReader(sFile);
			while (dr.prep(rowToStart, columnsToRead, rowToStart)) {
				while ((row = dr.readValidRow()) != null) {
					log(row);
				}
				log("----------");
			}
			dr.close();

			log("Read from null rowToStart:");
			dr = new DelimitedReader(sFile);
			dr.prep(null, columnsToRead, rowToEnd);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			dr.close();
		} catch (FileNotFoundException e) {
			log(e);
		}

		log("To avoid try and catch:");
		DelimitedReader dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		if (dr != null) {
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			dr.close();
		}

		return;
	}

	protected void _readRowPatterns(String[] rowPattern, String[] rowToStart, String[] columnsToRead, String[] rowToEnd) {
		String sFile = fi("dreader.csv");

		DelimitedReader dr;
		String[] row;
		List<String[]> lrow;
		List<String> lrow1;

		log("readValidRow:");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			dr.setValidRowPattern(rowPattern);
			while ((row = dr.readValidRow()) != null) {
				log(row);
			}
			log("----------");
		}
		dr.close();
		log("readValidRow(true):");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			dr.setValidRowPattern(rowPattern);
			while ((row = dr.readValidRow(true)) != null) {
				log(row);
			}
			log("----------");
		}
		dr.close();
		log("readValidRows Block:");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		Processable1 pa1 = new Processable1.ArrayToString();
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			dr.setValidRowPattern(rowPattern);
			while ((lrow = dr.readValidRows(true)) != null) {
				log(U.toStr(lrow, pa1));
			}
			log("----------");
		}
		dr.close();
		log("readValidRows All:");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			dr.setValidRowPattern(rowPattern);
			while ((lrow = dr.readValidRows(false)) != null) {
				log(U.toStr(lrow, pa1));
			}
			log("----------");
		}
		dr.close();
		log("readValidRows All (without setting pattern):");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			//dr.setValidRowPattern(rowPattern);
			while ((lrow = dr.readValidRows(false)) != null) {
				log(U.toStr(lrow, pa1));
			}
			log("----------");
		}
		dr.close();
		log("readValidRows All a single column and supress all log levels below LOG_ERROR:");
		dr = (DelimitedReader) U.newInstance(DelimitedReader.class, sFile);
		dr.setLogOutputLevel(Logger.LOG_ERROR);
		while (dr.prep(rowToStart, columnsToRead, rowToEnd)) {
			dr.setValidRowPattern(rowPattern);
			while ((lrow1 = dr.readValidRows(false, 0)) != null) {
				log(lrow1);
			}
			log("----------");
		}
		dr.close();
	}

	public void readRowPatterns() {
		String[] rowToStartEnd = { "data", "start\\d" };
		String[] columnsToRead = { "c4", "c1" };
		String[] rowPatterns = { null, "1" };

		log("Use null for rowToStart and rowToEnd:");
		_readRowPatterns(rowPatterns, null, columnsToRead, null);

		log("\n\n");
		log("Use rowToStart: " + U.toStr(rowToStartEnd) + " and rowToEnd: " + U.toStr(rowToStartEnd));
		_readRowPatterns(rowPatterns, rowToStartEnd, columnsToRead, rowToStartEnd);
	}

	public static void main(String[] args) {
		DelimitedReaderDemo drd = new DelimitedReaderDemo();
		drd.delimitedReader();
		drd.readRowPatterns();
	}

}
