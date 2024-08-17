package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoLocacaoPUT extends IntentService {


    public ServicoLocacaoPUT() {
        super("ServicoLocacaoPUT");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String json = intent.getStringExtra("put_jsonlocacao");
                Integer locacaoID = Integer.parseInt(intent.getStringExtra("locacaoID"));
                System.out.println(locacaoID);
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locacao/" + locacaoID);
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

                if(con.getResponseCode() == 200){
                    response = "Locacao atualizada com sucesso";

                }
                if(con.getResponseCode() == 304){
                    response = "Erro ao atualizar locacao";
                }
                Intent respintent= new Intent("RESPOSTA_PUT");
                respintent.putExtra("resposta_put",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }
        }
    }

}