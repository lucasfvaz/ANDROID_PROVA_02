package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceLocadorCEPGET extends IntentService {



    public ServiceLocadorCEPGET() {
        super("ServiceLocadorCEPGET");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
             try{
                 String cepLocador = intent.getStringExtra("cepLocador");
                 URL url = new URL("https://viacep.com.br/ws/" + cepLocador + "/json/");
                 HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                 if(con.getResponseCode() == 200){
                     String resp = "";
                     String linha;
                     BufferedReader buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                     do{
                         linha = buf.readLine();
                         if(linha != null){
                             resp += linha;
                         }
                     }while (linha != null);

                     Intent respintent= new Intent("RESPOSTA_CEP");
                     respintent.putExtra("resposta_cep",resp);
                     sendBroadcast(respintent);

                 }else{
                     Intent respintent= new Intent("RESPOSTA_CEP");
                     respintent.putExtra("resposta_cep","conexaofalhou");
                     sendBroadcast(respintent);
                 }
             }catch (Exception ex){

             }
        }
    }


}