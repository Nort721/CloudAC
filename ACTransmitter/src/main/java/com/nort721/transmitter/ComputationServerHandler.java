package com.nort721.transmitter;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter
public class ComputationServerHandler extends Thread {

    private final String computation_server_address;
    private final int computation_server_port;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ComputationServerHandler(String computation_server_address, int computation_server_port) {
        this.computation_server_address = computation_server_address;
        this.computation_server_port = computation_server_port;
    }

    @Override
    public void run() {
        try {
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCommunication() throws IOException {
        socket = new Socket(computation_server_address, computation_server_port);

        // data received from license server
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // used to send data to license server
        output = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            processServerReply(input.readLine());
        }
    }

    public void sendDataToServer(String data) {
        output.println(data);
    }

    private void processServerReply(String msg) {
        System.out.println("got message from server -> " + msg);
    }

    public void closeConnection() {
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
