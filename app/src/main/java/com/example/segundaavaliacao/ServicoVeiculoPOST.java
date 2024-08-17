package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoVeiculoPOST extends IntentService {



    public ServicoVeiculoPOST() {
        super("ServicoVeiculoPOST");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String json = intent.getStringExtra("post_jsonveiculo");
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/veiculo");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String response = "";
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if(con.getResponseCode() == 201){
                    response = "Criado com sucesso";
                }
                if(con.getResponseCode() == 304){
                    response = "Esse Veiculo com essa Placa Ja foi cadastrado";
                }
                Intent respintent= new Intent("RESPOSTA_POST");
                respintent.putExtra("resposta_post",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }
        }
    }


}