package com.springapp.mvc;

import org.assertj.core.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.selector.Html;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppTests {
    private static Logger LOG = LoggerFactory.getLogger(AppTests.class);

    @Test
    public void simple() throws Exception {
        LOG.info("test test");

    }

    @Test
    public void testXpath() throws Exception {
        Path path = Paths.get("E:\\companies\\1044904777028734976.html");
        String content = Strings.join(Files.readAllLines(path)).with("\n");

        Html html = new Html(content);
        String s = html.$(".result-list").$(".info").xpath("/div/div").all().get(0);
        html.xpath("//div[contains(text(),'注册资本')]").all().get(0);
        LOG.info(s);

    }
}
