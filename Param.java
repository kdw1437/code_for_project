//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uro.transfer;

import com.uro.util.StringUtil;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Param implements IDTO {
    private static final long serialVersionUID = 1L;
    private final HashMap<String, Object> map = new HashMap();

    public Param() {
    }

    private String toUpper(String key) {
        return key == null ? null : key.trim();
    }

    public void addValue(String key, Object value) {
        this.map.put(this.toUpper(key), value);
    }

    public void addValue(String key, int value) {
        this.map.put(this.toUpper(key), new Integer(value));
    }

    public void addValue(String key, byte value) {
        this.map.put(this.toUpper(key), new Byte(value));
    }

    public void addValue(String key, short value) {
        this.map.put(this.toUpper(key), new Short(value));
    }

    public void addValue(String key, long value) {
        this.map.put(this.toUpper(key), new Long(value));
    }

    public void addValue(String key, float value) {
        this.map.put(this.toUpper(key), new Float(value));
    }

    public void addValue(String key, double value) {
        this.map.put(this.toUpper(key), new Double(value));
    }

    public void addValue(String key, boolean value) {
        this.map.put(this.toUpper(key), new Boolean(value));
    }

    public boolean containsKey(String key) {
        return this.map.containsKey(this.toUpper(key));
    }

    public Object getValue(String key) {
        return this.map.get(this.toUpper(key));
    }

    public Object getValue(String key, Object defaultValue) {
        Object result = this.getValue(key);
        if (result == null) {
            result = defaultValue;
        }

        return result;
    }

    public String getString(String key) {
        Object v = this.map.get(this.toUpper(key));
        return v == null ? null : v.toString();
    }

    public String getString(String key, String defaultValue) {
        String result = defaultValue;
        Object v = this.map.get(this.toUpper(key));
        if (v != null) {
            if (v instanceof Clob) {
                try {
                    result = StringUtil.getCLOB((Clob)v);
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            } else {
                result = v.toString();
            }
        }

        return result;
    }

    public int getInt(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));
        if (v == null) {
            return 0;
        } else {
            try {
                return v instanceof Number ? ((Number)v).intValue() : (new BigDecimal(v.toString())).intValue();
            } catch (NumberFormatException var4) {
                throw new ParamException("Param.getInt() : value '" + v.toString() + "' of key '" + key + "' is not number.", 0);
            }
        }
    }

    public int getInt(String key, int defaultValue) throws ParamException {
        try {
            int result = this.getInt(key);
            return result;
        } catch (ParamException var5) {
            return defaultValue;
        }
    }

    public byte getByte(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));
        if (v == null) {
            return 1;
        } else {
            try {
                return v instanceof Number ? ((Number)v).byteValue() : (new BigDecimal(v.toString())).byteValue();
            } catch (NumberFormatException var4) {
                throw new ParamException("Param.getByte() : value '" + v.toString() + "' of key '" + v + "' is not number.", 0);
            }
        }
    }

    public byte getByte(String key, byte defaultValue) throws ParamException {
        try {
            byte result = this.getByte(key);
            return result;
        } catch (ParamException var5) {
            return defaultValue;
        }
    }

    public short getShort(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));

        try {
            return v instanceof Number ? ((Number)v).shortValue() : (new BigDecimal(v.toString())).shortValue();
        } catch (NumberFormatException var4) {
            throw new ParamException("Param.getShort() : value '" + v.toString() + "' of key '" + key + "' is not number.", 0);
        }
    }

    public short getShort(String key, short defaultValue) throws ParamException {
        try {
            short result = this.getShort(key);
            return result;
        } catch (ParamException var5) {
            return defaultValue;
        }
    }

    public long getLong(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));

        try {
            return v instanceof Number ? ((Number)v).longValue() : (new BigDecimal(v.toString())).longValue();
        } catch (NumberFormatException var4) {
            throw new ParamException("Param.getLong() : value '" + v.toString() + "' key '" + key + "' is not number.", 0);
        }
    }

    public long getLong(String key, long value) throws ParamException {
        try {
            long result = this.getLong(key);
            return result;
        } catch (ParamException var7) {
            return value;
        }
    }

    public float getFloat(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));

        try {
            return v instanceof Number ? ((Number)v).floatValue() : (new BigDecimal(v.toString())).floatValue();
        } catch (NumberFormatException var4) {
            throw new ParamException("Param.getFloat() : value '" + v.toString() + "' of key '" + key + "' is not number.", 0);
        }
    }

    public float getFloat(String key, float value) throws ParamException {
        try {
            float result = this.getFloat(key);
            return result;
        } catch (ParamException var5) {
            return value;
        }
    }

    public double getDouble(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));

        try {
            return v instanceof Number ? ((Number)v).doubleValue() : (new BigDecimal(v.toString())).doubleValue();
        } catch (NumberFormatException var4) {
            throw new ParamException("Param.getDouble() : value '" + v.toString() + "' of key '" + key + "' is not number.", 0);
        }
    }

    public double getDouble(String key, double value) throws ParamException {
        try {
            double result = this.getDouble(key);
            return result;
        } catch (ParamException var7) {
            return value;
        }
    }

    public boolean getBoolean(String key) throws ParamException {
        Object v = this.map.get(this.toUpper(key));
        if (v != null) {
            return Boolean.valueOf(v.toString());
        } else {
            throw new ParamException("Param.getBoolean() : key '" + key + "' doesn't exist or value is null.", 1);
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean result = defaultValue;

        try {
            result = this.getBoolean(key);
        } catch (ParamException var5) {
        }

        return result;
    }

    public ListParam getListParam(String key) {
        try {
            ListParam list = (ListParam)this.map.get(this.toUpper(key));
            return list;
        } catch (ClassCastException var3) {
            return null;
        }
    }

    public void clear() {
        this.map.clear();
    }

    public void remove(String key) {
        this.map.remove(this.toUpper(key));
    }

    public String[] keys() {
        Set<String> keys = this.map.keySet();
        String[] keylist = new String[keys.size()];
        return (String[])((String[])keys.toArray(keylist));
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public void addParam(Param param) {
        if (param != null) {
            this.map.putAll(param.getMap());
        }

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<String> iter = this.map.keySet().iterator();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            Object value = this.map.get(key);
            sb.append(key).append("=").append(value);
            if (iter.hasNext()) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public void view() {
    }
}
