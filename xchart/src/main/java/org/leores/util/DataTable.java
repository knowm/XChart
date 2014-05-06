package org.leores.util;

import java.util.ArrayList;
import java.util.List;

import org.leores.ecpt.TRuntimeException;
import org.leores.util.able.Processable2;

public class DataTable {
	public String info;
	protected String[] colNames;
	protected List<Object[]> rows;
	private int nColumn;

	public DataTable(String info, int nColumn) {
		this.info = info;
		this.nColumn = nColumn;
		rows = new ArrayList<Object[]>();
	}

	public String[] getColNames() {
		return colNames;
	}

	public boolean setColNames(String[] colNames) {
		boolean rtn = false;
		if (colNames != null && colNames.length == nColumn) {
			rtn = true;
			this.colNames = colNames;
		}
		return rtn;
	}

	public <B> boolean addAll(boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B> pa2SubList, List<B>... lists) {
		boolean rtn = false;
		List<B>[] tLists = lists;
		if (tLists != null && tLists.length == nColumn) {
			rtn = true;
			if (pa2SubList != null) {
				for (int i = 0; i < nColumn; i++) {
					tLists[i] = U.subList(lists[i], pa2SubList);
				}
			}
			int size = tLists[0].size();
			if (!U.bEqualSize(tLists)) {
				if (bAddUnEqualSizedList) {
					size = U.minSize(tLists);
				} else {
					rtn = false;
					throw new TRuntimeException("addAll: lists are not of the same size for DataTable: " + info);
				}
			}
			for (int i = 0; i < size; i++) {
				Object[] row = new Object[nColumn];
				for (int j = 0; j < nColumn; j++) {
					row[j] = tLists[j].get(i);
				}
				add(row);
			}
		}
		return rtn;
	}

	public <B> boolean addAll(boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B> pa2SubList, double[]... arrays) {
		boolean rtn = false;
		if (arrays != null && arrays.length == nColumn) {
			List<B>[] lists = new List[nColumn];
			for (int i = 0; i < arrays.length; i++) {
				lists[i] = U.parseList(arrays[i]);
			}
			rtn = addAll(bAddUnEqualSizedList, pa2SubList, lists);
		}
		return rtn;
	}

	public <B> boolean addAll(boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B[]> pa2SubList, List<B[]> list) {
		boolean rtn = false;
		if (list != null && list.size() > 0) {
			B[] b0 = list.get(0);
			if (b0 != null && b0.length == nColumn) {
				List tList = list;
				if (pa2SubList != null) {
					tList = U.subList(list, pa2SubList);
				}
				rtn = rows.addAll(tList);
			}
		}
		return rtn;
	}

	public boolean add(Object... objs) {
		boolean rtn = false;
		if (objs != null && objs.length == nColumn) {
			rtn = true;
			rows.add(objs);
		}
		return rtn;
	}

	public int nColumn() {
		return nColumn;
	}

	public int nRow() {
		return rows.size();
	}

	public Object[] getRow(int i) {
		return rows.get(i);
	}

	public Object[] removeRow(int i) {
		return rows.remove(i);
	}
}
