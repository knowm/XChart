package org.leores.util;

import java.util.ArrayList;
import java.util.List;

import org.leores.util.able.Processable2;

public class DataTableSet {
	public String info;
	public List<DataTable> members;

	public DataTableSet(String info) {
		this.info = info;
		members = new ArrayList<DataTable>();
	}

	public boolean add(DataTable dataTable) {
		boolean rtn = false;
		if (dataTable != null) {
			rtn = true;
			members.add(dataTable);
		}
		return rtn;
	}

	public boolean add(DataTable... dataTables) {
		boolean rtn = false;
		if (dataTables != null && dataTables.length > 0) {
			rtn = true;
			for (int i = 0; i < dataTables.length; i++) {
				if (!add(dataTables[i])) {
					rtn = false;
					break;
				}
			}
		}
		return rtn;
	}

	public DataTable addNewDataTable(String info, int nColumn) {
		DataTable rtn = new DataTable(info, nColumn);
		add(rtn);
		return rtn;
	}

	public <B> DataTable addNewDataTable(String info, boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B> pa2SubList, List<B>... lists) {
		DataTable rtn = new DataTable(info, lists.length);
		if (rtn.addAll(bAddUnEqualSizedList, pa2SubList, lists)) {
			add(rtn);
		} else {
			rtn = null;
		}
		return rtn;
	}

	public <B> DataTable addNewDataTable(String info, boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B> pa2SubList, double[]... arrays) {
		DataTable rtn = new DataTable(info, arrays.length);
		if (rtn.addAll(bAddUnEqualSizedList, pa2SubList, arrays)) {
			add(rtn);
		} else {
			rtn = null;
		}
		return rtn;
	}

	public <B> DataTable addNewDataTable(String info, boolean bAddUnEqualSizedList, Processable2<Boolean, Integer, B[]> pa2SubList, List<B[]> list) {
		DataTable rtn = null;
		if (list != null && list.size() > 0) {
			B[] b0 = list.get(0);
			if (b0 != null) {
				rtn = new DataTable(info, b0.length);
				if (rtn.addAll(bAddUnEqualSizedList, pa2SubList, list)) {
					add(rtn);
				} else {
					rtn = null;
				}
			}
		}
		return rtn;
	}

	public <B> DataTable addNewDataTable(String info, Processable2<Boolean, Integer, B> pa2SubList, List<B>... lists) {
		return addNewDataTable(info, false, pa2SubList, lists);
	}

	public <B> DataTable addNewDataTable(String info, List<B>... lists) {
		return addNewDataTable(info, false, null, lists);
	}

	public <B> DataTable addNewDataTable(String info, Processable2<Boolean, Integer, B> pa2SubList, double[]... arrays) {
		return addNewDataTable(info, false, pa2SubList, arrays);
	}

	public DataTable addNewDataTable(String info, double[]... arrays) {
		return addNewDataTable(info, false, null, arrays);
	}

	public <B> DataTable addNewDataTable(String info, Processable2<Boolean, Integer, B[]> pa2SubList, List<B[]> list) {
		return addNewDataTable(info, false, pa2SubList, list);
	}

	public <B> DataTable addNewDataTable(String info, List<B[]> list) {
		return addNewDataTable(info, false, null, list);
	}

	public int size() {
		return members.size();
	}

	public DataTable get(int i) {
		return members.get(i);
	}

	public DataTable remove(int i) {
		return members.remove(i);
	}
}
