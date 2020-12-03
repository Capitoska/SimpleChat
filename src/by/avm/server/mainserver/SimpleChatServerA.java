package by.avm.server.mainserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SimpleChatServerA {
    ArrayList clientOutputStream;

    public static void main(String[] args) {
        SimpleChatServerA simpleChatServerA = new SimpleChatServerA();
        simpleChatServerA.go();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.nextInt() == 1) {
                //              simpleChatServerA.tellEveryoneIP();
            }
        }
    }

    //   public void tellEveryoneIP(){
//        Iterator it = clientIPAdress.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next() + "\n");
//        }
//    }
    @SuppressWarnings({"ResultOfMethodCallIgnored", "StatementWithEmptyBody"})
    public void tellEveryone(String message) {
        Iterator it = clientOutputStream.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void go() {
        clientOutputStream = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(9990);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStream.add(writer);
                System.out.println(clientSocket.getInetAddress().getHostAddress());
                //clientIPAdress.add((clientSocket.getInetAddress().getHostAddress()) + "");
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //    ArrayList<String> clientIPAdress;
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read" + message);
                    tellEveryone(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
