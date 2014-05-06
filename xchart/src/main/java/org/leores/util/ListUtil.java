package org.leores.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.leores.util.able.Processable1;
import org.leores.util.able.Processable2;

public class ListUtil extends StrUtil {

	public static <A> boolean bEqual(A a1, A a2) {
		boolean rtn = false;

		if (a1 != null) {
			rtn = a1.equals(a2);
		} else if (a2 == null) {
			//a1 and a2 are both null
			rtn = true;
		}

		return rtn;
	}

	public static <A> List<A> subList(Iterator<A> itr, Processable1<Boolean, A> pa1) {
		List<A> rtn = null;

		if (itr != null && pa1 != null) {
			rtn = new ArrayList<A>();
			while (itr.hasNext()) {
				A a = itr.next();
				if (pa1.process(a)) {
					rtn.add(a);
				}
			}
		}

		return rtn;
	}

	/**
	 * This implementation is optimised for ArrayList. It is faster than using
	 * Iterator!
	 * 
	 * @param list
	 * @param tBa
	 * @return
	 */
	public static <A> List<A> subList(List<A> list, Processable1<Boolean, A> pa1) {
		List<A> rtn = null;

		if (list != null && pa1 != null) {
			rtn = new ArrayList<A>();
			for (int i = 0, mi = list.size(); i < mi; i++) {
				A a = list.get(i);
				if (pa1.process(a)) {
					rtn.add(a);
				}
			}
		}

		return rtn;
	}

	public static <B> List<B> subList(List<B> list, Processable2<Boolean, Integer, B> pa2) {
		List<B> rtn = null;
		if (list != null && pa2 != null) {
			rtn = new ArrayList<B>();
			for (int i = 0, mi = list.size(); i < mi; i++) {
				B b = list.get(i);
				if (pa2.process(i, b)) {
					rtn.add(b);
				}
			}
		}
		return rtn;
	}

	public static <A> Integer sizeSubList(List<A> list, Processable1<Boolean, A> pa1) {
		Integer rtn = null;

		if (list != null && pa1 != null) {
			rtn = 0;
			for (int i = 0, size = list.size(); i < size; i++) {
				A a = list.get(i);
				if (pa1.process(a)) {
					rtn++;
				}
			}
		}

		return rtn;
	}

	public static <A> boolean bEqualSize(List<A>... lists) {
		boolean rtn = true;
		if (lists != null && lists.length > 1) {
			int size = lists[0].size();
			for (int i = 1; i < lists.length; i++) {
				if (size != lists[i].size()) {
					rtn = false;
					break;
				}
			}
		}
		return rtn;
	}

	public static <A> int minSize(List<A>... lists) {
		int rtn = -1;
		if (lists != null && lists.length > 0) {
			rtn = lists[0].size();
			for (int i = 1; i < lists.length; i++) {
				int iSize = lists[i].size();
				if (iSize < rtn) {
					rtn = iSize;
				}
			}
		}
		return rtn;
	}

	/**
	 * The list parameter has to implement Comparable.
	 * 
	 * @param list
	 * @return
	 */
	public static boolean removeMinElement(List list) {
		boolean rtn = false;

		if (list != null) {
			int size = list.size();
			if (size > 0) {
				int iMin = 0;
				Comparable eMin = (Comparable) list.get(iMin);
				while (eMin == null && iMin < size - 1) {
					iMin++;
					eMin = (Comparable) list.get(iMin);
				}
				for (int i = iMin + 1; i < size; i++) {
					Comparable e = (Comparable) list.get(i);
					if (e != null && e.compareTo(eMin) < 0) {
						eMin = e;
						iMin = i;
					}
				}
				if (eMin != null) {
					list.remove(iMin);
					rtn = true;
				}
			}
		}

		return rtn;
	}

	/**
	 * The list parameter has to implement Comparable.
	 * 
	 * @param list
	 * @return
	 */
	public static boolean removeMaxElement(List list) {
		boolean rtn = false;

		if (list != null) {
			int size = list.size();
			if (size > 0) {
				int iMax = 0;
				Comparable eMax = (Comparable) list.get(iMax);
				while (eMax == null && iMax < size - 1) {
					iMax++;
					eMax = (Comparable) list.get(iMax);
				}
				for (int i = iMax + 1; i < size; i++) {
					Comparable e = (Comparable) list.get(i);
					if (e != null && e.compareTo(eMax) > 0) {
						eMax = e;
						iMax = i;
					}
				}
				if (eMax != null) {
					list.remove(iMax);
					rtn = true;
				}
			}
		}

		return rtn;
	}

	/**
	 * Remove the elements with specified id(s) in a list. return true if all
	 * elements are removed.
	 * 
	 * @param list
	 * @param lids
	 * @return
	 */

	public static boolean removeElements(List list, List lids) {
		boolean rtn = false;

		if (list != null && lids != null) {
			rtn = true;
			Collections.sort(lids);//sort it to ascending order
			try {
				for (int i = lids.size() - 1; i >= 0; i--) {
					int id = (Integer) lids.get(i);
					list.remove(id);
				}
			} catch (Exception e) {
				rtn = false;
				tLog(e);
			}

		}

		return rtn;
	}

	public static <A> int processElements(List<A> list, Processable1<A, ? super A> pa1) {
		int rtn = 0;
		if (list != null && pa1 != null) {
			for (int i = 0, mi = list.size(); i < mi; i++) {
				A a = list.get(i);
				A a2 = pa1.process(a);
				list.set(i, a2);
				rtn++;
			}
		}
		return rtn;
	}

	/**
	 * Remove the elements with specified id(s) in a list. return true if all
	 * elements are removed.
	 * 
	 * @param list
	 * @param ids
	 * @return
	 */

	public static boolean removeElements(List list, Integer... ids) {
		return removeElements(list, Arrays.asList(ids));
	}

	/**
	 * When pa2 is not null, if two elements a1 a2 satisfy pa2.process(a1,a2),
	 * then remove one of them. When pa2 is null, the default setting is to
	 * remove one of them a1 and a2 if e1==e2
	 * 
	 * @param list
	 * @param pa2
	 * @return
	 */
	public static <A> List<A> refineList(List<A> list, Processable2<Boolean, A, A> pa2) {
		List<A> rtn = null;

		if (list != null) {
			int size = list.size();
			rtn = new ArrayList<A>(size);
			for (int i = 0; i < size; i++) {
				A a = list.get(i);
				boolean bToAdd = true;
				for (int j = 0, size2 = rtn.size(); j < size2; j++) {
					A a2 = rtn.get(j);
					if ((pa2 == null && bEqual(a, a2)) || (pa2 != null && pa2.process(a, a2))) {
						bToAdd = false;
						break;
					}
				}
				if (bToAdd) {
					rtn.add(a);
				}
			}
		}

		return rtn;
	}

	/**
	 * Use a modified version of Knuth-Fisher-Yates shuffle algorithm to
	 * partially shuffle the list and return the expected number of random items
	 * in a list.
	 * 
	 * @param list
	 * @param nSub
	 * @param randomer
	 * @return
	 */
	public static List subList(List list, Integer nSub, Randomer randomer) {
		List rtn = null;

		if (list != null && nSub != null && randomer != null) {
			int lSize = list.size();
			if (lSize > nSub) {
				rtn = new ArrayList();
				Object[] objs = list.toArray();
				for (int i = objs.length - 1; i > objs.length - nSub - 1; i++) {
					int iRand = randomer.nextInt(i + 1);
					Object oRand = objs[iRand];
					objs[iRand] = objs[i];
					//This is not necessary here as we do not really need the final shuffle result.
					//objs[i] = oRand;
					rtn.add(oRand);
				}

			} else {
				tLog("list.size < nSub! ", lSize, nSub);
			}
		}

		return rtn;
	}

	/**
	 * Sort the elements in <b>lVals</b> and <b>lKeys</b> according to the
	 * values of elements in <b>lVals</b>, so that elements in <b>lKeys</b>
	 * remain in the same position to their counterparts in <b>lVals</b>.
	 * 
	 * @param lVals
	 * @param lKeys
	 * @return
	 */

	public static boolean sortValKeyLists(List lVals, List lKeys) {
		boolean rtn = false;

		if (lVals != null && lKeys != null) {
			int size = lVals.size();
			if (size == lKeys.size()) {
				rtn = true;
				List<ValKeyPair> lValKeyPair = new ArrayList<ValKeyPair>(size);
				for (int i = 0; i < size; i++) {
					Object val = lVals.get(i);
					Object key = lKeys.get(i);
					ValKeyPair vkp = new ValKeyPair(val, key);
					lValKeyPair.add(vkp);
				}
				Collections.sort(lValKeyPair);
				lVals.clear();
				lKeys.clear();
				for (int i = 0; i < size; i++) {
					ValKeyPair vkp = lValKeyPair.get(i);
					lVals.add(vkp.val);
					lKeys.add(vkp.key);
				}
			}
		}

		return rtn;
	}

	public static String toStr(Iterator itr, Processable1<String, Object> pa1) {
		String rtn = null;

		if (itr != null && pa1 != null) {
			rtn = "[";

			if (itr.hasNext()) {
				Object obj = itr.next();
				rtn += pa1.process(obj);
			}

			while (itr.hasNext()) {
				Object obj = itr.next();
				rtn += de + pa1.process(obj);
			}

			rtn += "]";
		}

		return rtn;
	}

	public static String toStr(List list, Processable1<String, Object> pa1) {
		String rtn = null;

		if (list != null) {
			rtn = toStr(list.iterator(), pa1);
		}

		return rtn;
	}

	public static List parseList(double[] array) {
		List rtn = null;
		if (array != null) {
			rtn = new ArrayList();
			for (int i = 0; i < array.length; i++) {
				rtn.add(array[i]);
			}
		}
		return rtn;
	}

	@SuppressWarnings("unchecked")
	public static <A> A[] toArray(List<A> list, Class<A> componentClass) {
		A[] rtn = null;
		if (list != null && componentClass != null) {
			rtn = (A[]) Array.newInstance(componentClass, list.size());
			rtn = list.toArray(rtn);
		}
		return rtn;
	}

}
