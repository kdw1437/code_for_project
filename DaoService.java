//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uro;

import com.uro.biz.JediTransaction;
import com.uro.biz.JediTransactionManager;
import com.uro.log.LoggerMg;
import com.uro.service.sql.ParameterSpec;
import com.uro.service.sql.SQLParam;
import com.uro.service.sql.SQLServiceException;
import com.uro.service.sql.SQLServiceManager;
import com.uro.service.sql.SQLServiceSpec;
import com.uro.service.sql.SQLServiceXmlDAO;
import com.uro.sql.JediConnection;
import com.uro.sql.JediSQLException;
import com.uro.transfer.ListParam;
import com.uro.transfer.Param;
import com.uro.util.JsonUtil;
import com.uro.util.StringUtil;
import com.uro.waf.controller.RequestParser;
import com.uro.waf.vo.EnvVo;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;

public class DaoService {
    Logger log = LoggerMg.getInstance().getLogger("fw");
    private Param param = null;
    private SQLParam sqlparam = new SQLParam();
    private JediTransaction tran;
    private boolean error = false;
    private String message = "";
    private HttpServletRequest daoRequest;
    private HttpServletResponse daoResponse;

    public DaoService() {
        this.tran = JediTransactionManager.getJediTransaction();
    }

    public DaoService(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        this.daoRequest = request;
        this.tran = JediTransactionManager.getJediTransaction();

        try {
            this.preparedStart(request);
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

    }

    public DaoService(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        this.daoRequest = request;
        this.daoResponse = response;
        this.tran = JediTransactionManager.getJediTransaction();

        try {
            this.preparedStart(request);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

    }

    private void preparedStart(HttpServletRequest request) throws UnsupportedEncodingException {
        this.param = RequestParser.getInstance().parseParameter(request);
        this.sqlparam.addParam(this.param);
        new HashMap();
        Map<String, Object> mapObject = this.param.getMap();
        Iterator var3 = mapObject.keySet().iterator();

        while(true) {
            while(var3.hasNext()) {
                String mapKey = (String)var3.next();
                String value = (String)mapObject.get(mapKey);
                value = value.trim();
                if (value.length() >= 9 && value.substring(0, 9).equals("_j_s_o_n_")) {
                    value = value.substring(9, value.length());
                    List<HashMap> auths = JsonUtil.getObjectList(HashMap.class, !value.equals("") && !value.equals("undefined") ? value : "[]");
                    ListParam list = null;
                    String colums = "";
                    String[] columsArray = null;
                    if (auths.size() > 0) {
                        for(int i = 0; i < auths.size(); ++i) {
                            if (i == 0) {
                                Set set = ((HashMap)auths.get(i)).keySet();
                                Iterator interator = set.iterator();

                                for(int keyRow = 0; interator.hasNext(); ++keyRow) {
                                    String col = (String)interator.next();
                                    if (keyRow == 0) {
                                        colums = col;
                                    } else {
                                        colums = colums + "," + col;
                                    }
                                }

                                columsArray = colums.split(",");
                                list = new ListParam(columsArray);
                            }

                            list.createRow();

                            for(int k = 0; k < columsArray.length; ++k) {
                                String temp = ((HashMap)auths.get(i)).get(columsArray[k]) == null ? "" : ((HashMap)auths.get(i)).get(columsArray[k]).toString();
                                list.setValue(i, columsArray[k], StringUtil.removeTag(temp));
                            }
                        }
                    }

                    this.sqlparam.remove(mapKey);
                    this.sqlparam.addValue(mapKey, list);
                } else if (value.length() >= 9 && value.substring(0, 9).equals("_h_t_m_l_")) {
                    value = value.substring(9, value.length());

                    try {
                        File policyFile = new File(EnvVo.getAppHome() + "WEB-INF/env/antisamy-policy.xml");
                        Policy policy = Policy.getInstance(policyFile);
                        AntiSamy as = new AntiSamy(policy);
                        CleanResults cr = as.scan(value);
                        value = cr.getCleanHTML();
                        this.sqlparam.addValue(mapKey, value);
                    } catch (Exception var15) {
                        this.sqlparam.addValue(mapKey, "");
                        var15.printStackTrace();
                        this.sqlparam.addValue("_error", true);
                        this.sqlparam.addValue("_message", "HTML 본문에 XSS및 공격가능 문구가 포함되어 있습니다.");
                    }
                } else {
                    this.sqlparam.addValue(mapKey, StringUtil.removeTag(value));
                }
            }

            this.sqlparam.addValue("_error", this.error);
            this.sqlparam.addValue("_message", this.message);
            return;
        }
    }

    public Param getParam() {
        return this.param;
    }

    public SQLParam getSqlParam() {
        return this.sqlparam;
    }

    public JediTransaction getTran() {
        return this.tran;
    }

    public Connection getConnection(String resourcekey) {
        JediConnection con = null;
        Connection connect = null;

        try {
            con = JediConnection.getInstance(resourcekey);
            connect = con.getConnection();
        } catch (JediSQLException var5) {
            var5.printStackTrace();
        }

        return connect;
    }

    private boolean isClassUseSlq(String sqlName) {
        boolean isGo = true;
        if (this.sqlparam.getValue("_extention_class_mode") != null && (Boolean)this.sqlparam.getValue("_extention_class_mode")) {
            String classUseSql = this.sqlparam.getValue("_class_use_sql_", "").toString();
            if (classUseSql.indexOf(sqlName) == -1) {
                this.log.debug(sqlName + " is not used in the now extention class................");
                isGo = false;
            }
        }

        return isGo;
    }

    public void sqlexe(String sqlName, boolean isLog) throws SQLServiceException {
        this.sqlparam.addValue("_logYn", isLog);
        this.tran.setRefererId(this.sqlparam.getRefererId());
        this.tran.setPurl(this.sqlparam.getPurl());
        SQLServiceXmlDAO dao = SQLServiceXmlDAO.getInstance();
        SQLServiceSpec spec = dao.getSQLServiceSpec(sqlName.trim());
        int page = this.sqlparam.getPage();
        if (EnvVo.isMetadataReloadable() && !sqlName.startsWith("fw.")) {
            spec = null;
        }

        if (spec == null) {
            this.sqlparam.addValue("_o_pId", EnvVo.getpId());
            this.sqlparam.addValue("_o_prcsId", sqlName.trim());
            this.sqlparam.setSqlName("fw.selectTbmPrcsOne2");
            this.sqlparam = SQLServiceManager.getInstance().execute(this.sqlparam, this.tran);
            ListParam prcsList = this.sqlparam.getListParam("selectTbmPrcsOne2");
            if (prcsList.rowSize() > 0) {
                List<HashMap> SqlParameterList = JsonUtil.getObjectList(HashMap.class, prcsList.getValue(0, "sqlParameter", ""));
                ParameterSpec[] parameterArray = new ParameterSpec[SqlParameterList.size()];
                int m = 0;

                for(Iterator var10 = SqlParameterList.iterator(); var10.hasNext(); ++m) {
                    HashMap temp = (HashMap)var10.next();
                    parameterArray[m] = new ParameterSpec((String)temp.get("param"), "", "", (String)temp.get("queryParameter"), (String)temp.get("queryDefault"));
                }

                String name = sqlName.trim();
                String resourcekey = prcsList.getValue(0, "sqlResourceKey", "");
                String type = prcsList.getValue(0, "sqlTyp", "").equals("S") ? "simple" : "prepared";
                boolean paging = prcsList.getValue(0, "sqlPageFlag", "").equals("Y");
                boolean batch = prcsList.getValue(0, "sqlBatFlag", "").equals("Y");
                String description = prcsList.getValue(0, "prcsDesc", "");
                String query = prcsList.getValue(0, "sqlQuery", "").replaceAll("\\\\r\\\\n", "\\\n");
                int pageSize = Integer.parseInt(prcsList.getValue(0, "sqlPageSize", "0"));
                String batchName = prcsList.getValue(0, "sqlBatNm", "");
                String resultName = prcsList.getValue(0, "sqlResultNm", "");
                String staticBatchYn = prcsList.getValue(0, "staticBatchYn", "N");
                String prcsInParams = prcsList.getValue(0, "prcsInParams", "");
                List<HashMap> prcsInParamsList = JsonUtil.getObjectList(HashMap.class, prcsInParams);
                String replaceParam = "";

                for(int i = 0; i < prcsInParamsList.size(); ++i) {
                    if (((HashMap)prcsInParamsList.get(i)).get("checkYn") != null && (Boolean)((HashMap)prcsInParamsList.get(i)).get("checkYn")) {
                        replaceParam = replaceParam + "," + (String)((HashMap)prcsInParamsList.get(i)).get("param");
                    }
                }

                replaceParam = replaceParam.equals("") ? "" : replaceParam.substring(1);
                dao.setSQLServiceSpec(new SQLServiceSpec(name, resourcekey, type, paging, batch, description, query, pageSize, batchName, resultName, parameterArray, staticBatchYn, replaceParam));
                spec = dao.getSQLServiceSpec(name);
            }
        }

        if (spec.getStaticBatchYn().equals("Y")) {
            this.sqlparam.addValue("_static_batch", "Y");
        }

        if (!dao.getSQLServiceSpec(sqlName.trim()).getSqlReplaceParam().equals("")) {
            this.sqlparam.setPreQueryReplaceParam(dao.getSQLServiceSpec(sqlName.trim()).getSqlReplaceParam().split(","));
        }

        this.sqlparam.setPage(page);
        if (this.isClassUseSlq(sqlName)) {
            this.sqlparam.setSqlName(sqlName);
            this.sqlparam = SQLServiceManager.getInstance().execute(this.sqlparam, this.tran);
        }

        this.sqlparam.addValue("_static_batch", "N");
    }

    public void setSqlParam(SQLParam sqlparam) {
        this.sqlparam = sqlparam;
    }

    public ListParam getListParam(String listName) {
        return this.sqlparam.getListParam(listName);
    }

    public ListParam getNowListParam() {
        return this.sqlparam.getListParam(this.sqlparam.getResultName());
    }

    public void setError(String message) {
        this.error = true;
        this.message = message;
        this.sqlparam.addValue("_error", this.error);
        this.sqlparam.addValue("_message", message);
    }

    public boolean isForceTargetUrl() {
        return this.sqlparam.getBoolean("_forceTargetUrlYn", false);
    }

    public void setForceTargetUrlYn(boolean yn) {
        this.sqlparam.addValue("_forceTargetUrlYn", yn);
    }

    public void setForceTargetUrl(String targetUrl) {
        this.sqlparam.addValue("_forceTargetUrlYn", true);
        this.sqlparam.addValue("_forceTargetUrl", targetUrl);
    }

    public String getForceTargetUrl() {
        return this.sqlparam.getString("_forceTargetUrl", "");
    }

    public void commit() {
        this.tran.commit();
    }

    public void rollback() {
        this.tran.rollback();
    }

    public void setMessage(String message) {
        this.sqlparam.addValue("_message", message);
    }

    public void sqlParamClear() {
        this.sqlparam.clear();
    }

    public void setResultName(String resultName) {
        this.sqlparam.setResultName(resultName);
    }

    public void setReqTarget(String reqTarget) {
        this.sqlparam.addValue("_reqTarget", reqTarget);
    }

    public void setStaticBatch(boolean staticBatchYn) {
        String yn = staticBatchYn ? "Y" : "N";
        this.sqlparam.addValue("_static_batch", yn);
    }

    public void exeSql() throws SQLServiceException {
        this.sqlparam = SQLServiceManager.getInstance().execute(this.sqlparam, this.tran);
    }

    public void exe() throws SQLServiceException {
        this.sqlparam = SQLServiceManager.getInstance().execute(this.sqlparam, this.tran);
    }

    public void getFwPrcs(String prcsId) throws SQLServiceException {
        this.sqlparam = SQLServiceManager.getInstance().execute(this.sqlparam, this.tran);
    }

    public String getResultName() {
        return this.sqlparam.getResultName();
    }

    public void setSqlParamAddValue(String key, String value) {
        this.sqlparam.addValue(key, value);
    }

    public void setSqlParamAddValue(String key, ListParam list) {
        this.sqlparam.addValue(key, list);
    }

    public void sqlParamRemoveValue(String[] keys) {
        for(int i = 0; i < keys.length; ++i) {
            this.sqlparam.remove(keys[i]);
        }

    }

    public HttpServletRequest getRequest() {
        return this.daoRequest;
    }

    public HttpServletResponse getResponse() {
        return this.daoResponse;
    }

    public void setExtentionClassMode(boolean useyn) {
        this.sqlparam.addValue("_extention_class_mode", useyn);
    }

    public boolean isError() {
        return this.sqlparam.getBoolean("_error", false);
    }

    public void setNotSaveLog() {
        this.sqlparam.addValue("_isNotSaveLog", "Y");
    }

    public boolean isNotSaveLog() {
        String logYn = (String)this.sqlparam.getValue("_isNotSaveLog", "N");
        return logYn.equals("Y");
    }

    public ListParam jsonToListParam(String value) {
        List<HashMap> auths = JsonUtil.getObjectList(HashMap.class, value);
        ListParam list = null;
        String colums = "";
        String[] columsArray = null;
        if (auths.size() > 0) {
            for(int i = 0; i < auths.size(); ++i) {
                if (i == 0) {
                    Set set = ((HashMap)auths.get(i)).keySet();
                    Iterator interator = set.iterator();

                    for(int keyRow = 0; interator.hasNext(); ++keyRow) {
                        String col = (String)interator.next();
                        if (keyRow == 0) {
                            colums = col;
                        } else {
                            colums = colums + "," + col;
                        }
                    }

                    columsArray = colums.split(",");
                    list = new ListParam(columsArray);
                }

                list.createRow();

                for(int k = 0; k < columsArray.length; ++k) {
                    String temp = ((HashMap)auths.get(i)).get(columsArray[k]) == null ? "" : ((HashMap)auths.get(i)).get(columsArray[k]).toString();
                    list.setValue(i, columsArray[k], temp);
                }
            }
        }

        return list;
    }

    public String getStringValue(String key) {
        return (String)this.sqlparam.getValue(key, "");
    }

    public String getStringValue(String key, String defaultValue) {
        return (String)this.sqlparam.getValue(key, defaultValue);
    }

    public int getIntValue(String key) {
        return Integer.parseInt((String)this.sqlparam.getValue(key, ""));
    }

    public void setValue(String key, Object value) {
        this.sqlparam.addValue(key, value);
    }

    public void setPreQueryReplaceParam(String[] param) {
        this.sqlparam.addValue("_preQueryReplaceParam", param);
    }

    public String[] getPreQueryReplaceParam() {
        return (String[])((String[])this.sqlparam.getValue("_preQueryReplaceParam", (Object)null));
    }

    public String getReqUrl() {
        return this.sqlparam.getString("_reqUrl", "");
    }

    public String getReqTyp() {
        return this.sqlparam.getString("_reqTyp", "");
    }

    public boolean isView() {
        return this.sqlparam.getString("_reqTyp", "").equals("V");
    }
}
