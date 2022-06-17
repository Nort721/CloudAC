package com.nort721.transmitter.cserver;

import com.nort721.transmitter.ACTransmitter;
import com.nort721.transmitter.utils.CompressionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ComputationServerListener extends Thread {

    private static final int PORT = 1212;

    @Override
    public void run() {
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {

            try {

                Socket socket = serverSocket.accept();

                ReplyHandler replyHandler = new ReplyHandler(socket);

                replyHandler.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ReplyHandler extends Thread {

        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public ReplyHandler(Socket socket) {
            this.socket = socket;

            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));;
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("Error creating streams: " + e.getMessage());
            }

            //System.out.println(" -> established secure connection with " + socket.getInetAddress().getHostAddress());
        }

        @Override
        public void run() {
            try {

                String data = CompressionUtil.decode(input.readLine());
                System.out.println(socket.getInetAddress().getHostName() + " -> " + data);

                String[] args = getArgs(data, '|');

                if (args[0].equalsIgnoreCase("FLAG")) {
                    kickPlayerSafely(args[1], args[2]);
                }

                // close everything when were done
                output.close();
                input.close();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void kickPlayerSafely(String uuid, String data) {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));

            if (player == null) return;

            Bukkit.getServer().getScheduler().runTask(ACTransmitter.getInstance(), new Runnable() {
                public void run() {
                    player.kickPlayer("KICKED FOR CHEATING | " + data);
                }
            });
        }

        private String[] getArgs(String input, char separator) {

            ArrayList<String> args = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            for (char c : input.toCharArray()) {

                if (c == separator) {
                    args.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }

            }

            args.add(sb.toString());

            String[] arr = new String[args.size()];
            args.toArray(arr);

            return arr;
        }
    }

}
