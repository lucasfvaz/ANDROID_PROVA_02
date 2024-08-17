package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoLocadorDELETE extends IntentService {



    public ServicoLocadorDELETE() {
        super("ServicoLocadorDELETE");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String cpfID = intent.getStringExtra("cpfID");
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locador/" + cpfID);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setDoOutput(true);
                String response = "";

                if(con.getResponseCode() == 204){
                    response = "Excluído com sucesso";
                }else{
                    response = "Erro ao Excluir Locador tente novamente";
                }
                Intent respintent= new Intent("RESPOSTA_LOCADORDELETE");
                respintent.putExtra("resposta_locadordelete",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }
        }
    }


}