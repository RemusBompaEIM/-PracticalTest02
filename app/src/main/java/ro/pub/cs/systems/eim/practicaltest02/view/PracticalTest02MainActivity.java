package ro.pub.cs.systems.eim.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {
    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private Button setButton = null;
    private Button pollButton = null;
    private Button resetButton = null;
    private EditText clientHours = null;
    private EditText clientMinute = null;
    private EditText portClient = null;
    private TextView weatherForecastTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port);
        connectButton = (Button)findViewById(R.id.Connect);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.adresa_client);
        portClient = (EditText)findViewById(R.id.port_client);
        setButton = (Button)findViewById(R.id.Set);
        resetButton = (Button)findViewById(R.id.Reset);
        pollButton = (Button)findViewById(R.id.Poll);
        clientHours= (EditText)findViewById(R.id.ora_client);
        clientMinute = (EditText)findViewById(R.id.minut_client);
        setButton.setOnClickListener(setButtonClickListener);
        resetButton.setOnClickListener(resetButtonClickListener);
        pollButton.setOnClickListener(pollButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.view_results);
    }

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }

    }

    private SetButtonClickListener setButtonClickListener = new SetButtonClickListener();
    private class SetButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = portClient.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String hours = clientHours.getText().toString();
            String minutes = clientMinute.getText().toString();
            if (hours == null || hours.isEmpty() || minutes == null || minutes.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (hours / minutes type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }
            weatherForecastTextView.setText(Constants.EMPTY_STRING);

            String request = "set," + hours + "," + minutes + "\n";
            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), request, weatherForecastTextView
            );
            clientThread.start();
        }
    }

    private ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();
    private class ResetButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = portClient.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            weatherForecastTextView.setText(Constants.EMPTY_STRING);

            String request = "reset\n";
            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), request, weatherForecastTextView
            );
            clientThread.start();
        }
    }

    private PollButtonClickListener pollButtonClickListener = new PollButtonClickListener();
    private class PollButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = portClient.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            weatherForecastTextView.setText(Constants.EMPTY_STRING);

            String request = "poll\n";
            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), request, weatherForecastTextView
            );
            clientThread.start();
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }

        super.onDestroy();
    }
}
