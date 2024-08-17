package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoLocacaoDELETE extends IntentService {

    public ServicoLocacaoDELETE() {
        super("ServicoLocacaoDELETE");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try{
                String locacaoID = intent.getStringExtra("locacaoID");
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locacao/" + locacaoID);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setDoOutput(true);
                String response = "";

                if(con.getResponseCode() == 204){
                    response = "Excluído com sucesso";
                }else{
                    response = "Erro ao Excluir Locacao tente novamente";
                }
                Intent respintent= new Intent("RESPOSTA_DELETE");
                respintent.putExtra("resposta_delete",  response);
                sendBroadcast(respintent);

            }catch (Exception EX){
                EX.printStackTrace();
            }
        }
    }


}