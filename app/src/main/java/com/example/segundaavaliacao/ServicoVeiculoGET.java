package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServicoVeiculoGET extends IntentService {




    public ServicoVeiculoGET() {
        super("ServicoVeiculoGET");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/veiculo");
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

                    Intent respintent= new Intent("RESPOSTA_VEICULO");
                    respintent.putExtra("resposta_veiculo",resp);
                    sendBroadcast(respintent);

                }else{
                    Intent respintent= new Intent("RESPOSTA_VEICULO");
                    respintent.putExtra("resposta_veiculo","conexaofalhou");
                    sendBroadcast(respintent);
                }
            }catch (Exception ex){

            }
        }
    }



}