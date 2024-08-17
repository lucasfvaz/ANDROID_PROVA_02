package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;

import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoLocacaoPOST extends IntentService {

    public ServicoLocacaoPOST() {
        super("ServicoLocacaoPOST");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String json = intent.getStringExtra("post_jsonlocacao");
                System.out.println(json);
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locacao");
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
                    response = "Locado com sucesso";
                }else{
                    response = con.getResponseMessage();
                }
                Intent respintent= new Intent("RESPOSTA_POSTLOCACACO");
                respintent.putExtra("resposta_postlocacao",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }

        }
    }


}