//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uro.biz;

import com.uro.log.LoggerMg;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;

public class JediTransaction {
    private Map<String, JediTransaction> tranGroup;
    private boolean isLog = false;
    private String sqlNm = "";
    private String refererId = "";
    private String purl = "";
    private String classPrcsId = "";
    Logger log = LoggerMg.getInstance().getLogger("fw");

    public JediTransaction() {
    }

    public void setTransaction(JediTransaction tran) {
        if (this.tranGroup == null) {
            this.tranGroup = new HashMap();
        }

        this.tranGroup.put(tran.getResourceName(), tran);
    }

    public JediTransaction getTransaction(String resourceName) {
        return this.tranGroup == null ? null : (JediTransaction)this.tranGroup.get(resourceName);
    }

    public void begin() throws TransactionException {
    }

    public void commit() throws TransactionException {
        if (!this.sqlNm.equals("")) {
        }

        if (this.tranGroup != null) {
            Iterator iter = this.tranGroup.values().iterator();

            while(iter.hasNext()) {
                try {
                    ((JediTransaction)iter.next()).commit();
                } catch (Exception var3) {
                }
            }
        }

        JediTransactionManager.remove(this);
        this.tranGroup = null;
    }

    public void rollback() throws TransactionException {
        if (!this.sqlNm.equals("")) {
            this.log.info("※" + this.refererId + this.getUrl(this.purl) + this.getUrl(this.classPrcsId) + " [" + this.sqlNm + "] [Transaction of " + getGroupName() + "] is being rollbacked and dbconnection closed");
        }

        if (this.tranGroup != null) {
            Iterator iter = this.tranGroup.values().iterator();

            while(iter.hasNext()) {
                try {
                    ((JediTransaction)iter.next()).rollback();
                } catch (Exception var3) {
                }
            }
        }

        JediTransactionManager.remove(this);
        this.tranGroup = null;
    }

    public String getResourceName() {
        return null;
    }

    public String toString() {
        return "[JediTransaction : " + this.tranGroup + "]";
    }

    private static String getGroupName() {
        return Thread.currentThread().getName();
    }

    public static Long getGroupId() {
        return Thread.currentThread().getId();
    }

    public static String getTransactionGroup() {
        return Thread.currentThread().getName();
    }

    public boolean isLog() {
        return this.isLog;
    }

    public void setIsLog(boolean yn) {
        this.isLog = yn;
    }

    public String getSqlNm() {
        return this.sqlNm;
    }

    public void setSqlNm(String _sqlNm) {
        this.sqlNm = _sqlNm;
    }

    public void setRefererId(String _refererId) {
        if (_refererId == null) {
            _refererId = "unknown";
        }

        this.refererId = _refererId;
    }

    public String getRefererId() {
        return this.refererId;
    }

    public void setPurl(String _purl) {
        this.purl = _purl;
    }

    public String getPurl() {
        return this.purl;
    }

    private String getUrl(String url) {
        if (url == null) {
            return "";
        } else {
            String pURL = "";
            if (!url.equals("")) {
                pURL = "[" + url + "]";
            }

            return pURL;
        }
    }

    public void setClassPrcsId(String _classPrcsId) {
        this.classPrcsId = _classPrcsId;
    }

    public String getClassPrcsId() {
        return this.classPrcsId;
    }
}
