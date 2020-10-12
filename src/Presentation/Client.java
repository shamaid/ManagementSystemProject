package Presentation;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import org.controlsfx.control.Notifications;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashSet;


public class Client  {

    private Socket socket;
    private Socket notificactionsSocket;
    private final int notificationsIterval = 2000;
    private Timer notificationsTimer;

    private boolean receiveNotifications = false;
    private final Object mutexObject = new Object();
    private DataInputStream not_dis;
    private DataOutputStream not_dos;


    public Client (int serverPort)
    {

        try
        {
            socket = new Socket("132.72.65.47", serverPort);
            notificactionsSocket = new Socket("132.72.65.47", serverPort + 1);
            notificationsTimer = new Timer();


            not_dis = new DataInputStream(notificactionsSocket.getInputStream());
            not_dos = new DataOutputStream(notificactionsSocket.getOutputStream());


        }
        catch (Exception e)
        {
           sendNotification("Can't connect to server, please try again later");
           ClientMain.getStage().close();
        }

    }

    public void closeSockets(){
        try {
            socket.close();
            notificactionsSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startNotifications(String uid)
    {

        receiveNotifications = true;

        try
        {

            not_dos.writeBytes(uid + "\n");
            not_dos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                listenForNotifications();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

/*
        notificationsTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                checkNotifications(uid);

            }
        }, notificationsIterval, notificationsIterval);
*/

    }

    public void listenForNotifications()
    {

        while (receiveNotifications) {
            try {

                BufferedReader rd = new BufferedReader(new InputStreamReader(not_dis));
                String lineReceived = rd.readLine();
                sendNotification(lineReceived);
                // liat
                // lineReceived contains the notification

            }
            catch (SocketException se)
            {
            }

            catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void sendNotification(String lineReceived) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Notification");
            alert.setContentText(lineReceived);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("AppStyle.css").toExternalForm());
            alert.show();
        });
    }


    public void stopNotifications()
    {

        receiveNotifications = false;
        //notificationsTimer.cancel();
    }

	

    public List<String> sendToServer(String stringToSend)
    {
        List<String> res = new LinkedList<>();

        try {

            synchronized (mutexObject)
            {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Client sent to server " + stringToSend.replace("\n", ""));

                if (stringToSend.length() == 0 || stringToSend.charAt(stringToSend.length()-1) != '\n')
                    stringToSend = stringToSend + "\n";

                outputStream.writeBytes(stringToSend);
                outputStream.flush();

                res = createListFromServerString(receiveFromServer());
            }


        }
        catch (SocketException se)
        {
            sendNotification("server was terminated ... :(");
            // liat
            // need to throw here alert that server was terminated
            // now throws error when trying to login when server closes when user is active
        }

        catch (Exception e) {

            e.printStackTrace();
        }

        
        return res;
    }

    public String receiveFromServer()
    {
        String res = "-1";

        try
        {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(inputStream));

            res = clientReader.readLine();

            System.out.println("Client receive from server : " + res + "\n");


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;

    }

    private List<String> createListFromServerString(String ans)
    {
        List<String> res = new LinkedList<>();

        String[] split = ans.split("~");

        for (String s : split)
            res.add(s);

        return res;
    }

    public String ListToString(List<String> list)
    {

        String res = "";

        if (list.size() == 0)
            return res;

        for (String s : list)
            res = res + s + "~";

        res = res.substring(0, res.length()-1);

        return res;
    }


}

