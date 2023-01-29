package com.pufferenco;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class TextIO {
   public static void WriteTxt(String file, String content) {
      try (FileWriter myWriter = new FileWriter(file)) {
         myWriter.write(content);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void AppendTxt(String file, String line) {
      try {
         Files.write(Paths.get(file), line.getBytes(), StandardOpenOption.APPEND);
      } catch (IOException e) {
         //exception handling left as an exercise for the reader
      }
   }
}
