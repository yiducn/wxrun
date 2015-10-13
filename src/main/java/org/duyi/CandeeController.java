package org.duyi;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.Arrays;


@Controller
public class CandeeController {
    private static final String TOKEN = "BabyXXX";
    @RequestMapping("checkCandee.do")
    public @ResponseBody
    String checkCandee(@RequestParam(value="signature", required=false)String signature,
                   @RequestParam(value="timestamp", required=false)String timestamp,
                   @RequestParam(value="nonce", required=false)String nonce,
                       @RequestParam(value="echostr", required=false) String echostr,
                       HttpServletRequest request
                   ) {
        if(!isValid(timestamp, nonce, signature))
            return "invalid";
        //
        String test = null;
        try {
            ServletInputStream sis = request.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sis);
            String s1 = doc.getElementsByTagName("ToUserName").toString();
            String s2 = doc.getElementsByTagName("Content").toString();
            test = "hello"+s1+s2;
            return test;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return "unknown test";
    }

    /**
     * 判断消息的真实性
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    private boolean isValid(String timestamp, String nonce,String signature){
        String[] result = {TOKEN, timestamp, nonce};
        Arrays.sort(result);
        String s = DigestUtils.sha1Hex(result[0] + result[1] + result[2]);
        return s.equals(signature);
    }
}

