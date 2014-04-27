package org.leores.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.leores.ecpt.*;
import org.leores.util.able.Processable1;
import org.leores.util.able.Processable2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ObjUtil extends StrUtil {
	public static String sSet = "=";
	public static Modifier modifier;

	public static int modPub = modifier.PUBLIC;
	public static int modPro = modifier.PROTECTED;
	public static int modPri = modifier.PRIVATE;
	public static int modPubPro = modPub | modPro;
	public static int modPubProPri = modPub | modPro | modPri;
	public static int modAll = -1;//0 means no modifier the default

	/**
	 * Check whether class2 is suitable to fill in as a parameter of tClass
	 * 
	 * @param tClass
	 * @param class2
	 * @return
	 * @deprecated use bAssignable instead.
	 */
	public static boolean bMatch(Class tClass, Class class2) {
		boolean rtn = false;

		if (tClass != null && class2 != null) {
			if (tClass == class2) {
				rtn = true;
			} else {
				if (tClass.isInterface()) {
					Class[] c2Interfaces = class2.getInterfaces();
					if (c2Interfaces != null) {
						for (int i = 0; i < c2Interfaces.length; i++) {
							if (tClass == c2Interfaces[i]) {
								rtn = true;
								break;
							}
						}
					}
				}
			}
		}

		return rtn;
	}

	public static boolean bAssignable(Class cTo, Class cFrom) {
		boolean rtn = false;

		if (cFrom == null) {
			rtn = true;
		} else if (cTo != null) {
			rtn = cTo.isAssignableFrom(cFrom);
		}

		return rtn;
	}

	public static boolean bAssignable(Class[] cTos, Class[] cFroms) {
		boolean rtn = false;

		if (cTos != null && cFroms != null && cTos.length == cFroms.length) {
			rtn = true;
			for (int i = 0; i < cTos.length; i++) {
				if (!bAssignable(cTos[i], cFroms[i])) {
					rtn = false;
					break;
				}
			}
		}

		return rtn;
	}

	public static Class[] getClass(Object... objs) {
		Class[] rtn = null;

		if (objs != null) {
			rtn = new Class[objs.length];
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] != null) {
					rtn[i] = objs[i].getClass();
				} else {
					rtn[i] = null;
				}
			}
		}

		return rtn;
	}

	public static boolean bAssignable(Class[] cTos, Object... args) {
		boolean rtn = false;

		Class[] argTypes = getClass(args);
		rtn = bAssignable(cTos, argTypes);

		return rtn;
	}

	public static Method getMethod(Class tClass, String mName, Class... args) {
		Method rtn = null;

		if (tClass != null && mName != null && args != null) {
			Method[] methods = tClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				String mNamei = methods[i].getName();
				if (mName.equals(mNamei)) {
					Class[] argTypes = methods[i].getParameterTypes();
					if (bAssignable(argTypes, args)) {
						rtn = methods[i];
						break;
					}
				}
			}
		}

		return rtn;
	}

	public static Method getMethod(Class tClass, String mName, Object... args) {
		Method rtn = null;
		Class[] argTypes = getClass(args);
		rtn = getMethod(tClass, mName, argTypes);
		return rtn;
	}

	public static Method getGetterMethod(Class tClass, String sField) {
		String sGetterMethod = getterMethodName(sField);
		Method rtn = getMethod(tClass, sGetterMethod);
		return rtn;
	}

	protected static Method getGetterMethod(Field field) {
		Method rtn = null;
		String sField = field.getName();
		Class tClass = field.getDeclaringClass();
		rtn = getGetterMethod(tClass, sField);
		return rtn;
	}

	protected static Method getGetterMethod(Object obj, String sField) {
		Method rtn = null;
		Class tClass = obj.getClass();
		rtn = getGetterMethod(tClass, sField);
		return rtn;
	}

	public static Method getSetterMethod(Class tClass, String sField, Class... args) {
		String sSetterMethod = setterMethodName(sField);
		Method rtn = getMethod(tClass, sSetterMethod, args);
		return rtn;
	}

	public static Method getSetterMethod(Class tClass, String sField, Object... args) {
		Method rtn = null;
		Class[] argTypes = getClass(args);
		rtn = getSetterMethod(tClass, sField, argTypes);
		return rtn;
	}

	protected static Method getSetterMethod(Field field, Object... args) {
		Method rtn = null;
		String sField = field.getName();
		Class tClass = field.getDeclaringClass();
		rtn = getSetterMethod(tClass, sField, args);
		return rtn;
	}

	protected static Method getSetterMethod(Object obj, String sField, Object... args) {
		Method rtn = null;
		Class tClass = obj.getClass();
		rtn = getSetterMethod(tClass, sField, args);
		return rtn;
	}

	public static Object invokeMethod(boolean bSetAccess, Object obj, Method method, Object... args) {
		Object rtn = null;
		try {
			boolean bAccessible = method.isAccessible();
			if (bSetAccess) {
				method.setAccessible(true);
			}
			rtn = method.invoke(obj, args);
			if (bSetAccess) {
				method.setAccessible(bAccessible);
			}
		} catch (Exception e) {
			tLog(e);
		}
		return rtn;
	}

	public static Object invokeMethod(Object obj, Method method, Object... args) {
		return invokeMethod(true, obj, method, args);
	}

	/**
	 * 
	 * @param tClass
	 * @param obj
	 *            could be null when invoking static class method
	 * @param sMethod
	 * @param bException
	 *            whether send runtime exception when method is not found.
	 * @param args
	 * @return
	 */

	public static Object invokeMethodByName(Class tClass, Object obj, String sMethod, boolean bException, Object... args) {
		Object rtn = null;

		//null check has been done in getMethod.
		Method method = U.getMethod(tClass, sMethod, args);
		if (method != null) {
			rtn = invokeMethod(obj, method, args);
		} else if (bException) {
			throw new TRuntimeException("Can not get [method, args]:", sMethod, args);
		}

		return rtn;
	}

	/**
	 * 
	 * @param obj
	 * @param sMethod
	 * @param bException
	 *            whether send runtime exception when method is not found.
	 * @param args
	 * @return
	 */
	public static Object invokeObjMethodByName(Object obj, String sMethod, boolean bException, Object... args) {
		return invokeMethodByName(obj.getClass(), obj, sMethod, bException, args);
	}

	public static Constructor getConstructor(Class tClass, Class... args) {
		Constructor rtn = null;

		if (tClass != null && args != null) {
			Constructor[] constructors = tClass.getConstructors();
			if (constructors != null) {
				for (int i = 0; i < constructors.length; i++) {
					Class[] argTypes = constructors[i].getParameterTypes();
					if (bAssignable(argTypes, args)) {
						rtn = constructors[i];
						break;
					}
				}
			}
		}

		return rtn;
	}

	public static Constructor getConstructor(Class tClass, Object... args) {
		Constructor rtn = null;

		Class[] argTypes = getClass(args);
		rtn = getConstructor(tClass, argTypes);

		return rtn;
	}

	/**
	 * 
	 * @param tClass
	 * @param args
	 * @return a new instance of tClass if it has a constructor with <b>args</b>
	 *         otherwise <b>null</b>
	 */
	public static Object newInstance(Class tClass, Object... args) {
		Object rtn = null;

		Constructor tConstructor = getConstructor(tClass, args);

		if (tConstructor != null) {
			try {
				rtn = tConstructor.newInstance(args);
			} catch (Exception e) {
				rtn = null;
				tLog(e);
			}
		} else {
			// ttLog("Constructor not found for:" + tClass +
			// ObjUtil.toStr(args));
		}

		return rtn;
	}

	/**
	 * 
	 * @param className
	 * @param args
	 * @return a new instance of className if it has a constructor with
	 *         <b>args</b> otherwise <b>null</b>
	 */

	public static Object newInstance(String className, Object... args) {
		Object rtn = null;

		try {
			rtn = newInstance(Class.forName(className), args);
		} catch (ClassNotFoundException e) {
			tLog(e);
		}

		return rtn;
	}

	/**
	 * The use of <b>Arrays.copyOf</b> makes the program requires at least JDK
	 * 1.6. Reprogramme this function would downgrade the requirement to JDK 1.5
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] rtn = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, rtn, first.length, second.length);
		return rtn;
	}

	public static Field[] concat(Field[] first, Field[] second) {
		Field[] rtn = new Field[first.length + second.length];
		System.arraycopy(first, 0, rtn, 0, first.length);
		System.arraycopy(second, 0, rtn, first.length, second.length);
		return rtn;
	}

	public static boolean bInMod(Field field, Integer mod) {
		int modUsed = modPub; // the default mod is modPub.
		if (mod != null) {
			modUsed = mod;
		}
		boolean rtn;
		if (mod >= 0) {
			rtn = (field != null) && ((modUsed & field.getModifiers()) > 0);
		} else {//when use modAll = -1
			rtn = true;
		}

		return rtn;
	}

	public static boolean bModPub(Integer mod) {
		int modUsed = modPub; // the default mod is modPub.
		if (mod != null) {
			modUsed = mod;
		}
		return modUsed == modPub;
	}

	public static Field[] filterFields(Field[] fields, Integer mod) {
		Field[] rtn = null;

		if (fields != null) {
			rtn = new Field[0];
			List<Field> lMatchedFields = new ArrayList<Field>();
			for (int i = 0; i < fields.length; i++) {
				if (bInMod(fields[i], mod)) {
					lMatchedFields.add(fields[i]);
				}
			}
			rtn = lMatchedFields.toArray(rtn);
		}

		return rtn;
	}

	public static Field[] filterFields(Field[] fields, String[] sFields) {
		Field[] rtn = null;

		if (fields != null && sFields != null) {
			rtn = new Field[0];
			List<Field> lMatchedFields = new ArrayList<Field>();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i] != null) {
					String sFieldi = fields[i].getName();
					for (int j = 0; j < sFields.length; j++) {
						if (sFieldi.equals(sFields[j])) {
							lMatchedFields.add(fields[i]);
							break;
						}
					}
				}
			}
			rtn = lMatchedFields.toArray(rtn);
		}

		return rtn;
	}

	/**
	 * Get the first appearance of the field with the name of <b>sField</b>,
	 * searching from the current Class to its parent Classes.
	 * 
	 * @param tClass
	 * @param sField
	 * @param mod
	 * @return
	 */

	public static Field getField(Class tClass, String sField, Integer mod) {
		Field rtn = null;

		if (sField != null) {
			Field[] fields = getFields(tClass, mod);
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					if (sField.equals(fields[i].getName())) {
						rtn = fields[i];
						break;
					}
				}
			}
		}

		return rtn;
	}

	public static Field[] getFields(Class tClass, Integer mod) {
		Field[] rtn = null;

		if (tClass != null) {
			if (bModPub(mod)) {//if only want public fields. We use this function rather than mod == modPub directly because the mod can be null which represent the default value of mod (modPub). 
				rtn = tClass.getFields();
			} else {
				//getDecalredFields does not return inherited fields.
				rtn = tClass.getDeclaredFields();
				rtn = filterFields(rtn, mod);
				Class tSuperClass = tClass.getSuperclass();
				if (!tSuperClass.equals(Object.class) && tSuperClass != null) {
					Field[] superFields = getFields(tSuperClass, mod);
					rtn = concat(rtn, superFields);
				}
			}
		}

		return rtn;
	}

	/**
	 * This function gives no warnings if any of the <b>sFields</b> does not
	 * exist. It returns all the fields that exist.
	 * 
	 * @param tClass
	 * @param sFields
	 * @param mod
	 * @return
	 */

	public static Field[] getFields(Class tClass, String[] sFields, Integer mod) {
		Field[] rtn = null;

		if (tClass != null) {
			rtn = getFields(tClass, mod);
			if (sFields != null) {
				rtn = filterFields(rtn, sFields);
			}
		}

		return rtn;
	}

	/**
	 * Get the value of a field. This function firstly attempt to use the getter
	 * method of the field if it exists. If not, it then tries to use reflection
	 * to get the value of the field.
	 * 
	 * @param obj
	 *            can be null when accessing static field.
	 * @param field
	 *            can not be null.
	 * @param bSetAccess
	 *            whether to try to access all field regardless whether it is
	 *            not accessible by normal means. If this is true, the function
	 *            can access private and protected fields.
	 * @return
	 */

	public static Object getFieldValue(Object obj, Field field, boolean bSetAccess, boolean bUseGetter) {
		Object rtn = null;

		if (field != null) {
			boolean getterFound = false;
			if (bUseGetter) {
				//try to find corresponding getter method first.				
				Method mGetter = getGetterMethod(field);
				if (mGetter != null) {
					getterFound = true;
					rtn = invokeMethod(obj, mGetter);
				}
			}
			if (!getterFound) {
				boolean bAccessible = field.isAccessible();
				if (bSetAccess) {
					field.setAccessible(true);
				}
				try {
					rtn = field.get(obj);
				} catch (Exception e) {
					tLog(e);
				}
				if (bSetAccess) {
					field.setAccessible(bAccessible);
				}
			}
		}

		return rtn;
	}

	/**
	 * Get the value of a field. This function firstly attempt to use the getter
	 * method of the field if it exists. If not, it then tries to use reflection
	 * to get the value of the field. By default this function will try to
	 * access inaccessible fields such as private and protected fields.
	 * 
	 * @param obj
	 *            can be null when accessing static field.
	 * @param field
	 *            can not be null.
	 * @return
	 */

	public static Object getFieldValue(Object obj, Field field) {
		return getFieldValue(obj, field, true, true);
	}

	public static Object getFieldValue(Object obj, String sField, Integer mod, boolean bException) {
		Object rtn = null;
		boolean found = false;

		if (obj != null) {
			Method mGetter = getGetterMethod(obj, sField);
			if (mGetter != null) {
				found = true;
				rtn = invokeMethod(obj, mGetter);
			} else {
				Field field = getField(obj.getClass(), sField, mod);
				if (field != null) {
					found = true;
					rtn = getFieldValue(obj, field, true, false);
				}
			}
		}

		if (!found && bException) {
			throw new TRuntimeException("can not get field [obj, sField, mod]:", obj, sField, mod);
		}

		return rtn;
	}

	public static Object getFieldValue(Object obj, String sField, Integer mod) {
		return getFieldValue(obj, sField, mod, true);
	}

	public static Object getFieldValue(Object obj, String sField) {
		return getFieldValue(obj, sField, null, true);
	}

	public static Object[] getFieldValues(Object obj, String[] sFields, Integer mod) {
		Object[] rtn = null;

		if (obj != null && sFields != null) {
			rtn = new Object[sFields.length];
			for (int i = 0; i < sFields.length; i++) {
				rtn[i] = getFieldValue(obj, sFields[i], mod);
			}
		}

		return rtn;
	}

	public static Object[] getFieldValues(Object obj, String[] sFields) {
		return getFieldValues(obj, sFields, null);
	}

	/**
	 * Set the value of a field. This function firstly attempt to use the setter
	 * method of the field if it exists. If not, it then tries to use reflection
	 * to set the value of the field.
	 * 
	 * @param obj
	 *            can be null when accessing static field.
	 * @param field
	 *            can not be null.
	 * @param bSetAccess
	 *            whether to try to access all field regardless whether it is
	 *            not accessible by normal means. If this is true, the function
	 *            can access private and protected fields.
	 * @return
	 */

	public static boolean setFieldValue(Object obj, Field field, Object value, boolean bSetAccess, boolean bUseSetter) {
		boolean rtn = false;
		if (field != null) {
			boolean setterFound = false;
			if (bUseSetter) {
				//try to find corresponding getter method first.
				Method mSetter = getSetterMethod(field, value);
				if (mSetter != null) {
					setterFound = true;
					rtn = true;
					Object mRtn = invokeMethod(obj, mSetter, value);
					if (mRtn != null) {
						rtn = (Boolean) mRtn;
					}
				}
			}
			if (!setterFound) {
				boolean bAccessible = field.isAccessible();
				if (bSetAccess) {
					field.setAccessible(true);
				}
				try {
					rtn = true;
					field.set(obj, value);
				} catch (Exception e) {
					rtn = false;
					tLog(e);
				}
				if (bSetAccess) {
					field.setAccessible(bAccessible);
				}
			}
		}
		return rtn;
	}

	/**
	 * Set the value of a field. This function firstly attempt to use the setter
	 * method of the field if it exists. If not, it then tries to use reflection
	 * to set the value of the field. By default this function will try to
	 * access inaccessible fields such as private and protected fields.
	 * 
	 * @param obj
	 *            can be null when accessing static field.
	 * @param field
	 *            can not be null.
	 * 
	 * @return
	 */

	public static boolean setFieldValue(Object obj, Field field, Object value) {
		return setFieldValue(obj, field, value, true, true);
	}

	/**
	 * 
	 * @param str
	 *            % represents the object itself.
	 * @param obj
	 * @param flags
	 * @return
	 */
	public static String evalVariable(String str, Object obj, int flags) {
		String rtn = str;

		if (hasVariable(str)) {
			rtn = "";
			Pattern pat = Pattern.compile(sVarPat);
			Matcher matcher = pat.matcher(str);
			int iLastTo = 0;
			while (matcher.find()) {
				int iFrom = matcher.start(), iTo = matcher.end();
				rtn += str.substring(iLastTo, iFrom);
				String sVar = matcher.group(1);
				Object oVal = null;
				boolean bInvalid = false;

				if (sVar.equals("%")) {
					oVal = obj;
				} else if (obj != null) {
					try {
						List<String> lMethod = parseMethodAsStrList(sVar);
						if (lMethod != null && lMethod.size() > 0) {
							String sMethod = lMethod.remove(0);
							if (lMethod.size() > 0) {
								Object[] oaPars = lMethod.toArray();
								oVal = U.invokeObjMethodByName(obj, sMethod, true, oaPars);
							} else {
								oVal = U.invokeObjMethodByName(obj, sMethod, true);
							}

						} else {
							oVal = getFieldValue(obj, sVar, modAll);
						}
					} catch (TRuntimeException e) {
						bInvalid = true;
						if ((flags & EVAL_InvalidException) > 0) {
							throw e;
						}
					}
				}

				if (!bInvalid) {
					if (oVal != null) {
						rtn += oVal;
					} else {
						if ((flags & EVAL_NullException) > 0) {
							throw new TRuntimeException("evalVariable: null value of ", sVar);
						}
						if ((flags & EVAL_NullIgnore) > 0) {
							rtn += str.substring(iFrom, iTo);
						} else {
							rtn += null;
						}
					}
				} else if ((flags & EVAL_InvalidIgnore) > 0) {
					rtn += str.substring(iFrom, iTo);
				}

				iLastTo = iTo;
			}
			rtn += str.substring(iLastTo);
		}

		return rtn;
	}

	/**
	 * 
	 * @param str
	 *            $variable$ #expression#, e.g. #$var1$+$var2$#. Evaluation
	 *            sections with any whitespace characters (\t\n\x0b\r\f) will be
	 *            ignored. $%$ represents the object itself.
	 * @param obj
	 *            variable values will be obtained from obj.
	 * @param flags
	 * <br>
	 *            <b>EVAL_InvalidException:</b> throw TRuntimeException when
	 *            found invalid evaluations. <br>
	 *            <b>EVAL_InvalidIgnore:</b> ignore those invalid evaluation
	 *            sections including invalid variables (fields/methods) or
	 *            expressions (the original content of those sections will be
	 *            copied to the result). <b>Note</b>: valid variables with null
	 *            values are NOT ignored! <br>
	 *            <b>EVAL_NullException:</b> throw TRuntimeException when a
	 *            evaluation result (field/method) in $xxxx$ is null.<br>
	 *            <b>EVAL_NullIgnore:</b> ignore those $xxx$ evaluation sections
	 *            with null results (the original content of those sections will
	 *            be copied to the result).
	 * 
	 * @return
	 */
	public static String eval(String str, Object obj, int flags) {
		String rtn = evalVariable(str, obj, flags);
		rtn = evalExpression(rtn, flags); //bIgnoreInvalid and bIgnoreNull are of the same meaning an evaluation of expressions.
		return rtn;
	}

	public static String evalIterative(String str, Object obj) {
		String rtn = str;

		while (hasEvaluation(rtn)) {
			rtn = eval(rtn, obj, EVAL_InvalidException);
		}

		return rtn;
	}

	public static String eval(String str, Object obj) {
		return eval(str, obj, EVAL_InvalidException);
	}

	public static boolean bNoEval(String str) {
		boolean rtn = false;

		if (str != null && str.length() >= sNoEval.length()) {
			String sBeginning = str.substring(0, sNoEval.length());
			rtn = sNoEval.equals(sBeginning);
		}

		return rtn;
	}

	/**
	 * 
	 * @param obj
	 * @param sField
	 * @param value
	 * @param bNewInstance
	 *            true: when the value is not directly assignable to the field,
	 *            generate a new instance using the field's class and the value
	 *            as the construct parameter.
	 * @param mod
	 * @return
	 */
	public static boolean setFieldValue(Object obj, String sField, Object value, Integer mod, boolean bNewInstance, boolean bException) {
		boolean rtn = false;

		if (obj != null && sField != null) {
			//try to find corresponding getter method first.
			if (value != null && value instanceof String) {
				String sValue = (String) value;
				if (bNoEval(sValue)) {
					value = sValue.substring(sNoEval.length());
				} else {
					value = eval(sValue, obj);
				}
			}
			Method mSetter = getSetterMethod(obj, sField, value);
			if (mSetter != null) {
				rtn = true;
				Object mRtn = invokeMethod(obj, mSetter, value);
				if (mRtn != null) {
					rtn = (Boolean) mRtn;
				}
			} else {
				Field field = getField(obj.getClass(), sField, mod);
				if (field != null) {
					rtn = true;
					Object oValue = value;
					if (bNewInstance && value != null) {
						Class fieldType = field.getType();
						if (!bAssignable(fieldType, value.getClass())) {
							oValue = newInstance(fieldType, value);
							if (oValue == null) {
								rtn = false;
							}
						}
					}
					if (rtn) {
						rtn = setFieldValue(obj, field, oValue, true, false);
					}
				}
			}

		}

		if (!rtn && bException) {
			throw new TRuntimeException("Can not set field value [obj, sField, value, bNewInstance, mod]:", obj, sField, value, bNewInstance, mod);
		}

		return rtn;
	}

	public static boolean setFieldValue(Object obj, String sField, Object value, Integer mod) {
		return setFieldValue(obj, sField, value, mod, false, true);
	}

	public static boolean setFieldValue(Object obj, String sField, Object value) {
		return setFieldValue(obj, sField, value, null, false, true);
	}

	/**
	 * This function provide a shallow copy of <b>oFrom</b>'s variables values
	 * to <b>oTo</b>'s variables. <br>
	 * <br>
	 * In order to copy a clone of a object rather than its reference, the
	 * object's attributes' class has to implement a constructor that take in an
	 * object of itself as the only parameter or has a clone method. <br>
	 * <br>
	 * The ArrayList and LinkedList has clone methods. So that they could be
	 * copied (cloned) using this function. Arrays can also be copied (cloned)
	 * by this function.
	 * 
	 * @param oTo
	 * @param oFrom
	 * @return
	 */
	public static boolean copy(Object oTo, Object oFrom, String[] sFields, Integer modTo, Integer modFrom) {
		boolean rtn = false;

		if (oTo != null && oFrom != null) {
			rtn = true;
			Class cTo = oTo.getClass();
			Class cFrom = oFrom.getClass();
			if (cTo != cFrom) {
				tLog("Warning: Copying from a different class!", cTo, cFrom);
			}
			try {
				Field[] fTos = getFields(cTo, sFields, modTo);
				for (int i = 0; i < fTos.length; i++) {
					int fToModifiers = fTos[i].getModifiers();
					if (!modifier.isFinal(fToModifiers)) {
						String fToName = fTos[i].getName();
						Field fFrom = getField(cFrom, fToName, modFrom);
						if (fFrom != null) {
							Object value = getFieldValue(oFrom, fFrom);
							Object vCopy = null;
							if (value != null) {
								Class vClass = value.getClass();
								if (bAssignable(Object[].class, vClass)) {
									Object[] oaValue = (Object[]) value;
									vCopy = oaValue.clone();
								} else {
									Method mClone = getMethod(vClass, "clone");
									if (mClone != null) {
										vCopy = invokeMethod(value, mClone);
									} else {
										vCopy = newInstance(value.getClass(), value);
									}
								}
							}
							boolean mRtn;
							if (vCopy != null) {
								mRtn = setFieldValue(oTo, fTos[i], vCopy);
							} else {
								mRtn = setFieldValue(oTo, fTos[i], value);
							}
							rtn = rtn && mRtn;
						}
					}
				}
			} catch (Exception e) {
				rtn = false;
				tLog(e);
			}
		}

		return rtn;
	}

	public static boolean copy(Object oTo, Object oFrom, String[] sFields) {
		return copy(oTo, oFrom, sFields, modAll, modAll);
	}

	public static boolean copy(Object oTo, Object oFrom, String sField) {
		String[] sFields = { sField };
		return copy(oTo, oFrom, sFields, modAll, modAll);
	}

	public static boolean copy(Object oTo, Object oFrom) {
		return copy(oTo, oFrom, null, modAll, modAll);
	}

	/**
	 * 
	 * @param tObj
	 * @param numOutPattern
	 * @return null if the tObj is not a number (or null) or numOutPattern is
	 *         null
	 */
	public static String numToStr(Object tObj, String numOutPattern) {
		String rtn = null;

		if (tObj != null && numOutPattern != null && tObj instanceof Number) {
			DecimalFormat df = new DecimalFormat(numOutPattern);
			rtn = df.format(tObj);
		}

		return rtn;
	}

	public static String[] toStrArray(Class tClass, Integer mod) {
		String[] rtn = null;

		if (tClass != null) {
			Field[] fields = getFields(tClass, mod);
			rtn = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				rtn[i] = fields[i].getName();
			}
		}

		return rtn;
	}

	public static String[] toStrArray(Class tClass) {
		return toStrArray(tClass, modPub);
	}

	public static String[][] toStrArray(Object tObj, String[] sFields, String numOutPattern, Integer mod, Processable1<String, Object> pa1) {
		String[][] rtn = null;

		if (tObj != null) {
			if (sFields == null) {
				Field[] fields = getFields(tObj.getClass(), mod);
				rtn = new String[2][fields.length];
				for (int i = 0; i < fields.length; i++) {
					rtn[0][i] = fields[i].getName();
					Object oValue = getFieldValue(tObj, fields[i]);
					rtn[1][i] = numToStr(oValue, numOutPattern);
					if (rtn[1][i] == null) {
						if (pa1 != null) {
							rtn[1][i] = pa1.process(oValue);
						} else {
							rtn[1][i] = oValue + ""; // Avoid the null exception 
						}
					}
				}
			} else {
				rtn = new String[2][sFields.length];
				for (int i = 0; i < sFields.length; i++) {
					rtn[0][i] = sFields[i];
					Object oValue = getFieldValue(tObj, sFields[i], mod);
					rtn[1][i] = numToStr(oValue, numOutPattern);
					if (rtn[1][i] == null) {
						if (pa1 != null) {
							rtn[1][i] = pa1.process(oValue);
						} else {
							rtn[1][i] = oValue + ""; // Avoid the null exception
						}

					}
				}
			}
		}

		return rtn;
	}

	public static String[][] toStrArray(Object tObj, String[] sFields, String numOutPattern, Processable1<String, Object> pa1) {
		return toStrArray(tObj, sFields, numOutPattern, null, pa1);
	}

	public static String[][] toStrArray(Object tObj, String numOutPattern, Processable1<String, Object> pa1) {
		return toStrArray(tObj, null, numOutPattern, null, pa1);
	}

	public static String[][] toStrArray(Object tObj, String numOutPattern) {
		return toStrArray(tObj, null, numOutPattern, null, null);
	}

	public static String[][] toStrArray(Object tObj) {
		return toStrArray(tObj, null, null, null, null);
	}

	public static String[] toStrArray(List list, String numOutPattern) {
		String[] rtn = null;

		if (list != null) {
			int size = list.size();
			rtn = new String[size];
			for (int i = 0; i < size; i++) {
				Object eObj = list.get(i);
				rtn[i] = numToStr(eObj, numOutPattern);
				if (rtn[i] == null) {
					rtn[i] = eObj + "";
				}
			}
		}

		return rtn;
	}

	public static String[] toStrArray(List list) {
		return toStrArray(list, null);
	}

	public static String toStr(Object tObj, String[] sFields, String numOutPattern, Integer mod, Processable1<String, Object> pa1) {
		String rtn = null;

		rtn = tObj + " ";
		if (tObj != null && tObj.getClass() != String.class && !(tObj instanceof Number)) {
			String[][] strArray = toStrArray(tObj, sFields, numOutPattern, mod, pa1);
			if (strArray != null) {
				for (int i = 0; i < strArray[0].length; i++) {
					rtn += strArray[0][i] + "=" + strArray[1][i] + sEnumerate;
				}
			}
		}

		return rtn;
	}

	public static String toStr(Object tObj, String numOutPattern, Integer mod) {
		return toStr(tObj, null, numOutPattern, mod, null);
	}

	public static String toStr(Object tObj, Integer mod) {
		return toStr(tObj, null, null, mod, null);
	}

	public static String toStr(Object tObj) {
		return toStr(tObj, null, null, null, null);
	}

	public static String toStr(Object... objs) {
		String rtn = null;

		if (objs != null) {
			rtn = "[";
			for (int i = 0; i < objs.length; i++) {
				rtn += objs[i];
				if (i < objs.length - 1) {
					rtn += de;
				}
			}
			rtn += "]";

		} else {
			rtn = objs + "";
		}

		return rtn;
	}

	public static int processFields(Object tObj, Integer mod, Processable2<Boolean, Object, Field> pa2) {
		int rtn = 0;
		if (tObj != null) {
			Field[] fields = getFields(tObj.getClass(), mod);
			for (int i = 0; i < fields.length; i++) {
				if (pa2.process(tObj, fields[i])) {
					rtn++;
				}
			}
		}
		return rtn;
	}

	public static boolean clear(Object tObj, Integer mod) {
		boolean rtn = false;
		if (tObj != null) {
			rtn = true;
			Field[] fields = getFields(tObj.getClass(), mod);
			for (int i = 0; i < fields.length; i++) {
				if (!setFieldValue(tObj, fields[i], null)) {
					rtn = false;
				}
			}
		}
		return rtn;
	}

	public static boolean clear(Object tObj) {
		return clear(tObj, null);
	}

	public static boolean hasNoNull(Object tObj, Integer mod) {
		boolean rtn = true;

		if (tObj != null) {
			Field[] fields = getFields(tObj.getClass(), mod);
			for (int i = 0; i < fields.length; i++) {
				Object oValue = getFieldValue(tObj, fields[i]);
				if (oValue == null) {
					rtn = false;
					break;
				}
			}
		} else {
			rtn = false;
		}

		return rtn;
	}

	public static boolean hasNoNull(Object tObj) {
		return hasNoNull(tObj, null);
	}

	protected static String getSValue(Element e) {
		String rtn = null;
		if (e.hasChildNodes()) {
			rtn = e.getTextContent();
		} else {
			rtn = e.getAttribute("val");
		}
		return rtn;
	}

	protected static String getSValueIfReachEnd(Element e) {
		String rtn = null;
		if (e.hasChildNodes()) {
			int neChildNodes = e.getChildNodes().getLength();
			Node nodeFirstChild = e.getFirstChild();
			short nodeFCType = nodeFirstChild.getNodeType();
			if (neChildNodes == 1 && nodeFCType != Node.ELEMENT_NODE) {//Node.TEXT_NODE || Node.CDATA_SECTION_NODE
				rtn = nodeFirstChild.getNodeValue();
			}
		} else {
			rtn = e.getAttribute("val");
		}
		return rtn;
	}

	public static Type[] getActualTypeArguments(Field field) {
		Type[] rtn = null;
		if (field != null) {
			Type gType = field.getGenericType();
			if (gType instanceof ParameterizedType) {
				ParameterizedType pType = (ParameterizedType) gType;
				rtn = pType.getActualTypeArguments();
			}
		}
		return rtn;
	}

	public static Class getComponentType(Field field) {
		Class rtn = null;
		Class fieldType = field.getType();
		if (bAssignable(List.class, fieldType)) {
			Type[] aTypes = getActualTypeArguments(field);
			if (aTypes != null) {
				rtn = (Class) aTypes[0];
			}
		} else if (bAssignable(Object[].class, fieldType)) {
			rtn = fieldType.getComponentType();
		}
		return rtn;
	}

	public static boolean bLoadString(String str) {
		boolean rtn = false;
		if (str != null && str.indexOf(sSet) >= 0) {
			rtn = true;
		}
		return rtn;
	}

	/**
	 * Load fields' value of tObj from str. This function does not support
	 * Primitive Data Types. They has to be replaced by their corresponding
	 * Object classes. e.g. double->Double
	 * 
	 * @param tObj
	 * @param str
	 *            format: var1=value1;var2=value2;...
	 * @return
	 */

	public static boolean loadFromString(Object tObj, String str, boolean bExeception) {
		boolean rtn = false;

		if (tObj != null && U.bLoadString(str)) {
			invokeObjMethodByName(tObj, "beforeLoadObj", false, "ObjUtil.loadFromString");
			rtn = true;
			String str_ = str.trim();
			String[] tokens = split(str);

			for (int i = 0; i < tokens.length; i++) {
				String token = tokens[i].trim();
				String[] setting = token.split(sSet, 2);
				if (setting.length == 2) {
					Object oVal = setting[1];
					if ("!NULL!".equals(oVal)) {
						oVal = null;
					}
					rtn = setFieldValue(tObj, setting[0], oVal, modAll, true, bExeception);
				} else {
					rtn = false;
					if (bExeception) {
						throw new TRuntimeException("Wrong format: " + tokens[i] + " should be: var=value" + sEnumerate);
					}
				}
			}
			invokeObjMethodByName(tObj, "afterLoadObj", false, "ObjUtil.loadFromString");
		}

		return rtn;
	}

	public static boolean loadFromString(Object tObj, String str) {
		return loadFromString(tObj, str, true);
	}

	/**
	 * o1 and o2 should not have any loop references!! Otherwise the compare
	 * would not stop. o1 and o2 could be null. null is seemed as the smallest
	 * value. For List and Array, firstly compare size and then each element if
	 * the sizes of o1 and o2 are the same. By setting bHash = false, the
	 * function returns null when o1 and o2 are both not null and there is at
	 * least an Non-NULL elements in o1 or o2 that is not comparable to its
	 * counterpart. By setting bHash = true, the hashcode of o1 and o2 will be
	 * compared in the previous case.
	 * 
	 * @param o1
	 * @param o2
	 * @param bHash
	 *            whether to use Object.hashCode() to compare o1 and o2 when
	 *            they are not able to be compared (will return null if bHash =
	 *            false in this case)
	 * @return 1 if o1 > o2, -1 if o1 < o2, 0 if o1 = o2, null if unable to
	 *         compare.
	 */

	public static Integer compare(Object o1, Object o2, boolean bHash) {
		Integer rtn = null;

		if (o1 != null && o2 != null) {
			Class cO1 = o1.getClass();
			Class cO2 = o2.getClass();
			if (bAssignable(cO1, cO2) && bAssignable(Comparable.class, cO1)) {
				Comparable c1 = (Comparable) o1;
				Comparable c2 = (Comparable) o2;
				rtn = c1.compareTo(c2);
			} else if (bAssignable(List.class, cO1) && bAssignable(List.class, cO2)) {
				List l1 = (List) o1;
				List l2 = (List) o2;
				int size1 = l1.size();
				int size2 = l2.size();
				if (size1 == size2) {
					rtn = 0;
					//this loop will stop when rtn !=0, including the situation when rtn == null
					for (int i = 0; i < size1 && rtn == 0; i++) {
						Object eO1 = l1.get(i);
						Object eO2 = l2.get(i);
						rtn = compare(eO1, eO2, bHash);
					}
				} else if (size1 > size2) {
					rtn = 1;
				} else {
					rtn = -1;
				}
			} else if (bAssignable(Object[].class, cO1) && bAssignable(Object[].class, cO2)) {
				Object[] oa1 = (Object[]) o1;
				Object[] oa2 = (Object[]) o2;
				if (oa1.length == oa2.length) {
					rtn = 0;
					//this loop will stop when rtn !=0, including the situation when rtn == null
					for (int i = 0; i < oa1.length && rtn == 0; i++) {
						rtn = compare(oa1[i], oa2[i], bHash);
					}
				} else if (oa1.length > oa2.length) {
					rtn = 1;
				} else {
					rtn = -1;
				}
			} else if (bHash) {
				Integer iO1 = o1.hashCode();
				Integer iO2 = o2.hashCode();
				rtn = iO1.compareTo(iO2);
			}
		} else if (o1 == null && o2 == null) {
			rtn = 0;
		} else if (o1 != null) {
			rtn = 1;
		} else {
			rtn = -1;
		}

		return rtn;
	}

	/**
	 * o1 and o2 should not have any loop references!! Otherwise the compare
	 * would not stop. o1 and o2 could be null. null is seemed as the smallest
	 * value. For List and Array, firstly compare size and then each element if
	 * the sizes of o1 and o2 are the same. The function compares the hashcode
	 * of o1 and o2 when they are both not null and there is at least a Non-NULL
	 * elements in o1 or o2 that is not comparable to its counterpart.
	 * 
	 * @param o1
	 * @param o2
	 * @return 1 if o1 > o2, -1 if o1 < o2, 0 if o1 = o2.
	 */

	public static int compare(Object o1, Object o2) {
		return compare(o1, o2, true);
	}

	public static boolean bValidNumber(Object o1) {
		boolean rtn = false;
		if (o1 != null && o1 instanceof Number) {
			Number n1 = (Number) o1;
			double d1 = n1.doubleValue();
			rtn = !(Double.isInfinite(d1) || Double.isNaN(d1));
		}
		return rtn;
	}

}
