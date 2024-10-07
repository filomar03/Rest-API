package it.unimib.sd2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DbConnection {
    private static final int PORT = 3030;
    private static final String ADDR = "localhost";

    public static String performQuery(String query) throws IOException {
        try (Socket socket = new Socket(ADDR, PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            out.println(query);
            return in.readLine();
        }
    }
}
