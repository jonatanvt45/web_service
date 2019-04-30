package com.example.webservice;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView lv_contacts_list;
    private ArrayAdapter adapter;
    private String getAllContactsURL = "http://130.100.0.1:8080/agenda/getAllContacts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        lv_contacts_list = (ListView)findViewById(R.id.lv_contacts_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv_contacts_list.setAdapter(adapter);
        webServiceRest(getAllContactsURL);
    }
    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.e("abrir conexion",connection.toString());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String id_cliente;
        String nombre_cliente;
        String apellido_paterno_cliente;
        String apellido_materno_cliente;
        String email_cliente;
        String telefono_cliente;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            e.printStackTrace();
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id_cliente = jsonObject.getString("id_cliente");
                nombre_cliente = jsonObject.getString("nombre_cliente");
                apellido_paterno_cliente = jsonObject.getString("apellido_paterno_cliente");
                apellido_materno_cliente = jsonObject.getString("apellido_materno_cliente");
                email_cliente = jsonObject.getString("email_cliente");
                telefono_cliente = jsonObject.getString("telefono_cliente");
                adapter.add(id_cliente + ": " + nombre_cliente + ": " + apellido_paterno_cliente + ": " +
                        apellido_materno_cliente + ": " + email_cliente + ": " + telefono_cliente);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}