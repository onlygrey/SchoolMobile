package com.example.schalbyshev.yandex.core;

import android.text.TextUtils;

import com.example.schalbyshev.yandex.core.Lang;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by schalbyshev on 08.04.2017.
 */

public class LangXMLParser {

    public final String LOG_TAG = this.getClass().getSimpleName()+" !TAG! ";

    public String textTranslate;
    public ArrayList<Lang> langArrayList;
    public LangXMLParser(String stringXML){
        String tmp = "";
        String stringKey = "";
        String stringValue = "";
        //Log.d(LOG_TAG, "LangXMLParser " + stringXML);
        langArrayList=new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            // даем парсеру на вход Reader
            xpp.setInput(new StringReader(stringXML));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        //Log.d(LOG_TAG, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        /*Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                                + ", depth = " + xpp.getDepth() + ", attrCount = "
                                + xpp.getAttributeCount());*/
                        tmp = "";

                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                            if (xpp.getAttributeName(i).equals("key")) {
                                stringKey=xpp.getAttributeValue(i);
                            }else if (xpp.getAttributeName(i).equals("value")){
                                stringValue=xpp.getAttributeValue(i);
                                langArrayList.add(new Lang(stringKey,stringValue));
                            }
                        }
                        if (!TextUtils.isEmpty(tmp))
                            //Log.d(LOG_TAG, "Attributes: " + tmp);
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        //Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        textTranslate=xpp.getText();
                        //Log.d(LOG_TAG, "text = " + textTranslate);
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            //Log.d(LOG_TAG, "END_DOCUMENT");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Lang> getAllLangs(){
        return langArrayList;
    }
}
