package me.supeer.diobfuskator.loader;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import com.itranswarp.compiler.JavaStringCompiler;
import uwu.narumi.deobfuscator.transformer.Transformer;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class TransformerLoader {

    public byte[] transformerBytes;
    public String fileName;
    public String parent;
    public String fullName;

    public TransformerLoader(File file, String parent){
        this.fileName = file.getName();
        this.parent = parent;
        this.fullName = this.parent + "." + (this.fileName.replace(".java", ""));
        try{
            this.transformerBytes = Files.readAllBytes(file.toPath());
        }
        catch (Exception ex){
        }
    }

    public TransformerLoader(byte[] source, String javaName, String parent){
        this.fileName = javaName;
        this.parent = parent;
        this.fullName = this.parent + "." + (this.fileName.replace(".java", ""));
        this.transformerBytes = source;
    }

    public Class<?> load(){
        try{
            String JAVA_SOURCE_CODE = new String(transformerBytes);
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(this.fileName, JAVA_SOURCE_CODE);
            Class<?> clazz = compiler.loadClass(this.fullName, results);
            return clazz;
        }
        catch (Exception ex){

        }
        return null;
    }

    public Transformer loadAsTransformer(){
        try {
            return (Transformer) load().newInstance();
        }
        catch (Exception e){ }
        return null;
    }

}
