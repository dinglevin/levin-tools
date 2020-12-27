package com.dinglevin.tools.analysis.financial.data.crawler;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;

public abstract class DataCrawler {
    public static final String IFENG_FINANCE_API_HOST = "api.finance.ifeng.com";
    public static final String SINA_HOST = "sina.com.cn";
    public static final String SINA_JS_HOST = "sinajs.cn";
    public static final String SINA_FINANCE_HOST = "finance.sina.com.cn";
    public static final String SINA_FINANCE_VIP_HOST = "vip.stock.finance.sina.com.cn";
    public static final String MONEY_163_HOST = "money.163.com";
    public static final String CS_INDEX_HOST = "www.csindex.com.cn";
    public static final String EAST_MONEY_HOST = "eastmoney.com";
    public static final String SSE_HOST = "www.sse.com.cn";
    public static final String SSE_QUERY_HOST = "query.sse.com.cn";
    public static final String SZSE_HOST = "www.szse.cn";
    public static final String TU_DATA_HOST = "tudata.oss-cn-beijing.aliyuncs.com";
    public static final String SHIBOR_HOST = "www.shibor.org";
    
    public static final int DEFAULT_PORT = 80;
    
    private final String remoteHost;
    private final int remotePort;
    
    protected DataCrawler(String remoteHost, int remotePort) {
        checkArgument(StringUtils.isNotBlank(remoteHost), "remote host is blank");
        checkArgument(remotePort > 0, "remote port is 0 or less");
        
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }
    
    protected DataCrawler(String remoteHost) {
        this(remoteHost, DEFAULT_PORT);
    }
    
    public String getRemoteHost() {
        return remoteHost;
    }
    
    public int getRemotePort() {
        return remotePort;
    }
}
