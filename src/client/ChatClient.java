package client;

import client.view.ChatPanel;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatClient {

    private String serverName;
    private int serverPort;
    private Socket socket;
    private List<UserStatusListener> userStatusListeners = new ArrayList<>();
    private List<MessageListener> messageListeners = new ArrayList<>();
    private final String LOGIN_SUCCESSFUL = "ok login";
    private Controller controller;
    private String nickName;
    private StringBuffer messageToSend = new StringBuffer();

    private Map<String, Boolean> userListAndOnlineStatus = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    });


    public ChatClient(String serverName, int serverPort, Controller controller) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.controller = controller;
    }

    void msg(String sendTo, String msgBody) {
        String cmd = "msg " + sendTo + " " + msgBody;
        getMessageToSend().append(cmd);
    }

    public void logOut() throws IOException {
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

        while (socket.getOutputStream() != null) {

            Thread.sleep(1000);

            if (getMessageToSend().length() != 0) {
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
                String[] tokens = line.split(" ", 3);

                if (tokens.length > 0) {
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
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleMessage(String[] tokens) {
        String login = tokens[1];
        String body = tokens[2];

        controller.odbierzWiadomosc(login, body);

        for (MessageListener messageListener : messageListeners) {
            messageListener.onMessage(login, body);
        }

    }

    private void handleOffline(String[] tokens) {

        userListAndOnlineStatus.replace(tokens[1], false);
        controller.aktualizujStatusLogowania();

    }

    private void handleOnline(String[] tokens) {

        userListAndOnlineStatus.replace(tokens[1], true);
        controller.aktualizujStatusLogowania();

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
                this.nickName = nickName;

                getAllUsers();

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

    public boolean getAllUsers() throws IOException {

        String cmd = "getAllUsers";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(cmd);
        printWriter.flush();

        Scanner scannerResponse = new Scanner(socket.getInputStream());

        while (scannerResponse.hasNextLine()) {

            String line = scannerResponse.nextLine();
            String[] tokens = line.split(" ");

            if (tokens[0].equals("list")) {
                userListAndOnlineStatus.put(tokens[1], false);
            } else if (tokens[0].equals("online")) {
                userListAndOnlineStatus.replace(tokens[1], true);
            } else if (tokens[0].equals("endlist")) {
                userListAndOnlineStatus.remove(nickName);
            } else if (tokens[0].equals("endonline")) {
                System.out.println("endonline" + userListAndOnlineStatus);
                return true;
            }
        }
        return true;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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


    public Map<String, Boolean> getUserListAndOnlineStatus() {
        return userListAndOnlineStatus;
    }

    public void setUserListAndOnlineStatus(Map<String, Boolean> userListAndOnlineStatus) {
        this.userListAndOnlineStatus = userListAndOnlineStatus;
    }

}
