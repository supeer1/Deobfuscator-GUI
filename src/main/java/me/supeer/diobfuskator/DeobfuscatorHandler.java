package me.supeer.diobfuskator;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import uwu.narumi.deobfuscator.Deobfuscator;
import uwu.narumi.deobfuscator.transformer.Transformer;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;

public class DeobfuscatorHandler {

    public static void handle(String input, String output, ArrayList<Transformer> transformers) {
        try{
            Deobfuscator deobfuscator = Deobfuscator.builder()
                    .input(Path.of(input))
                    .output(Path.of(output))
                    .transformers(
                            transformers
                    )
                    .classReaderFlags(0)
                    .classWriterFlags(0)
                    .consoleDebug()
                    .build();

            deobfuscator.start();
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

}
