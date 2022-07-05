package me.supeer.diobfuskator;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import me.supeer.diobfuskator.loader.ComposedLoader;
import me.supeer.diobfuskator.loader.TransformerLoader;
import uwu.narumi.deobfuscator.transformer.Transformer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Repo {

    public static final String BASE_URL = "https://raw.githubusercontent.com/narumii/Deobfuscator/master/src/main/java/uwu/narumi/deobfuscator/transformer/";
    public static final String JUNK = "https://raw.githubusercontent.com/narumii/Deobfuscator/master/src/main/java/";
    public static final String NORMAL_BASE_URL = "https://github.com/narumii/Deobfuscator/tree/master/src/main/java/uwu/narumi/deobfuscator/transformer/";

    public static String getSourceFromRepository(String transformer){
        String url = getUrl(transformer, true);
        return readContent(url);
    }

    public static String getUrl(String transformer, boolean t){
        if(t){
            return BASE_URL + "impl/" + (transformer.endsWith(".java") ? transformer : transformer + ".java");
        }
        return BASE_URL + "composed/" + (transformer.endsWith(".java") ? transformer : transformer + ".java");
    }

    public static String readContent(String url){
        try {
            URL u = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
            return new String(httpURLConnection.getInputStream().readAllBytes());
        }
        catch (Exception e){

        }
        return null;
    }

    public static Transformer getTransformerFromRepository(String transformer){
        String fullUrl = getUrl(transformer, true);
        String[] sp = fullUrl.split("/");
        String transformerSourceFile = sp[sp.length - 1];
        String transformerPackage = fullUrl.replace(JUNK, "").replace("/", ".").replace("."+transformerSourceFile, "");
        String source = getSourceFromRepository(transformer);
        if(source != null){
            TransformerLoader transformerLoader = new TransformerLoader(source.getBytes(StandardCharsets.UTF_8), transformerSourceFile, transformerPackage);
            return transformerLoader.loadAsTransformer();
        }
        return null;
    }

    public static ArrayList<String> loadComposedFromRepository(String a){
        String fullUrl = getUrl(a, false);
        String source = readContent(fullUrl);
        String[] sp = fullUrl.split("/");
        String transformerSourceFile = sp[sp.length - 1];
        String transformerPackage = fullUrl.replace(JUNK, "").replace("/", ".").replace("."+transformerSourceFile, "");
        ComposedLoader composedLoader = new ComposedLoader(source.getBytes(), transformerSourceFile, transformerPackage);
        return composedLoader.load();
    }

    public static boolean isTransformerAvailable(String transformer){
        String url = getUrl(transformer, true);
        String content = readContent(url);
        return content != null;
    }

    public static boolean isComposedAvailable(String a){
        String url = getUrl(a, false);
        String content = readContent(url);
        return content != null;
    }



}
