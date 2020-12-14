package edu.courseproject.server;

import edu.courseproject.server.serverthread.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Launcher {
    private final static String PORT_REGEX = "\\d{4}";
    private static int port = 2525;

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].matches(PORT_REGEX)) {
                port = Integer.valueOf(args[0]);
            }
        }
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                System.out.println("client: " + socket.getInetAddress().getHostAddress() + " port: " + port + " connected");
                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
