package com.pufferenco;

import java.io.FileWriter;
import java.io.IOException;

public class TextIO {
   public static void WriteTxt(String file, String content){
      try(FileWriter myWriter = new FileWriter(file)) {
         myWriter.write(content);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
