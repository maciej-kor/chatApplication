package client;

import client.view.ChatPanel;

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

    private List<String> userList = new ArrayList<>();
    private List<String> onlineList = new ArrayList<>();

    private String userName;


    private StringBuffer messageToSend = new StringBuffer();


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
    }

    public void msg(String sendTo, String msgBody) {
        String cmd = "msg " + sendTo + " " + msgBody;
        getMessageToSend().append(cmd);
    }

    private void logOut() throws IOException {
        String cmd = "logout";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(cmd);
        printWriter.flush();
    }


    private void startMessageWriter() {
        Thread writeMessageThread = new Thread(() -> {
            try {
                writeMessageLoop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        writeMessageThread.start();
    }

    private void writeMessageLoop() throws IOException, InterruptedException {

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

        while (socket.getOutputStream() != null){

            Thread.sleep(1000);

            if (getMessageToSend().length() != 0){
                printWriter.println(getMessageToSend().toString());
                printWriter.flush();
                removeMessageToSend();
            }

        }

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
                String[] tokens = line.split(" ", 4);
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

        ChatPanel.receiveMessage(body);

        for (MessageListener messageListener : messageListeners) {
            messageListener.onMessage(login, body);
        }

    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        onlineList.remove(login);

        for (UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        onlineList.add(login);

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

    public boolean login(String nickName, String password) throws IOException {

        if (this.connect()) {

            System.out.println("Connection successful");

            String cmd = "login " + nickName + " " + password + "\n";
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.print(cmd);
            printWriter.flush();

            Scanner scannerResponse = new Scanner(socket.getInputStream());
            String response = scannerResponse.nextLine();
            System.out.println(response);

            if (response.equals(LOGIN_SUCCESSFUL)) {
                System.out.println("Login successful");
                this.userName = nickName;
                userList = getAllUsers();

                startMessageWriter();
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
        printWriter.println(cmd);
        printWriter.flush();

        List<String> usersList = new ArrayList<>();
        Scanner scannerResponse = new Scanner(socket.getInputStream());

        while (scannerResponse.hasNextLine()) {

            String line = scannerResponse.nextLine();
            String[] tokens = line.split(" ");

            if (tokens[0].equals("list")) {
                System.out.println(tokens[1]);
                usersList.add(tokens[1]);
            } else if (tokens[0].equals("end")) {
                usersList.remove(userName);
                return usersList;
            }
        }
        return usersList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public StringBuffer getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(StringBuffer messageToSend) {
        this.messageToSend = messageToSend;
    }

    public void removeMessageToSend() {
        this.messageToSend.setLength(0);
    }

    public List<String> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(List<String> onlineList) {
        this.onlineList = onlineList;
    }
}
