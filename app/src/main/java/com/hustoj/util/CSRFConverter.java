package com.hustoj.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Relish Wang
 * @since 2018/01/07
 */
public class CSRFConverter {
    public static String convert(String csrfXml) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            if (documentBuilder != null) {
                document = documentBuilder.parse(new StringBufferInputStream(csrfXml));
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取根标签
        Element element = null;
        if (document != null) {
            element = document.getDocumentElement();
        }
        if (element != null) {
            return element.getAttribute("value");
        }
        return null;
    }
}
