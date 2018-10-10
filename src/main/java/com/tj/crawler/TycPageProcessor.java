package com.tj.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.concurrent.TimeUnit;

/**
 * @Author: jie.tang
 * @Date: 2018-10-10 下午11:28
 * @Desc:
 */
public class TycPageProcessor implements PageProcessor {

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(3).setSleepTime(100);
        buildHeaders(site);
        return site;
    }

    public void buildHeaders(Site site){
        String headerStr =
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                        "Accept-Encoding: gzip, deflate, br\n" +
                        "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7\n" +
                        "Connection: keep-alive\n" +
                        "Cookie: aliyungf_tc=AQAAADqiWG6hvQUALiF4alsZ7IWSypE1; channel=baidu; Hm_lvt_52d64b8d3f6d42a2e416d59635df3f71=1538231104,1539187455; sid=s%3A91P2eCg8AkTKaPZkUvLeHCGsE6mTcMFR.tBR2PDinXYRq31opoFO%2BpcZb7m2ayx19XF7zDGqwfac; latest_pos=MmQ1NTg2NDktOTRhOS00ZjM0LWI3YzQtMTQxMjczOGVmYmYy; Hm_lpvt_52d64b8d3f6d42a2e416d59635df3f71=1539187534; responseTimeline=147; _zg=%7B%22uuid%22%3A%20%2216625b7f38aaad-04650d4a3583f6-1130685c-384000-16625b7f38b90%22%2C%22sid%22%3A%201539187455.273%2C%22updated%22%3A%201539187551.703%2C%22info%22%3A%201539187455279%2C%22cuid%22%3A%20%2222edd96c-e83e-4b81-835a-bb2e469cc5ed%22%7D\n" +
                        "Host: www.qixin.com\n" +
                        "Referer: https://www.qixin.com/company/9eda1ceb-4d50-4b02-9ef0-ad1437d24f75\n" +
                        "Upgrade-Insecure-Requests: 1\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.3";

        String[] headerLines = headerStr.trim().split("\\n");
        for (int i = 0; i < headerLines.length; i++) {
            String[] headKeyVal = headerLines[i].split(":");
            site.addHeader(headKeyVal[0].trim(),headKeyVal[1].trim());
        }

    }

    @Override
    public void process(Page page) {
        System.out.println(page);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("begin");
        TycPageProcessor tycPageProcessor = new TycPageProcessor();
        Spider.create(tycPageProcessor).addUrl("https://www.qixin.com/search?key=58%E5%88%B0%E5%AE%B6").thread(1).run();
    }
}
