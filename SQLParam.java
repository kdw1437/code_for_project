//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uro.service.sql;

import com.uro.transfer.Param;

public class SQLParam extends Param {
    private static final long serialVersionUID = 1L;
    private transient ParameterSpec[] parameter;

    public SQLParam() {
    }

    public void setResourcekey(String resourcekey) {
        this.addValue("_resourcekey", resourcekey);
    }

    public String getResourcekey() {
        return this.getString("_resourcekey");
    }

    public void setSqlName(String sqlName) {
        this.addValue("_sqlName", sqlName);
    }

    public String getSqlName() {
        return this.getString("_sqlName");
    }

    public void setType(String type) {
        this.addValue("_type", type);
    }

    public String getType() {
        return this.getString("_type");
    }

    public void setQuery(String query) {
        this.addValue("_query", query);
    }

    public String getQuery() {
        return this.getString("_query");
    }

    public void setDatasource(String dataSource) {
        this.addValue("_dataSource", dataSource);
    }

    public String getDatasource() {
        return this.getString("_dataSource");
    }

    public String getDbmsType() {
        return this.getString("_dbmsType");
    }

    public void setDbmsType(String dbmsType) {
        this.addValue("_dbmsType", dbmsType);
    }

    public void setFetchMax(int fetchMax) {
        this.addValue("_fetchMax", fetchMax);
    }

    public int getFetchMax() {
        return this.getInt("_fetchMax");
    }

    public void setEncoding(String encoding) {
        this.addValue("_encoding", encoding);
    }

    public String getEncoding() {
        return this.getString("_encoding");
    }

    public void setIsolationLevel(String isolationLevel) {
        this.addValue("_isolationLevel", isolationLevel);
    }

    public String getIsolationLevel() {
        return this.getString("_isolationLevel");
    }

    public void setPaging(boolean isPaging) {
        this.addValue("_paging", isPaging);
    }

    public boolean isPaging() {
        return this.getBoolean("_paging", false);
    }

    public void setBatch(boolean isBatch) {
        this.addValue("_batch", isBatch);
    }

    public boolean isBatch() {
        return this.getBoolean("_batch");
    }

    public void setCount(int count) {
        this.addValue("_count", count);
    }

    public int getCount() {
        return this.getInt("_count");
    }

    public void setPage(int page) {
        this.addValue("_page", page);
    }

    public int getPage() {
        return this.getInt("_page", 0);
    }

    public void setPageSize(int pageSize) {
        this.addValue("_pageSize", pageSize);
    }

    public int getPageSize() {
        return this.getInt("_pageSize", 0);
    }

    public void setTotalPage(int totalPage) {
        this.addValue("_totalPage", totalPage);
    }

    public int getTotalPage() {
        return this.getInt("_totalPage", 0);
    }

    public void setTotalCount(int totalCount) {
        this.addValue("_totalCount", totalCount);
    }

    public int getTotalCount() {
        return this.getInt("_totalCount", 0);
    }

    public void setBatchCount(int[] batchCount) {
        this.addValue("_batchCount", batchCount);
    }

    public int[] getBatchCount() {
        return (int[])((int[])this.getValue("_batchCount"));
    }

    public void setResultName(String resultName) {
        this.addValue("_resultName", resultName);
    }

    public String getResultName() {
        return this.getString("_resultName");
    }

    public void setBatchName(String batchName) {
        this.addValue("_batchName", batchName);
    }

    public String getBatchName() {
        return this.getString("_batchName");
    }

    public void setParameter(ParameterSpec[] parameter) {
        this.parameter = parameter;
    }

    public ParameterSpec[] getParameter() {
        return this.parameter;
    }

    public void setLogYn(boolean logYn) {
        this.addValue("_logYn", logYn);
    }

    public void setRefererId(String refererId) {
        this.addValue("_refererId", refererId);
    }

    public String getRefererId() {
        return this.getString("_refererId");
    }

    public void setPurl(String purl) {
        this.addValue("_purl", purl);
    }

    public String getPurl() {
        return this.getString("_purl");
    }

    public void setClassPrcsId(String classPrcsId) {
        this.addValue("_classPrcsId", classPrcsId);
    }

    public String getClassPrcsId() {
        return this.getString("_classPrcsId");
    }

    public void setPreQueryReplaceParam(String[] param) {
        this.addValue("_preQueryReplaceParam", param);
    }

    public String[] getPreQueryReplaceParam() {
        return (String[])((String[])this.getValue("_preQueryReplaceParam", (Object)null));
    }
}
