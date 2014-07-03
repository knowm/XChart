package org.leores.util.able;

import java.math.BigDecimal;
import java.util.List;

import org.leores.util.U;

public interface Processable1<R, A> {
	public R process(A a);

	public static class MaxListOutSize<A> implements Processable1<String, A> {
		protected Integer mSize;// null means do no limit list out size

		public MaxListOutSize(Integer mSize) {
			this.mSize = mSize;
		}

		public String process(A a) {
			String rtn = null;
			if (a != null && U.bAssignable(List.class, a.getClass())) {
				List lObj = (List) a;
				if (mSize != null && mSize >= 0 && mSize < lObj.size()) {
					List lOut = lObj.subList(0, mSize);
					rtn = lOut + "...";
				} else {
					rtn = lObj + "";
				}
			} else {
				rtn = a + "";
			}
			return rtn;
		}
	}

	public static class ArrayToString<A> implements Processable1<String, A> {
		public String process(A a) {
			String rtn = a + "";
			if (a != null && a instanceof Object[]) {
				rtn = "[";
				Object[] aa = (Object[]) a;
				for (int i = 0; i < aa.length; i++) {
					rtn += aa[i];
					if (i + 1 < aa.length) {
						rtn += ",";
					}
				}
				rtn += "]";
			}
			return rtn;
		}
	}

	public static class Expression<A> implements Processable1<A, A> {
		public String sElement, sExpression;

		public Expression(String sElement, String sExpression) {
			this.sElement = sElement;
			this.sExpression = sExpression;
		}

		public A process(A a) {
			A rtn = a;
			if (a != null) {
				String sExpToEval = sExpression.replaceAll(sElement, a + "");
				BigDecimal bdEvalRtn = U.eval1Expression(sExpToEval);
				if (bdEvalRtn != null) {
					rtn = (A) U.newInstance(a.getClass(), bdEvalRtn + "");
				} else {
					rtn = null;
				}
			}
			return rtn;
		}

	}
}
