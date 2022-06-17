package com.nort721.transmitter.cserver;

import com.nort721.transmitter.utils.CompressionUtil;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter
public class ComputationServerSender extends Thread {

    private final String computation_server_address;
    private final int computation_server_port;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ComputationServerSender(String computation_server_address, int computation_server_port) {
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

    private void startCommunication() throws IOException {}

    public void sendDataToServer(String data) {
        try {

            socket = new Socket(computation_server_address, computation_server_port);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(socket.getOutputStream(), true);

            output.println(CompressionUtil.encode(data));

        } catch (IOException e) {
            e.printStackTrace();
        }
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
