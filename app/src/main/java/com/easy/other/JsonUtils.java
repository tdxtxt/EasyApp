package com.easy.other;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Json处理工具</b> <br>
 * Create by ChenXinXu on 2016/1/21.
 * <p>
 * 问题联系邮箱: <var>554215282@qq.com</var>
 * </p>
 */
public class JsonUtils {

	/** 获取Field[] */
	public static Field[] getiField(Class<?> c) {
		Field[] f = null;
		try {
			f = c.getFields();
		} catch (SecurityException e) {
			Log.e("result", "getiField 失败 !");
		}
		return f;
	}

	/**
	 * 
	 * @param array
	 *            json数组
	 * @param c
	 *            Class
	 * @return List<Class对象> 错误 return null
	 */
	public static List<?> getJsonArray(JSONArray array, Class<?> c) {
		List<Object> datas = new ArrayList<Object>();
		if (array == null || c == null || isBaseType(c) 
				||c.isArray() || c.isEnum() || c.isInterface())
			return datas;
		Field[] fs = getiField(c);
		if (null == fs)
			return datas;
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.optJSONObject(i);
			Object ob = null;
			try {
				ob = c.newInstance();
				ob = getJsonObject(object, ob, fs, c.getName());
				if (null != ob)
					datas.add(ob);
			} catch (Exception e) {
				Log.e("result", "getJsonArray " + "实列化Object错误 !");
			}
		}
		return datas;
	}

	/**
	 * 
	 * @param arr
	 *            json数组
	 * @param c
	 *            Class
	 * @return List<Class对象> 错误 return null
	 */
	public static List<?> getJsonArray(String arr, Class<?> c) {
		List<Object> datas = new ArrayList<Object>();
		if (TextUtils.isEmpty(arr)|| c == null || isBaseType(c)
				||c.isArray() || c.isEnum() || c.isInterface())
			return datas;
		JSONArray array = getJSONArray(arr);
		if(null == array)
			return datas;
		Field[] fs = getiField(c);
		if (null == fs)
			return datas;
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.optJSONObject(i);
			Object ob = null;
			try {
				ob = c.newInstance();
			} catch (Exception e) {
				Log.e("result", "getJsonArray " + "实列化Object错误 !");
			}
			ob = getJsonObject(object, ob, fs, c.getName());
			if (null != ob)
				datas.add(ob);
		}
		return datas;
	}

	private static Object getJsonObject(JSONObject object, Object ob, Field[] fs, String className) {
		if (null == ob || null == object)
			return null;
		int len = fs.length;
		for (int k = 0; k < len; k++) {
			Field f = fs[k];
			String name = f.getName();
			Object str = object.opt(name);
			if (null == str || f.getModifiers() == (Modifier.FINAL | Modifier.STATIC))
				continue;
			Class<?> type = f.getType();
			try {
				if (type.isArray() || type.isInterface() || type.isEnum()
						|| type.isAnnotation() || type.isAnonymousClass())
					continue;
				if (type.equals(String.class)) {
					f.set(ob, str + "");
				} else if (type.equals(int.class) || type.equals(Integer.class)) {
					f.setInt(ob, Integer.parseInt(str + ""));
				} else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
					f.setBoolean(ob, Boolean.parseBoolean(str + ""));
				} else if (type.equals(double.class) || type.equals(Double.class)) {
					f.setDouble(ob, Double.parseDouble(str + ""));
				} else if (type.equals(float.class) || type.equals(Float.class)) {
					f.setFloat(ob, Float.parseFloat(str + ""));
				} else if (type.equals(long.class) || type.equals(Long.class)) {
					f.setLong(ob, Long.parseLong(str + ""));
				} else if (type.equals(short.class) || type.equals(Short.class)) {
					f.setShort(ob, Short.parseShort(str + ""));
				} else if (type.equals(byte.class) || type.equals(Byte.class)) {
					f.setByte(ob, Byte.parseByte(str + ""));
				} else if(type.equals(char.class)){
					String char_value = str+"";
					if(char_value.length() > 0)
						f.setChar(ob, char_value.charAt(0));
				} else if (type.equals(List.class) || type.equals(ArrayList.class)) {
					if (str.getClass().equals(JSONArray.class)) {
						Object child = getJsonArray((JSONArray) str, type);
						if (child != null)
							f.set(ob, child);
					}
				} else if (type.equals(Map.class)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> child = (Map<String, Object>) type.newInstance();
					child.put(name, str);
					f.set(ob, child);
				} else {
					if (str.getClass().equals(JSONObject.class)) {
						Object child = getJsonObject((JSONObject) str, type);
						if (child != null)
							f.set(ob, child);
					}
				}
			} catch (Exception e1) {
				Log.e("result", className + " " + type.getSimpleName() + " "
						+ name + " 类型转换失败 !");
			}

		}
		return ob;
	}

	/**
	 * 
	 * @param obj
	 *            son字符串
	 * @param c
	 *            Class
	 * @return Class对象 错误 return null
	 */
	public static Object getJsonObject(String obj, Class<?> c) {
		JSONObject object = getJSONObject(obj);
		if (obj == null || c == null || isBaseType(c) 
				||c.isArray() || c.isEnum() || c.isInterface())
			return null;
		Object ob = null;
		try {
			ob = c.newInstance();
		} catch (Exception e) {
			Log.e("result", "getJsonArray " + "实列化Object错误 !");
			return ob;
		}
		Field[] fs = getiField(c);
		if (null == fs)
			return ob;
		return getJsonObject(object, ob, fs, c.getName());
	}

	/**
	 * 
	 * @param object
	 *            son字符串
	 * @param c
	 *            Class
	 * @return Class对象 错误 return null
	 */
	public static Object getJsonObject(JSONObject object, Class<?> c) {
		if (object == null || null == c || isBaseType(c) 
				||c.isArray() || c.isEnum() || c.isInterface())
			return null;
		Object ob = null;
		try {
			ob = c.newInstance();
		} catch (Exception e) {
			Log.e("result", "getJsonArray " + "实列化Object错误 !");
			return ob;
		}
		Field[] fs = getiField(c);
		if (null == fs)
			return ob;
		return getJsonObject(object, ob, fs, c.getName());
	}

	/** @return List里的对象转换为 JSONArray 字符串 */
	public static <T> String getListClassJSONArray(List<T> datas) {
		if (datas == null || datas.size() < 0)
			return "[]";
		String result = "[";
		for (int i = 0; i < datas.size(); i++) {
			result += getClassJSONObject(datas.get(i)) + ",";
		}
		int len = result.length() - 1;
		if (result.charAt(len) == ',')
			result = result.substring(0, len);
		result += "]";
		return result;
	}

	/** @return Object下所有public 变量 和值 拼接成 JSONObject 字符串  <br>注意:<var>父类同名变量也会拼接进去</var>*/
	@SuppressWarnings("unchecked")
	public static String getClassJSONObject(Object o) {
		if (null == o|| isBaseType(o.getClass()) || o.getClass().isEnum())
			return "{}";
		if(o.getClass().isArray())
			return "[]";
		Field[] fs = getiField(o.getClass());
		if (fs == null || fs.length < 1)
			return "{}";
		String result = "{";
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			if (f.getModifiers() == (Modifier.FINAL | Modifier.STATIC))
				continue;
			String name = f.getName();
			Class<?> type = f.getType();
			try {
				if (type.isInterface() || type.isEnum()
						|| type.isAnnotation() || type.isAnonymousClass())
					continue;
				Object value = f.get(o);
				result += add2Json(name,value)+",";
			} catch (Exception e1) {
//				Log.i("result", type.getSimpleName() + " " + name + " 匹配失败!");
			}
		}
		int len = result.length() - 1;
		if (result.charAt(len) == ',')
			result = result.substring(0, len);
		result += "}";
		return result;
	}

	/** 拼接json中间字段,无头尾{}。*/
	public static String add2Json(String key, Object value){
		if (TextUtils.isEmpty(key))
			return  "";
		String result = "";
		Class<?> type = value.getClass();
		if (type.equals(String.class) || type.equals(char.class)) {
			result = "\"" + key + "\":\"" + value + "\"";
		} else if (isBaseType(type)) {
			result = "\"" + key + "\":" + value;
		}else if(type.equals(JSONArray.class) || type.equals(JSONObject.class)){
			if (null == value){
				if (type.equals(JSONArray.class))
					result = "\"" + key + "\":" + "[]";
				else
					result = "\"" + key + "\":" + "{}";
			}else {
				result = "\"" + key + "\":" + value.toString();
			}
		}else if (value instanceof List) {
			String str = getListClassJSONArray((List<Object>) value);
			if (!TextUtils.isEmpty(str))
				result = "\"" + key + "\":" + str;
		} else if (type.equals(Map.class)) {
			String str_va;
			if(null == value)
				str_va = "{}";
			else
				str_va = value.toString();
			result = "\"" + key + "\":" + str_va;
		} else {
			if(type.isArray()){
				result = "\"" + key + "\":" + value.toString();
			}else{
				String str = getClassJSONObject(value);
				if (str != null && !str.equals(""))
					result = "\"" + key + "\":" + str;
			}
		}
		return result;
	}

	/**是否是基本类型 */
	public static boolean isBaseType(Class<?> c){
		if (c.equals(int.class) || c.equals(boolean.class)
				|| c.equals(double.class)
				|| c.equals(float.class) || c.equals(long.class)
				|| c.equals(short.class) || c.equals(byte.class)
				|| c.equals(Integer.class)
				|| c.equals(Boolean.class)
				|| c.equals(Double.class)
				|| c.equals(Float.class) || c.equals(Long.class)
				|| c.equals(Short.class) || c.equals(Byte.class)
				|| c.equals(String.class) || c.equals(char.class)) {
			return true;
		}
		return false;
	}

	/** @return true json字符串 */
	public static boolean isJsonString(String str) {
		if (TextUtils.isEmpty(str))
			return false;
		boolean isJsonString = false;
		try {
			@SuppressWarnings("unused")
			JSONObject object = new JSONObject(str);
			isJsonString = true;
		} catch (JSONException e) {// 抛错 说明JSON字符不是数组或根本就不是JSON
			try {
				@SuppressWarnings("unused")
				JSONArray array = new JSONArray(str);
				isJsonString = true;
			} catch (JSONException e2) {// 抛错 说明JSON字符根本就不是JSON
				Log.e("result","非法的JSON字符串  => " + str);
				isJsonString = false;
			}
		}
		return isJsonString;
	}

	/** @return JSONObject对象 错误 return null */
	public static JSONObject getJSONObject(String str) {
		if (TextUtils.isEmpty(str))
			return null;
		JSONObject object = null;
		try {
			object = new JSONObject(str);
		} catch (JSONException e) {
			Log.e("result", "字符串不是JSONObject => " + str);
		}
		return object;
	}

	/** @return JSONArray对象 return null */
	public static JSONArray getJSONArray(String str) {
		if (TextUtils.isEmpty(str))
			return null;
		JSONArray array = null;
		try {
			array = new JSONArray(str);
		} catch (JSONException e) {
			Log.e("result", "字符串不是JSONArray => " + str);
		}
		return array;
	}

	/**
	 * @return 获取某个字段
	 */
	public static String getData(String result,String key) {
		JSONObject ob = getJSONObject(result);
		if (null == ob)
			return "";
		return ob.optString(key);
	}
	/**
	 * @return 获取Array数据
	 */
	public static JSONArray getDataArray(String result,String key) {
		JSONObject ob = getJSONObject(result);
		if (null == ob)
			return null;
		return ob.optJSONArray(key);
	}
	/**
	 * @return 获取Object数据
	 */
	public static JSONObject getDataObject(String result,String key) {
		JSONObject ob = getJSONObject(result);
		if (null == ob)
			return null;
		return ob.optJSONObject(key);
	}
	/**
	 * 本项目通用
	 * @param result 
	 * @return	状态是否成功
	 */
	public static boolean getState(String result){
		String stat = getData(result,"status");
		if("success".equals(stat))
			return true;
		return false;
	}
	/** 获取data数据 */
	public static String getData(String result){return getData(result,"data");	}
	/**
	 * 本项目通用
	 * @return error数据
	 */
	public static String getError(String result) {
		return getData(result,"message");
	}
	/** 获取code信息 */
	public static int getCode(String result){
		String code  = getData(result,"code");
		if (TextUtils.isEmpty(code) || "null".equals(code))
			return -1;
		return Integer.parseInt(getData(result,"code"));
	}
}
