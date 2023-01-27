package com.pufferenco;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class http {
   public static String get(String URL) throws IOException {
      URL url = new URL(URL);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      InputStream responseStream = connection.getInputStream();
      return new String(responseStream.readAllBytes());
   }
}
