package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatClient {

    private String serverName;
    private int serverPort;
    private Socket socket;
    private List<UserStatusListener> userStatusListeners = new ArrayList<>();
    private List<MessageListener> messageListeners = new ArrayList<>();
    private final String LOGIN_SUCCESSFUL = "ok login";

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;

        try {
            wykonanieMetody();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void wykonanieMetody() throws IOException {
        this.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });

        this.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("Message from " + fromLogin + " : " + msgBody);
            }
        });

        if (!this.connect()) {
            System.err.println("Connection failed");
        } else {
            System.out.println("Connection successful");
            if (this.login("Maciek", "maciek")) {
                System.out.println("Login successful");
                //this.msg("am", "siema");
            } else {
                System.err.println("Login failed");
            }
            //client.logOff();
        }
    }

    private void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody;
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(cmd);
        printWriter.flush();
    }

    private void logOut() throws IOException {
        String cmd = "logout";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(cmd);
        printWriter.flush();
    }

    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(cmd);
        printWriter.flush();
        Scanner scannerResponse = new Scanner(socket.getInputStream());
        String response = scannerResponse.nextLine();
        System.out.println(response);

        if (response.equals(LOGIN_SUCCESSFUL)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    private void startMessageWriter() {
        Thread writeMessageThread = new Thread(() -> writeMessageLoop());
        writeMessageThread.start();
    }

    private void writeMessageLoop() {

    }

    private void startMessageReader() {
        Thread readMessageThread = new Thread(() -> readMessageLoop());
        readMessageThread.start();
    }

    private void readMessageLoop() {
        String line;
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (socket.getInputStream() != null) {
                line = scanner.nextLine();
                String[] tokens = line.split(" ", 3);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if (cmd.equals("online")) {
                        handleOnline(tokens);
                    } else if (cmd.equals("offline")) {
                        handleOffline(tokens);
                    } else if (cmd.equals("msg")) {
                        handleMessage(tokens);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void handleMessage(String[] tokens) {
        String login = tokens[1];
        String body = tokens[2];

        for (MessageListener messageListener : messageListeners) {
            messageListener.onMessage(login, body);
        }

    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for (UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for (UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    private boolean connect() {

        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("client port is: " + socket.getLocalPort());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean logowanie(String login, String password) throws IOException {

        if (this.connect()) {

            System.out.println("Connection successful");

            String cmd = "login " + login + " " + password + "\n";
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.print(cmd);
            printWriter.flush();

            Scanner scannerResponse = new Scanner(socket.getInputStream());
            String response = scannerResponse.nextLine();
            System.out.println(response);

            if (response.equals(LOGIN_SUCCESSFUL)) {
                System.out.println("Login successful");
                startMessageReader();
                return true;
            } else {
                return false;
            }
        } else {
            System.err.println("Connection failed");
            return false;
        }
    }

    public void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public List<String> getAllUsers() throws IOException {
        String cmd = "getAllUsers";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(cmd);
        printWriter.flush();

        List<String> usersList = new ArrayList<>();
        Scanner scannerResponse = new Scanner(socket.getInputStream());

        while(scannerResponse.hasNextLine()){
            String line = scannerResponse.nextLine();
            String[] tokens = line.split(" ");

            if(tokens[0].equals("list")){
                usersList.add(tokens[1]);
            }
        }
        return usersList;
    }
}
