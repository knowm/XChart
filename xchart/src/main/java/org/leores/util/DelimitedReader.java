package org.leores.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Can read CSV and other delimited text files row by row. By default it will
 * read csv format files. Call setUnWrap(String before, String after, String
 * delimiter) to set the reader to read other formats.
 */
public class DelimitedReader extends Logger {
	protected static String DEF_DELIMITER = ",";
	protected static String DEF_BEFORE = "\"";
	protected static String DEF_AFTER = "\"";

	protected BufferedReader bufReader;
	protected String delimiter;
	protected String before;
	protected String after;
	protected String[] rowToStart;
	protected String[] columnsToRead;
	protected String[] rowToEnd;
	protected int[] iColumnsToRead;
	protected boolean bReady;
	protected String[] rowLastRead;//The last read row.
	//When the readValidRow ends (return null) it will set bRtnRowLastRead to enable the last read row (but invalid and not returned) to be returned by readRow by next call.
	protected boolean bRtnRowLastRead;
	protected boolean bValidRowPatternFound;
	protected String[] validRowPattern;

	/**
	 * 
	 * @param tValidRowPattern
	 * @param bQuote
	 *            whether covert elements of tValidRowPattern as a literal
	 *            pattern String. Pattern.quote(validRowPattern[i])
	 */
	public void setValidRowPattern(String[] tValidRowPattern, boolean bQuote) {
		bValidRowPatternFound = false;
		validRowPattern = tValidRowPattern;
		if (bQuote && validRowPattern != null) {
			for (int i = 0; i < validRowPattern.length; i++) {
				if (validRowPattern[i] != null) {
					validRowPattern[i] = Pattern.quote(validRowPattern[i]);
				}
			}
		}
	}

	public void setValidRowPattern(String[] tValidRowPattern) {
		setValidRowPattern(tValidRowPattern, false);
	}

	public DelimitedReader(Reader reader) {
		bufReader = new BufferedReader(reader);
		delimiter = DEF_DELIMITER;
		before = DEF_BEFORE;
		after = DEF_AFTER;
		bRtnRowLastRead = false;
		bValidRowPatternFound = false;
		validRowPattern = null;
		prep();
	}

	public DelimitedReader(String sFile) throws FileNotFoundException {
		this(new FileReader(sFile));
	}

	public void setUnWrap(String before, String after, String delimiter) {
		if (before != null) {
			this.before = before;
		}
		if (after != null) {
			this.after = after;
		}
		if (delimiter != null) {
			this.delimiter = delimiter;
		}
	}

	protected String[] readRow() {
		String[] rtn = null;

		if (bufReader != null) {
			try {
				if (bRtnRowLastRead) {
					rtn = rowLastRead;
					bRtnRowLastRead = false;
				} else {
					String line = bufReader.readLine();
					if (line != null) {
						rtn = U.unWrap(line, before, after, delimiter);
						rowLastRead = rtn;
					}
				}
			} catch (IOException e) {
				rtn = null;
				log(e);
			}
		}

		return rtn;
	}

	protected int[] findColumnsIndex(String[] row) {
		int[] rtn = null;

		if (row != null && row.length >= columnsToRead.length) {
			int[] iColumnsToReadTemp = new int[columnsToRead.length];
			int i;
			for (i = 0; i < columnsToRead.length; i++) {
				iColumnsToReadTemp[i] = -1;
				for (int j = 0; j < row.length; j++) {
					if (columnsToRead[i].equals(row[j])) {
						iColumnsToReadTemp[i] = j;
						break;
					}
				}
				if (iColumnsToReadTemp[i] == -1) {
					break;
				}
			}
			if (i == columnsToRead.length) {//all columns are found!
				rtn = iColumnsToReadTemp;
			}
		}

		return rtn;
	}

	/**
	 * columnsToRead must be after rowToStart
	 * 
	 * @param rowToStart
	 * @param columnsToRead
	 * @param rowToEnd
	 * @return
	 */
	public boolean prep(String[] rowToStart, String[] columnsToRead, String[] rowToEnd) {
		boolean rtn = true;
		this.rowToStart = rowToStart;
		this.columnsToRead = columnsToRead;
		this.rowToEnd = rowToEnd;
		iColumnsToRead = null;
		bValidRowPatternFound = false;

		String[] row;
		if (rowToStart != null) {
			rtn = false;
			while ((row = readRow()) != null) {
				if (match(rowToStart, row)) {
					rtn = true;
					break;
				}
			}
		}
		if (rtn && columnsToRead != null) {
			while ((row = readRow()) != null) {
				iColumnsToRead = findColumnsIndex(row);
				if (iColumnsToRead != null) {
					break;
				}
			}
			rtn = iColumnsToRead != null;
		}

		bReady = rtn;
		return rtn;
	}

	public boolean prep(String[] rowToStart, String[] columnsToRead) {
		return prep(rowToStart, columnsToRead, null);
	}

	public boolean prep(String[] rowToStart) {
		return prep(rowToStart, null, null);
	}

	public boolean prep() {
		return prep(null, null, null);
	}

	/**
	 * match the first pattern.length items of the two string arrays. null
	 * elements in pattern are ignored (null always match).
	 * 
	 * @param pattern
	 * @param strs
	 * @return
	 */

	public static boolean match(String[] pattern, String[] strs) {
		boolean rtn = false;

		if (pattern != null && strs != null && strs.length >= pattern.length) {
			rtn = true;
			for (int i = 0; i < pattern.length; i++) {
				if (pattern[i] != null) {
					if (strs[i] == null || !strs[i].matches(pattern[i])) {
						rtn = false;
						break;
					}
				}
			}
		}

		return rtn;
	}

	protected String[] readReadyRow() {
		String[] rtn = null;

		if (bReady) {
			String[] row;
			if ((row = readRow()) != null) {
				boolean bRowToEnd = false;
				if (match(rowToEnd, row)) {
					bRowToEnd = true;
					bRtnRowLastRead = true;
				}
				if (!bRowToEnd) {
					if (iColumnsToRead != null) {
						rtn = new String[iColumnsToRead.length];
						for (int i = 0; i < iColumnsToRead.length; i++) {
							int iColumn = iColumnsToRead[i];
							if (iColumn < row.length) {
								rtn[i] = row[iColumn];
							} else {
								//Returned rtn[i] of the not existing columns will be null. Other existing columns will have normal values.
								log(LOG_WARNING, "column [" + columnsToRead[i] + "] does not exist in " + Arrays.asList(row) + "!");
							}
						}
					} else {
						rtn = row;
					}
				}
			}
			bReady = rtn != null;//Stops at the rowToEnd or EOF. Until prep() is called again.
		}

		return rtn;
	}

	public String[] readValidRow(boolean bStopWhenPassBlock) {
		String[] rtn = null;
		if (validRowPattern != null) {
			String[] row;
			while ((row = readReadyRow()) != null) {
				if (match(validRowPattern, row)) {
					rtn = row;
					bValidRowPatternFound = true;
					break;
				} else if (bValidRowPatternFound && bStopWhenPassBlock) {
					bRtnRowLastRead = true;
					bValidRowPatternFound = false;
					break;
				}
			}
		} else {
			rtn = readReadyRow();
		}
		return rtn;
	}

	public String[] readValidRow() {
		return readValidRow(false);
	}

	/**
	 * Return data (between rowToStart and rowToEnd) matching the
	 * validRowPattern (when validRowPattern!=null). If validRowPattern==null it
	 * will return all rows between rowToStart and rowToEnd.
	 * 
	 * @param bBlock
	 *            <b>true:</b> read a continuous block of valid data (quick and
	 *            useful for sorted data); <b>false:</b> read all valid data
	 * @return
	 */
	public List<String[]> readValidRows(boolean bBlock) {
		List<String[]> rtn = new ArrayList<String[]>();
		String[] row;
		while ((row = readValidRow(bBlock)) != null) {
			rtn.add(row);
		}
		if (rtn.size() == 0) {
			rtn = null;
		}
		return rtn;
	}

	/**
	 * Return a data column (between rowToStart and rowToEnd) matching the
	 * validRowPattern (when validRowPattern!=null). If validRowPattern=null it
	 * will return all rows between rowToStart and rowToEnd.
	 * 
	 * @param bBlock
	 *            <b>true:</b> read a continuous block of valid data (quick and
	 *            useful for sorted data); <b>false:</b> read all valid data
	 * @param iRtnColumn
	 *            the index of the returned column according to columnsToRead
	 *            (when columnsToRead!=null); when columnsToRead == null, it is
	 *            the index of the raw row data.
	 * @return
	 */

	public List<String> readValidRows(boolean bBlock, Integer iRtnColumn) {
		List<String> rtn = new ArrayList<String>();
		String[] row;
		while ((row = readValidRow(bBlock)) != null) {
			rtn.add(row[iRtnColumn]);
		}
		if (rtn.size() == 0) {
			rtn = null;
		}
		return rtn;
	}

	public List<String[]> readValidRows(boolean bBlock, Integer... iRtnColumns) {
		List<String[]> rtn = new ArrayList<String[]>();
		String[] row;
		while ((row = readValidRow(bBlock)) != null) {
			if (row.length >= iRtnColumns.length) {
				String[] rtnRow = new String[iRtnColumns.length];
				for (int i = 0; i < iRtnColumns.length; i++) {
					rtnRow[i] = row[iRtnColumns[i]];
				}
				rtn.add(rtnRow);
			}
		}
		if (rtn.size() == 0) {
			rtn = null;
		}
		return rtn;
	}

	public String[] getRowLastRead() {
		return rowLastRead;
	}

	public boolean getBReady() {
		return bReady;
	}

	public void close() {
		if (bufReader != null) {
			try {
				bufReader.close();
			} catch (IOException e) {
				log(e);
			}
		}
	}
}
