/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.text.html.HTML;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


/**
 *
 * @author Muntari
 */
public class Download {
    
    public void start(JRootPane root, String WebUrl, JTextPane textPane, JLabel txtIp, JLabel txtDomain){
        try {
            String webUrl = WebUrl;
            URL url = new URL(webUrl);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String host = url.getHost();
            InetAddress address = InetAddress.getByName(host);
            String ip = address.getHostAddress();
            txtIp.setText(ip);
            txtDomain.setText(host);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
            htmlKit.read(br, htmlDoc, 0);
            for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.IMG); iterator.isValid(); iterator.next()) {
                AttributeSet attributes = (AttributeSet) iterator.getAttributes();
                String imgSrc = (String) attributes.getAttribute(HTML.Attribute.SRC);

                if (imgSrc != null && (!imgSrc.contains("https:")) && (imgSrc.toLowerCase().endsWith(".jpg") || (imgSrc.endsWith(".png")) || (imgSrc.endsWith(".jpeg")) || (imgSrc.endsWith(".bmp")) || (imgSrc.endsWith(".ico")))) {
                   console(textPane, imgSrc);
                    try {
                        downloadImage(webUrl, imgSrc);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
            console(textPane, "Progress Done....");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
     private static void downloadImage(String url, String imgSrc) throws IOException {
        BufferedImage image = null;
        try {
            if (!(imgSrc.startsWith("http"))) {
                url = url + imgSrc;
            } else {
                url = imgSrc;
            }
            imgSrc = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
            String imageFormat = null;
            imageFormat = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
            String imgPath = null;
            imgPath = "C:/temp/" + imgSrc + "";
            URL imageUrl = new URL(url);
            image = ImageIO.read(imageUrl);
            if (image != null) {
                File file = new File(imgPath);
                ImageIO.write(image, imageFormat, file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void console(JTextPane textPanel, String message){
        String text = textPanel.getText();
        textPanel.setText(message+"...\n"+text);
    }
    
}
