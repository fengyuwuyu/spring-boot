package com.ll.spring.boot.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ll.spring.boot.core.consts.Consts;
import com.ll.spring.boot.core.model.EnumOperator;
import com.ll.spring.boot.core.util.entity.Model2SqlEntity;
import com.ll.spring.boot.core.wrapper.MybatisCustomWrapper;

public class CommonUtils {
	
	/**
	 * 
	 * @param str 要截断的字符串
	 * @param replace 截断时使用的截断符
	 * @param n 每n个字符截断一次
	 * @return
	 */
	public static String truncation(String str, String replace, int n) {
		if (StringUtil.isNullEmpty(str)) {
			return str;
		}
		
		int len = str.length();
		int index = 0;
		StringBuilder sb = new StringBuilder();
		replace = StringUtil.isNullEmpty(replace) ? "" : replace;
		while (index < len) {
			sb.append(str.substring(index, (index + n) > len ? len : index + n));
			if (index + n < len) {
				sb.append(replace);
			}
			index += n;
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String result = truncation("asdasdasda", "</br>", 3);
		System.out.println(result);
	}

	public static String trimZero(String s) {
	    if (s.indexOf(".") > 0) {
	        // 去掉多余的0
	        s = s.replaceAll("0+?$", "");
	        // 如最后一位是.则去掉
	        s = s.replaceAll("[.]$", "");
	    }
	    return s;
	}
	
	/**
	 * 以separate为分隔符，返回String类型，数组中的对象需要实现toString方法
	 * 
	 * @param array
	 *            数组对象
	 * @param separate
	 * @return
	 */
	public static String join(Object[] array, String separate) {
		if (array != null && array.length > 0) {
			if (separate == null) {
				separate = ",";
			}
			if (array.length == 1) {
				return array[0].toString();
			} else {
				StringBuilder result = new StringBuilder();
				for (Object o : array) {
					result.append(o.toString() + separate);
				}
				return result.substring(0, result.length() - 1);
			}
		}
		return null;
	}

	public static String getCss(Object o) {
		return "<div style='color:red;font-size:14px'>" + o + "</div>";
	}

	public static boolean match(String reg, String line){
		return Pattern.matches(reg, line);
	}
	


	private static Logger log = LoggerFactory.getLogger(CommonUtils.class);
	
	public static <T> String listToString(List<T> list) {
		if (list == null || list.size() == 0) {
			return "";
		}

		Iterator<T> it = list.iterator();
		if (!it.hasNext())
			return "";

		StringBuilder sb = new StringBuilder();
		for (;;) {
			T e = it.next();
			sb.append(e);
			if (it.hasNext())
				sb.append(',');
			else
				break;
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> stringToList(String str, String seperator, Class<T> clazz) {
		if (StringUtil.isNullEmpty(str) || clazz == null) {
			return null;
		}

		seperator = StringUtil.isNullEmpty(seperator) ? "," : seperator;

		String[] arr = str.split(seperator);
		List<T> list = new ArrayList<>(arr.length);
		for (String string : arr) {
			try {
				Method method = clazz.getDeclaredMethod("valueOf", String.class);
				T t = (T) method.invoke(null, string);
				list.add(t);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				log.warn("Method [valueOf] not find!");
			}
		}
		return list;
	}

	/**
	 * 只支持简单对象的检测，且对象内部没有基本类型
	 * 
	 * @param t
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> boolean checkObjHasValue(T t, String... excludeFields) {
		if (t == null) {
			return false;
		}

		List<String> excludeFieldList = Collections.emptyList();
		if (excludeFields != null && excludeFields.length > 0) {
			excludeFieldList = Arrays.asList(excludeFields);
		}

		Class<? extends Object> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length > 0) {
			for (Field field : fields) {
				if (excludeFieldList.contains(field.getName())) {
					continue;
				}
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(t);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

				if (value != null) {
					if (value instanceof String) {
						String tmp = (String) value;
						if (!StringUtil.isNullEmpty(tmp)) {
							return true;
						}
					} else if (value instanceof List) {
						List temp = (List) value;
						if (temp.size() > 0) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	public static <T> void handleQueryDateSection(String beginColumnName, String endColumnName, String beginDateStr,
			String endDateStr, Map<String, Object> params, String pattern) {
		pattern = pattern == null ? Consts.DATE_TIME_PATTERN : pattern;

		if (!StringUtil.isNullEmpty(beginDateStr)) {
			params.put(beginColumnName, DateUtil.parse(beginDateStr, pattern));
		}
		if (!StringUtil.isNullEmpty(endDateStr)) {
			params.put(endColumnName, DateUtil.parse(endDateStr, pattern));
		}
	}

	public static <T> void handleQueryDateSection(String columnName, String beginDateStr, String endDateStr,
			Wrapper<T> wrapper, String pattern) {
		pattern = pattern == null ? Consts.DATE_TIME_PATTERN : pattern;
		columnName = columnName == null ? Consts.DEFAULT_SORT_FIELD : columnName;
		Date beginDate = null;
		Date endDate = null;

		if (!StringUtil.isNullEmpty(beginDateStr)) {
			beginDate = DateUtil.parse(beginDateStr, pattern);
		}
		if (!StringUtil.isNullEmpty(endDateStr)) {
			endDate = DateUtil.parse(endDateStr, pattern);
		}

		handleQueryDateSection(columnName, beginDate, endDate, wrapper);
	}

	public static <T> void handleQueryDateSection(String columnName, Date beginDate, Date endDate, Wrapper<T> wrapper) {
		if (beginDate == null && endDate == null) {
			return;
		}

		if (beginDate != null && endDate != null) {
			CommonUtils.handleRequestParams(wrapper, columnName, Arrays.asList(beginDate, endDate),
					EnumOperator.BETWEEN);
			return;
		}

		if (beginDate != null) {
			CommonUtils.handleRequestParams(wrapper, columnName, beginDate, EnumOperator.GE);
			return;
		}

		CommonUtils.handleRequestParams(wrapper, columnName, endDate, EnumOperator.LE);
	}

	public static <T> Wrapper<T> handleRequestParams(Wrapper<T> wrapper, Object... objs) {
		return handleRequestParams(wrapper, createModel2SqlEntityMap(objs));
	}

	public static <T> void handleDateRequestParams(Wrapper<T> wrapper, String columnName, String date, String pattern,
			EnumOperator operator) {
		if (!StringUtil.isNullEmpty(date)) {
			pattern = StringUtil.isNullEmpty(pattern) ? Consts.DATE_PATTERN : pattern;
			operator = operator == null ? EnumOperator.GE : operator;
			Date d = DateUtil.parse(date, pattern);
			handleRequestParams(wrapper, createModel2SqlEntityMap(columnName, d, operator));
		}
	}

	public static <T> Wrapper<T> handleRequestParams(Wrapper<T> wrapper, Map<String, Model2SqlEntity> paramMap) {
		if (paramMap == null || paramMap.size() == 0) {
			return wrapper;
		}

		for (Map.Entry<String, Model2SqlEntity> entry : paramMap.entrySet()) {
			Model2SqlEntity entity = entry.getValue();
			String columnName = entity.getColumnName();
			List<Object> values = entity.getValues();
			EnumOperator operator = entity.getOperator();
			if (!checkValues(values)) {
				continue;
			}

			Object value = values.get(0);

			switch (operator) {
			case EQ:
				wrapper.and().eq(columnName, value);
				break;
			case LIKE:
				if (value instanceof String) {
					wrapper.and().like(columnName, (String) value);
				}
				break;
			case LT:
				wrapper.and().lt(columnName, value);
				break;
			case LE:
				wrapper.and().le(columnName, value);
				break;
			case GT:
				wrapper.and().gt(columnName, value);
				break;
			case GE:
				wrapper.and().ge(columnName, value);
				break;
			case IN:
				wrapper.and().in(columnName, values);
				break;
			case NE:
				wrapper.and().ne(columnName, value);
				break;
			case BETWEEN:
				if (values.size() > 1) {
					wrapper.and().between(columnName, values.get(0), values.get(1));
				}
				break;
			case BIT:
				((MybatisCustomWrapper<T>) wrapper.and()).bit(columnName, value);
				break;
			case XOR:
				((MybatisCustomWrapper<T>) wrapper.and()).unbit(columnName, value);
				break;

			default:
				break;
			}
		}
		return wrapper;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Model2SqlEntity> createModel2SqlEntityMap(Object... objs) {
		if (objs == null || objs.length == 0 || objs.length % 3 != 0) {
			log.trace("empty or illegal args, args = " + Arrays.asList(objs));
			return null;
		}
		Map<String, Model2SqlEntity> paramMap = new HashMap<>();

		for (int i = 0; i < objs.length; i += 3) {
			if (objs[i] instanceof String
					&& (objs[i + 2] == null || (objs[i + 2] != null && objs[i + 2] instanceof EnumOperator))) {
				EnumOperator operator = objs[i + 2] == null ? EnumOperator.EQ : (EnumOperator) objs[i + 2];
				if (objs[i + 1] instanceof List) {
					paramMap.put(((String) objs[i]) + "-" + RandomUtil.randomString(6),
							new Model2SqlEntity((String) objs[i], (List<Object>) objs[i + 1], operator));
				} else if (objs[i + 1] instanceof String) {
					String tmp = (String) objs[i + 1];
					if (!StringUtil.isNullEmpty(tmp)) {
						paramMap.put(((String) objs[i]) + "-" + RandomUtil.randomString(6),
								new Model2SqlEntity((String) objs[i], objs[i + 1], operator));
					}
				} else {
					paramMap.put(((String) objs[i]) + "-" + RandomUtil.randomString(6),
							new Model2SqlEntity((String) objs[i], objs[i + 1], operator));
				}
			} else {
				log.warn("illegal params, [ " + objs[i] + ", " + objs[i + 1] + ", " + objs[i + 2] + " ]");
			}
		}

		return paramMap;
	}

	private static boolean checkValues(List<Object> values) {
		if (values == null || values.size() == 0) {
			log.trace("null values");
			return false;
		}

		for (Object object : values) {
			if (object == null) {
				log.trace("null value");
				return false;
			}
			if (object instanceof String && StringUtil.isNullEmpty((String) object)) {
				log.trace("String value is null");
				return false;
			}
		}
		return true;
	}
}
