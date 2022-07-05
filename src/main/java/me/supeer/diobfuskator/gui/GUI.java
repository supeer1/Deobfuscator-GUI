package me.supeer.diobfuskator.gui;
/*
 *
 * @Author supeer
 *
 *
 * - 2022 -
 *
 */


import me.supeer.diobfuskator.ConsoleOutputStream;
import me.supeer.diobfuskator.DeobfuscatorHandler;
import me.supeer.diobfuskator.Main;
import me.supeer.diobfuskator.Repo;
import me.supeer.diobfuskator.loader.ComposedLoader;
import me.supeer.diobfuskator.loader.TransformerLoader;
import org.apache.logging.log4j.core.util.Loader;
import uwu.narumi.deobfuscator.transformer.Transformer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

public class GUI extends JFrame {

    private final JPanel contentPane;
    private final JTextField inputText;
    private final JTextField outputText;
    private final JLabel lblOutput;
    private final JButton btnDeobfuscate;
    private final JList list;
    private final DefaultListModel defaultListModel;
    private final JTextField tra;
    private  JButton addToList;
    private  JButton removeFromList;
    private JButton testTransformers;

    public static String title = "DiObfuskator GUI " + Main.VER;

    public static void open(String[] args) {
        try {
            GUI frame = new GUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GUI() {
        setResizable(false);
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle(title);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 990, 760);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(40, 42, 54));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblInput = new JLabel("Input :");
        lblInput.setForeground(Color.WHITE);
        lblInput.setFont(new Font("Yu Gothic", Font.PLAIN, 15));
        lblInput.setBounds(38, 23, 113, 24);
        contentPane.add(lblInput);

        inputText = new JTextField();
        inputText.setBounds(38, 46, 847, 28);
        contentPane.add(inputText);
        inputText.setColumns(10);

        outputText = new JTextField();
        outputText.setColumns(10);
        outputText.setBounds(38, 121, 847, 28);
        contentPane.add(outputText);

        lblOutput = new JLabel("Output :");
        lblOutput.setForeground(Color.WHITE);
        lblOutput.setFont(new Font("Yu Gothic", Font.PLAIN, 15));
        lblOutput.setBounds(38, 95, 113, 24);
        contentPane.add(lblOutput);

        btnDeobfuscate = new JButton("Deobfuscate");
        btnDeobfuscate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnDeobfuscate.setForeground(Color.WHITE);
        btnDeobfuscate.setBackground(new Color(40, 42, 54));
        btnDeobfuscate.setBounds(771, 635, 189, 63);
        contentPane.add(btnDeobfuscate);

        int elHeight = 60;

        list = new JList();
        defaultListModel = new DefaultListModel();
        list.setModel(defaultListModel);
        list.setBackground(Color.WHITE);
        list.setForeground(Color.BLACK);
        list.setBounds(20, 165, 990-50, (635)- 165 - elHeight - 10);
        contentPane.add(list);

        int wl = 990-50;
        int hl = 165 + (635)- 165 - elHeight - 10;

        tra = new JTextField();
        tra.setColumns(10);
        lblOutput.setForeground(Color.WHITE);
        lblOutput.setFont(new Font("Yu Gothic", Font.PLAIN, 15));
        tra.setBounds(20, hl + 10, wl/2, elHeight - 25);
        contentPane.add(tra);

        addToList = new JButton("ADD");
        addToList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addToList.setBounds(20+wl/2 + 10, hl + 10, wl / 4 - 10, elHeight - 25);
        contentPane.add(addToList);

        removeFromList = new JButton("REMOVE");
        removeFromList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        removeFromList.setBounds(20+wl/2 + 10 + (wl / 4 - 10), hl + 10, wl / 4 - 10, elHeight - 25);
        contentPane.add(removeFromList);

        testTransformers = new JButton("Test Transformers");
        testTransformers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        testTransformers.setBounds(20, hl + 10 + elHeight -25 + 10, wl/4, elHeight - 25);
        contentPane.add(testTransformers);

        addToList.addActionListener((e) -> {
            String tran = tra.getText();
            if(tran != null && !tran.isEmpty()){
                if(Repo.isComposedAvailable(tran)){
                    this.setTitle("Loading composed class"+String.format("(%s)", tran));
                    StringBuilder builder = new StringBuilder();
                    builder.append("Composed class loaded.\n");
                    for(String entry : Repo.loadComposedFromRepository(tran)){
                        addElementToList(entry);
                        builder.append(entry+".\n");
                    }
                    this.setTitle(title);
                    JOptionPane.showMessageDialog(null, builder.toString());
                    return;
                }
                if(Repo.isTransformerAvailable(tran)){
                    addElementToList(tran);
                }
                else{
                    JOptionPane.showMessageDialog(null, "nothing found!");
                    tra.setText("");
                }
            }
        });

        removeFromList.addActionListener((e) -> {
            for(Object seletectedItem : list.getSelectedValuesList()){
                removeElementFromList(seletectedItem);
            }
        });

        testTransformers.addActionListener((e) -> {
            if(list.getModel().getSize() == 0){
                JOptionPane.showMessageDialog(null, "no transformers added");
                return;
            }
            this.setTitle("testing transformers...");
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < list.getModel().getSize(); i++){
                String tran = String.valueOf(list.getModel().getElementAt(i));
                Transformer transformer = Repo.getTransformerFromRepository(tran);
                if(transformer != null){
                    builder.append(Repo.getUrl(tran, true) + " successfully loaded.\n");
                }
                else{
                    builder.append(Repo.getUrl(tran, true) + " failed to load.\n");
                }
            }
            JOptionPane.showMessageDialog(null, builder.toString());
            this.setTitle(title);
        });

        btnDeobfuscate.addActionListener((e) -> {
            if(list.getModel().getSize() == 0){
                JOptionPane.showMessageDialog(null, "no transformers added");
                return;
            }
            String input = inputText.getText();
            String output = outputText.getText();
            if(output == null || input == null || input.isEmpty() || output.isEmpty()){
                return;
            }
            openConsole();
            Thread thread = new Thread(() -> {
                ArrayList<Transformer> transformers = new ArrayList<>();
                for(int i = 0; i < list.getModel().getSize(); i++){
                    String tran = String.valueOf(list.getModel().getElementAt(i));
                    Transformer transformer = Repo.getTransformerFromRepository(tran);
                    if(transformer != null){
                        transformers.add(transformer);
                        System.out.println(Repo.getUrl(tran, true) + " successfully loaded.");
                    }
                    else{
                        System.out.println(Repo.getUrl(tran, true) + " failed to load.");
                    }
                }
                DeobfuscatorHandler.handle(input, output, transformers);
            });
            thread.start();
        });

    }

    public void addElementToList(Object a){
        ((DefaultListModel)list.getModel()).addElement(a);
    }

    public void removeElementFromList(Object a){
        ((DefaultListModel)list.getModel()).removeElement(a);
    }



    public void openConsole(){
        JFrame jFrame = new JFrame();
        jFrame.setResizable(true);
        jFrame.setBounds(0, 0, 609, 1039);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setTitle("Console");
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setBounds(14, 14, 595, 1025);
        textArea.setBackground(new Color(40, 42, 54));
        textArea.setForeground(Color.white);
        textArea.setEditable(false);
        jFrame.getContentPane().add(textArea);
        textArea.setText("");
        ConsoleOutputStream consoleOutputStream = new ConsoleOutputStream(textArea);
        System.setOut(new PrintStream(consoleOutputStream));
        System.setErr(new PrintStream(consoleOutputStream));
    }

}

