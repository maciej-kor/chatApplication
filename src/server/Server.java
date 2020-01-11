package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends Thread{

    private int serverPort;

    private List<ServerThread> serverList = new ArrayList<>();

    private static Map<String, String> userMap = new HashMap<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
        fillUserMap();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("Wait for a client");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accept connection from " + clientSocket);
                ServerThread serverThread = new ServerThread(this, clientSocket);
                serverThread.start();
                serverList.add(serverThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillUserMap(){

        userMap.put("Maciek", "maciek");
        userMap.put("grzesiek", "grzesiek1");
        userMap.put("johny", "John");
        userMap.put("ma", "ma");

    }

    public boolean checkUserData(String nickName, String password){

        if (userMap.get(nickName) != null && userMap.get(nickName).equals(password))
            return true;
        else
            return false;

    }

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    public List<ServerThread> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerThread> serverList) {
        this.serverList = serverList;
    }

    public void removeServerThread(ServerThread serverThread) {
        serverList.remove(serverThread);
    }
}
