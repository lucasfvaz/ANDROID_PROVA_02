package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;


public class ServicoModeloGET extends IntentService {


    LinkedList<Modelo> modelos;

    public ServicoModeloGET() {
        super("ServicoModeloGET");
    }





    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/modelo");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                if(con.getResponseCode() == 200){
                    String resp = "";
                    String linha;
                    BufferedReader  buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    do{
                        linha = buf.readLine();
                        if(linha != null){
                            resp += linha;
                        }
                    }while (linha != null);

                    Intent respintent= new Intent("RESPOSTA_MODELO");
                    respintent.putExtra("resposta_modelo",resp);
                    sendBroadcast(respintent);

                }else{
                    Intent respintent= new Intent("RESPOSTA_MODELO");
                    respintent.putExtra("resposta_modelo","conexaofalhou");
                    sendBroadcast(respintent);
                }
            }catch (Exception ex){

            }
        }
    }





}