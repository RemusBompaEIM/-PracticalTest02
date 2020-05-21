package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.Alarm;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (currency)");
            String request = bufferedReader.readLine();
            if (request == null || request.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (request)!");
                return;
            }

            HashMap<String, Alarm> data = serverThread.getData();
            String ip = socket.getInetAddress().toString();

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Received " + request);
            if(request.substring(0,3).equals("set")){
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Ovverwrite alarm...");
                }
                String[] vals = request.split(",");
                data.put(ip, new Alarm(vals[1], vals[2]));
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Put alarm " + vals[1] + "," + vals[2] + " for " + ip);
                printWriter.println("Alarm set");
                printWriter.flush();
            }else if(request.equals("reset")){
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Delete alarm...");
                    data.remove(ip);
                    printWriter.println("Alarm deleted");
                    printWriter.flush();
                }else{
                    printWriter.println("No alarm for this ip");
                    printWriter.flush();
                }
            }else if(request.equals("poll")){
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Verify if alarm is active...");
                    Socket new_socket = new Socket(Constants.WEB_SERVICE_ADDRESS, Constants.WEB_SERVICE_PORT);
                    BufferedReader new_bufferedReader = Utilities.getReader(new_socket);
                    String dayTimeProtocol = new_bufferedReader.readLine();
                    dayTimeProtocol = new_bufferedReader.readLine();
                    String [] formatted = dayTimeProtocol.split(" ");
                    formatted = formatted[2].split(":");
                    String hours = formatted[0];
                    String minutes = formatted[1];
                    Alarm my_alarm = data.get(ip);
                    Log.d(Constants.TAG, "[COMMUNICATION THREAD] The server returned: " + dayTimeProtocol);
                    if(Integer.parseInt(my_alarm.getHour()) < Integer.parseInt(hours)){
                        printWriter.println("Active");
                        printWriter.flush();
                    }else if(Integer.parseInt(my_alarm.getHour()) == Integer.parseInt(hours)){
                        if(Integer.parseInt(my_alarm.getMinute()) <= Integer.parseInt(minutes)){
                            printWriter.println("Active");
                            printWriter.flush();
                        }else{
                            printWriter.println("Inactive");
                            printWriter.flush();
                        }
                    }else{
                        printWriter.println("Inactive");
                        printWriter.flush();
                    }


                }else{
                    printWriter.println("None");
                    printWriter.flush();
                }
            }


        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }

        }
    }
}