package util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class NoMaker extends JFrame {
    private JTextArea prg;
    public NoMaker() {
        super("É½¶«½¨Öþ´óÑ§¹ÜÀíÑ§Ôº ÐÅÏ¢¹ÜÀí");
        JPanel textPanel = new JPanel();
        JLabel messageLabel = new JLabel("Çë½«Ô´³ÌÐòÕ³Ìùµ½ÏÂÃæµÄÎÄ±¾¿òÖÐ£¬È»ºóµã»÷ÏÂÃæµÄ°´Å¥");
        textPanel.add(messageLabel);
        prg = new JTextArea(23,45);
        prg.setText("");
        JScrollPane jsPane =new JScrollPane(prg);
        textPanel.add(jsPane);

        this.add(textPanel,BorderLayout.CENTER);



        JPanel controlPanel = new JPanel();
        JButton addNum = new JButton("½«¼ÓÐÐºÅµÄ³ÌÐò¸´ÖÆµ½¼ôÇÐ°å");
        controlPanel.add(addNum);
        this.add(controlPanel,BorderLayout.SOUTH);
        {
            addNum.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent arg0) {
                    // TODO Auto-generated method stub
//					System.out.println("prg:"+prg.getText());
                    String content = prg.getText();
//					content.replace("\t", "    ");
                    String[] contentArray = content.split("\n");
                    int len = contentArray.length;
                    //how many lines
                    int digitNum = 0;
                    while(len!=0){
                        len = len / 10;
                        digitNum++;
                    }

                    StringBuilder all = new StringBuilder();
                    String format = "%0"+digitNum+"d";
                    int num = 1;
                    for (String line : contentArray) {
                        all.append(String.format(format,num) + " "+line+"\n");
                        num++;
                    }
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable trandata = new StringSelection(all.toString());
                    clipboard.setContents(trandata, null);

                }

            });
        }
        this.setSize(600,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }
    public static void main(String[] args){
        new NoMaker();
    }


}

