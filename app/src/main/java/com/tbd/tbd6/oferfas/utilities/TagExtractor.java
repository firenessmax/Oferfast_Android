package com.tbd.tbd6.oferfas.utilities;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fireness on 27-12-15.
 */
public class TagExtractor {
    private ArrayList<String> tags;
    private String texto;
    private static final Pattern TAG_REGEX = Pattern.compile("#([A-Za-z0-9]+)");

    public TagExtractor(String texto) {
        this.tags=new ArrayList<String>();
        this.texto = texto;
    }

    public ArrayList<String> extract(){
        Matcher matcher = TAG_REGEX.matcher(this.texto);
        while (matcher.find()) {
            this.tags.add(matcher.group(1));
        }
        return this.tags;
    }
    public String extractString(){
        String s ="[";
        for(String tag:tags) {
            s += "\"" + tag + "\",";

        }
        s=s.substring(0,s.length()-1);
        s+="]";
        return s;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
