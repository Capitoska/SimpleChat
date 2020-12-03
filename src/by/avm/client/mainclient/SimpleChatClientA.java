package by.avm.client.mainclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleChatClientA {
    JTextField outgoing, name;
    PrintWriter writer;
    Socket sock;
    JTextArea incoming;
    BufferedReader reader;
    JLabel label;

    public static void main(String[] args) {
        SimpleChatClientA simpleChatClientA = new SimpleChatClientA();
        simpleChatClientA.go();

    }

    public void go() {
        JFrame frame = new JFrame("Simple Chat Client");
        JPanel mainPanel = new JPanel();
        JPanel names = new JPanel();
        JPanel text = new JPanel();
        name = new JTextField(20);
        label = new JLabel("Names");
        names.add(name);
        names.add(label);
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendButtonListener());
        text.add(outgoing);
        text.add(sendButton);
        mainPanel.add(incoming);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.getContentPane().add(BorderLayout.NORTH, text);
        frame.getContentPane().add(BorderLayout.SOUTH, names);
        frame.setSize(550, 400);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("0.0.0.0", 9990);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class sendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                writer.println(name.getText() + ": " + outgoing.getText());
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }
}
