package com.scfg.core.common.util;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

public class ConvertBase64ToPdf {
    public static void convertBase64toPdfFile(String path,String base64){
        byte [] data= DatatypeConverter.parseBase64Binary(base64);
        File file=new File(path);
        try (OutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file))){
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
