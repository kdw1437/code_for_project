//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uro.transfer;

import com.uro.util.StringUtil;
import java.io.Serializable;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String[] columns;
    private final List<Object> rows = new ArrayList();
    private int colSize;

    public ListParam(String[] columns) throws ParamException {
        if (columns == null) {
            throw new ParamException("String array of columns is null.");
        } else if (columns.length == 0) {
            throw new ParamException("Size of columns must be over 0.");
        } else {
            this.colSize = columns.length;
            this.columns = new String[this.colSize];

            for(int i = 0; i < this.colSize; ++i) {
                if (columns[i] == null) {
                    throw new ParamException(i + "-th element of column array is null.");
                }

                this.columns[i] = columns[i];
            }

        }
    }

    public int rowSize() {
        return this.rows.size();
    }

    public int colSize() {
        return this.colSize;
    }

    private void checkRowIndex(int rowidx) throws ParamException {
        if (rowidx < 0 || rowidx > this.rows.size() - 1) {
            throw new ParamException("Row index " + rowidx + " is out of range.");
        }
    }

    private void checkColIndex(int colidx) throws ParamException {
        if (colidx < 0 || colidx > this.colSize - 1) {
            throw new ParamException("Column index " + colidx + " is out of range.");
        }
    }

    public String[] getColumns() {
        return this.columns;
    }

    public int findColumn(String col) {
        for(int i = 0; i < this.columns.length; ++i) {
            if (this.columns[i].equalsIgnoreCase(col)) {
                return i;
            }
        }

        return -1;
    }

    public int findRow(String col, Object value) {
        int index = -1;
        int colidx = this.findColumn(col);
        if (value == null && this.rows == null) {
            return -1;
        } else {
            for(int i = 0; this.rows != null && i < this.rows.size(); ++i) {
                Object[] values = (Object[])((Object[])this.rows.get(i));
                if (values[colidx] == null && value == null || values[colidx] != null && values[colidx].equals(value)) {
                    index = i;
                    break;
                }
            }

            return index;
        }
    }

    public String getColumnName(int colidx) throws ParamException {
        this.checkColIndex(colidx);
        return this.columns[colidx];
    }

    public int addRow(Object[] values) throws ParamException {
        if (values == null) {
            throw new ParamException("Row value array must be not null");
        } else if (values.length != this.colSize) {
            throw new ParamException("Size of row value array must be column size");
        } else {
            this.rows.add(values);
            return this.rows.size() - 1;
        }
    }

    public Object[] getRow(int rowidx) throws ParamException {
        this.checkRowIndex(rowidx);
        return (Object[])((Object[])this.rows.get(rowidx));
    }

    public Object[] getRow(String column, Object value) throws ParamException {
        int rowidx = this.findRow(column, value);
        return this.getRow(rowidx);
    }

    public int createRow() {
        this.rows.add(new Object[this.colSize]);
        return this.rows.size() - 1;
    }

    public void setValue(int rowidx, String column, Object value) throws ParamException {
        int colidx = this.findColumn(column);
        if (colidx != -1) {
            this.setValue(rowidx, colidx, value);
        }
    }

    public void setValue(String column1, String value1, String column2, Object value2) throws ParamException {
        int rowidx = this.findRow(column1, value1);
        this.setValue(rowidx, column2, value2);
    }

    public void setValue(int rowidx, int colidx, Object value) throws ParamException {
        this.checkRowIndex(rowidx);
        this.checkColIndex(colidx);
        Object[] values = (Object[])((Object[])this.rows.get(rowidx));
        values[colidx] = value;
    }

    public void setValue(String column, Object value1, int colidx, Object value2) throws ParamException {
        int rowidx = this.findRow(column, value1);
        this.setValue(rowidx, colidx, value2);
    }

    public Param getParam(int rowidx) throws ParamException {
        Param param = new Param();
        Object[] values = this.getRow(rowidx);

        for(int i = 0; i < this.colSize; ++i) {
            param.addValue(this.columns[i], values[i]);
        }

        return param;
    }

    public Param getParam(String column, Object value) throws ParamException {
        int rowidx = this.findRow(column, value);
        return this.getParam(rowidx);
    }

    public int addParam(Param param) throws ParamException {
        if (param == null) {
            throw new ParamException("Can't add null Param");
        } else {
            int newrow = this.createRow();

            for(int i = 0; i < this.columns.length; ++i) {
                this.setValue(newrow, this.columns[i], param.getValue(this.columns[i]));
            }

            return this.rows.size() - 1;
        }
    }

    public Object getValue(int rowidx, String column) throws ParamException {
        int colidx = this.findColumn(column);
        return colidx == -1 ? null : this.getValue(rowidx, colidx);
    }

    public String getValue(int rowidx, String column, String defaultvalue) throws ParamException {
        String result = null;
        int colidx = this.findColumn(column);
        if (colidx == -1) {
            return defaultvalue;
        } else if (this.getValue(rowidx, colidx) == null) {
            return defaultvalue;
        } else {
            if (this.getValue(rowidx, colidx) instanceof Clob) {
                try {
                    result = StringUtil.getCLOB((Clob)this.getValue(rowidx, colidx));
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            } else {
                result = this.getValue(rowidx, colidx).toString();
            }

            return result == null ? defaultvalue : result;
        }
    }

    public String getValueSlash(int rowidx, String column, String target) throws ParamException {
        String result = null;
        int colidx = this.findColumn(column);
        if (colidx == -1) {
            return "";
        } else {
            if (this.getValue(rowidx, colidx) instanceof String) {
                result = this.getValue(rowidx, colidx).toString().replaceAll(target, "\\\\" + target);
            }

            return result == null ? "" : result;
        }
    }

    public Object getValue(String column1, Object value, String column2) throws ParamException {
        int rowidx = this.findRow(column1, value);
        return rowidx == -1 ? null : this.getValue(rowidx, column2);
    }

    public Object getValue(int rowidx, int colidx) throws ParamException {
        this.checkRowIndex(rowidx);
        this.checkColIndex(colidx);
        Object[] values = (Object[])((Object[])this.rows.get(rowidx));
        return values[colidx];
    }

    public Object getValue(String column, Object value, int colidx) throws ParamException {
        int rowidx = this.findRow(column, value);
        return rowidx == -1 ? null : this.getValue(rowidx, colidx);
    }

    public void clear() {
        this.rows.clear();
    }

    public void remove(int rowidx) throws ParamException {
        this.checkRowIndex(rowidx);
        this.rows.remove(rowidx);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\t[column:");

        int i;
        for(i = 0; i < this.columns.length; ++i) {
            sb.append(this.columns[i]).append(",");
        }

        sb.append("\n");

        for(i = 0; i < this.rows.size(); ++i) {
            Object[] values = (Object[])((Object[])this.rows.get(i));
            sb.append("\t[").append(i).append(":");

            for(int j = 0; j < values.length; ++j) {
                sb.append(values[j]);
                if (j < values.length - 1) {
                    sb.append(",");
                }
            }

            sb.append("]\n");
        }

        sb.append("]");
        return sb.toString();
    }

    public List<Map<String, Object>> toListMap() {
        List<Map<String, Object>> result = new ArrayList();

        for(int i = 0; i < this.rowSize(); ++i) {
            Map<String, Object> temp = new LinkedHashMap();

            for(int j = 0; j < this.columns.length; ++j) {
                temp.put(this.columns[j], this.getValue(i, this.columns[j], ""));
            }

            result.add(temp);
        }

        return result;
    }

    public String toCsv(String split) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < this.rowSize(); ++i) {
            for(int j = 0; j < this.columns.length; ++j) {
                sb.append((j == 0 ? "" : split) + this.getValue(i, this.columns[j], ""));
            }

            sb.append("\r\n");
        }

        return sb.toString();
    }
}
