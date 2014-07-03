package org.leores.util.able;

import java.lang.reflect.Field;

import org.leores.util.U;

public interface Processable2<R, A, B> {
	public R process(A a, B b);

	public static class TrimStringField implements Processable2<Boolean, Object, Field> {

		public Boolean process(Object obj, Field field) {
			boolean rtn = false;
			if (field != null && U.bAssignable(String.class, field.getType())) {
				String sValue = (String) U.getFieldValue(obj, field);
				if (sValue != null) {
					String sValue2 = sValue.trim();
					if (sValue2.length() < sValue.length()) {
						rtn = true;
					}
					U.setFieldValue(obj, field, sValue2);
				}
			}
			return rtn;
		}

	}

	public static class SampleListByIndex<B> implements Processable2<Boolean, Integer, B> {
		public Integer step;

		/**
		 * The sample results will with these index: 0, 0+step, 0+2*step, ...
		 * 
		 * @param step
		 */
		public SampleListByIndex(Integer step) {
			this.step = step;
		}

		public Boolean process(Integer a, B b) {
			return a % step == 0;
		}

	}

}
