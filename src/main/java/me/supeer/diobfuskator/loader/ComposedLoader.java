package me.supeer.diobfuskator.loader;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import me.supeer.diobfuskator.Repo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ComposedLoader {

    public byte[] transformerBytes;
    public String fileName;
    public String parent;
    public String fullName;

    public ComposedLoader(byte[] source, String javaName, String parent){
        this.fileName = javaName;
        this.parent = parent;
        this.fullName = this.parent + "." + (this.fileName.replace(".java", ""));
        this.transformerBytes = source;
    }

    public ArrayList<String> load(){
        String content = new String(this.transformerBytes);
        ArrayList<String> transformers = new ArrayList<>();
        ArrayList<String> transformerQueue = queue(content);
        ArrayList<String> allTransformers = new ArrayList<>();
        String[] sp = content.split("\n");

        for(String a : sp){
            if(a.startsWith("import uwu.narumi.deobfuscator.transformer.impl.")){
                String aa = a.trim().replace("import uwu.narumi.deobfuscator.transformer.impl.", "").replace(";", "").trim();
                String ab = aa.replace(".", "/");
                if(Repo.isTransformerAvailable(ab)){
                    allTransformers.add(ab);
                }
                if(aa.contains(".*")){
                    String url = Repo.NORMAL_BASE_URL + "impl/" + aa.replace(".*", "") + "/";
                    try{
                        Connection connection = Jsoup.connect(url);
                        Document doc = connection.get();
                        Elements el1 = doc.getElementsByClass("js-navigation-open Link--primary");
                        for(Element el : el1){
                            String transformerSourceFile = el.text();
                            allTransformers.add((aa.replace(".*", "") + "/" + transformerSourceFile));
                        }
                    }
                    catch (Exception ex){

                    }
                }
            }
        }
        for(String queue : transformerQueue){
            for(String entry : allTransformers){
                if(entry.replace(".java", "").endsWith(queue)){
                    transformers.add(entry);
                }
            }
        }
        return transformers;
    }

    private ArrayList<String> queue(String content){
        String[] sp = content.split(",");
        ArrayList<String> keys = new ArrayList<>();
        for(int i = 0; i < sp.length; i ++){
            String p = sp[i].trim();
            if(i == 0){
                String aa = p.split("Arrays.asList\\(")[1].trim().replace("new ", "").replace("()", "");
                keys.add(aa);
                continue;
            }
            if(i == sp.length-1){
                String aa = p.split("\\);")[0].trim().replace("new ", "").replace("()", "");
                keys.add(aa);
                continue;
            }
            String aa = p.trim().replace("new ", "").replace("()", "");
            keys.add(aa);
        }
        return keys;
    }
}
