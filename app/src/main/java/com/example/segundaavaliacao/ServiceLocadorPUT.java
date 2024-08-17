package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServiceLocadorPUT extends IntentService {



    public ServiceLocadorPUT() {
        super("ServiceLocadorPUT");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String json = intent.getStringExtra("put_jsonlocador");
                String cpfID = intent.getStringExtra("cpfID");
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locador/" + cpfID);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String response = "";
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if(con.getResponseCode() == 204){
                    response = "Locador atualizado com sucesso";

                }else {
                    response = "ERRO";
                }
                if(con.getResponseCode() == 304){
                    response = "Esse Veiculo com essa Placa Ja foi cadastrado";
                }
                Intent respintent= new Intent("RESPOSTA_LOCADORPUT");
                respintent.putExtra("resposta_locadorput",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }
        }
    }


}