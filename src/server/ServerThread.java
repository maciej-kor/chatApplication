package server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerThread extends Thread {

    private Server server;
    private Socket clientSocket;
    private String login;

    private final String LOGIN_COMMAND = "login";
    private final String LOGOUT_COMMAND = "logout";
    private final String QUIT_COMMAND = "quit";
    private final String MESSAGE_COMMAND = "msg";

    public ServerThread(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        System.out.println("sockeet: " + clientSocket);
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {

        Scanner scanner = new Scanner(clientSocket.getInputStream());

        String line;

        while (clientSocket.getInputStream() != null) {

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                System.out.println(line);

                String[] tokens = line.split(" ", 3);
                System.out.println(Arrays.toString(tokens));

                if (tokens.length > 0) {

                    String cmd = tokens[0];

                    if (cmd.equals(QUIT_COMMAND) || cmd.equals(LOGOUT_COMMAND)) {
                        handleLogOff();
                        return;
                    } else if (cmd.equals(LOGIN_COMMAND)) {
                        handleLogin(tokens);
                    } else if (cmd.equals(MESSAGE_COMMAND)) {
                        handleMessage(tokens);
                    } else if (cmd.equals("getAllUsers")) {
                        handleGetUsers();
                    } else {
                        String msg = "unknown " + cmd + "\n";
                        sendMessage(msg);
                    }
                }
            }

        }
        clientSocket.close();
    }

    private void handleGetUsers() throws IOException {
        StringBuilder msg = new StringBuilder();
        List<ServerThread> workerList = server.getServerList();

        for (String s : server.getUserMap().keySet()) {
            msg.append("list ");
            msg.append(s);
            msg.append('\n');
        }

        msg.append("endlist");
        msg.append('\n');
        sendMessage(msg.toString());
        msg.setLength(0);

        for (ServerThread serverThread : workerList) {
            if (serverThread.getLogin() != null) {
                msg.append("online ");
                msg.append(serverThread.getLogin());
                msg.append('\n');
            }
        }

        msg.append("endonline");
        msg.append('\n');
        sendMessage(msg.toString());

    }

    private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];

        List<ServerThread> serverThreadList = server.getServerList();

        for (ServerThread serverThread : serverThreadList) {
            if (sendTo.equals(serverThread.getLogin())) {
                String outMsg = "msg " + login + " " + body + "\n";
                serverThread.sendMessage(outMsg);
            }
        }
    }

    private void handleLogin(String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            if (server.checkUserData(login, password)) {
                String msg = "ok login\n";
                sendMessage(msg);
                this.login = login;

                String onlineMsg = "online " + login + "\n";

                List<ServerThread> workerList = server.getServerList();

                for (ServerThread serverThread : workerList) {
                    serverThread.sendMessage(onlineMsg);
                }

            } else {
                String msg = "error login\n";
                sendMessage(msg);
            }

        }
    }

    private void handleLogOff() throws IOException {
        List<ServerThread> workerList = server.getServerList();
        String onlineMsg = "offline " + login + "\n";
        for (ServerThread serverThread : workerList) {
            if (!login.equals(serverThread.getLogin())) {
                serverThread.sendMessage(onlineMsg);
            }
        }
        server.removeServerThread(this);
        clientSocket.getOutputStream().close();
        clientSocket.close();
    }

    private void sendMessage(String message) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        System.out.println(message);
        printWriter.print(message);
        printWriter.flush();
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
