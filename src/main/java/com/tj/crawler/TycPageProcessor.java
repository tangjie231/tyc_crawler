package com.tj.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jie.tang
 * @Date: 2018-10-10 下午11:28
 * @Desc:
 */
public class TycPageProcessor implements PageProcessor {
    private static Logger logger = LoggerFactory.getLogger(TycPageProcessor.class);

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
                        "Accept-Language: zh-CN,zh;q=0.9\n" +
                        "Cache-Control: no-cache\n" +
                        "Connection: keep-alive\n" +
                        "Cookie: TYCID=128660e0c2f611e8ab81759e05b6e185; undefined=128660e0c2f611e8ab81759e05b6e185; ssuid=2060762260; _ga=GA1.2.1376999845.1538122277; aliyungf_tc=AQAAACCBq3HyJgQAorfGb+83T6OItinz; csrfToken=NymNPgZ3DifLRl5q514QG3SY; bannerFlag=true; _gid=GA1.2.338539365.1538964190; RTYCID=65a26ddaa9e34c6a849dece6586fda8a; CT_TYCID=0db6bbb7a7df49f6a0efd67a0b89d0d9; tyc-user-info=%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODUxNDIzNjAyNSIsImlhdCI6MTUzOTIyMjI2MCwiZXhwIjoxNTU0Nzc0MjYwfQ.rBf6c1v7-IeQX9BxIuIUmm7ZojzU7rfQUe_OY-_1Jz7Ku37XquH_ZACI-0Y3-Cy33YxCXV3I_HTpDdwLhQa62g%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522redPoint%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522monitorUnreadCount%2522%253A%252212%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252218514236025%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODUxNDIzNjAyNSIsImlhdCI6MTUzOTIyMjI2MCwiZXhwIjoxNTU0Nzc0MjYwfQ.rBf6c1v7-IeQX9BxIuIUmm7ZojzU7rfQUe_OY-_1Jz7Ku37XquH_ZACI-0Y3-Cy33YxCXV3I_HTpDdwLhQa62g; cloud_token=b5db61a8212c47ea940716137c2e4f4d; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1539164563,1539165728,1539222249,1539245989; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1539245989; _gat_gtag_UA_123487620_1=1\n" +
                        "Host: www.tianyancha.com\n" +
                        "Pragma: no-cache\n" +
                        "Upgrade-Insecure-Requests: 1\n" +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

        String[] headerLines = headerStr.trim().split("\\n");
        for (int i = 0; i < headerLines.length; i++) {
            String[] headKeyVal = headerLines[i].split(":");
            site.addHeader(headKeyVal[0].trim(),headKeyVal[1].trim());
        }

    }

    @Override
    public void process(Page page) {
        System.out.println(page.getRawText());
        Html html = page.getHtml();
        String companyName = html.xpath("//a[@tyc-event-ch=\"CompanySearch.Company\"]").nodes().get(0).xpath("//a/text()").get();
        List<Selectable> nodes = html.$(".result-list").$(".info").xpath("/div/div").nodes();
        if(nodes != null && !nodes.isEmpty()) {
            String legalPersonName = nodes.get(0).xpath("//a/text()").get();
            String regCapital = nodes.get(1).xpath("//span/text()").get();
            String regTime = nodes.get(2).xpath("//span/text()").get();

            page.putField("companyName",companyName);
            page.putField("legalPersonName",legalPersonName);
            page.putField("regCapital",regCapital);
            page.putField("regTime",regTime);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("begin");

        TycPageProcessor tycPageProcessor = new TycPageProcessor();
        Spider.create(tycPageProcessor)
                .addUrl("https://www.tianyancha.com/search?key=%e5%ad%90%e5%bc%b9%e7%9f%ad%e4%bf%a1")
                .addPipeline(new FilePipeline("e:/result"))
                .thread(1)
                .run();
    }
}
