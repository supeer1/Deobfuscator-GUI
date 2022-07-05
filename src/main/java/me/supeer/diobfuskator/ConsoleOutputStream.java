package me.supeer.diobfuskator;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import javax.swing.*;
import java.io.OutputStream;

public class ConsoleOutputStream
        extends OutputStream {
    public JTextArea console;

    public ConsoleOutputStream(JTextArea textArea) {
        this.console = textArea;
    }

    @Override
    public void write(int n) {
        this.console.append(String.valueOf((char)n));
    }
}
