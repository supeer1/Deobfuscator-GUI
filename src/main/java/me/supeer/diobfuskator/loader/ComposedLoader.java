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
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import uwu.narumi.deobfuscator.Deobfuscator;
import uwu.narumi.deobfuscator.transformer.Transformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<String> transformerQueue = keys(content);
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

    private ArrayList<String> keys(String content){
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



    private static int computeBufferSize(InputStream inputStream) throws IOException {
        int expectedLength = inputStream.available();
        return expectedLength < 256 ? 4096 : Math.min(expectedLength, 1048576);
    }

    private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        } else {
            int bufferSize = computeBufferSize(inputStream);

            byte[] var7;
            try {
                ByteArrayOutputStream outputStream;
                label142: {
                    outputStream = new ByteArrayOutputStream();

                    try {
                        byte[] data = new byte[bufferSize];

                        int bytesRead;
                        int readCount;
                        for(readCount = 0; (bytesRead = inputStream.read(data, 0, bufferSize)) != -1; ++readCount) {
                            outputStream.write(data, 0, bytesRead);
                        }

                        outputStream.flush();
                        if (readCount == 1) {
                            var7 = data;
                            break label142;
                        }

                        var7 = outputStream.toByteArray();
                    } catch (Throwable var13) {
                        try {
                            outputStream.close();
                        } catch (Throwable var12) {
                        }

                        throw var13;
                    }

                    outputStream.close();
                    return var7;
                }

                outputStream.close();
            } finally {
                if (close) {
                    inputStream.close();
                }

            }

            return var7;
        }
    }

}
