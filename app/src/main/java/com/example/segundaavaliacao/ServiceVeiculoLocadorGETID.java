package com.example.segundaavaliacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ServiceVeiculoLocadorGETID extends IntentService {


    public ServiceVeiculoLocadorGETID() {
        super("ServiceVeiculoLocadorGETID");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                String cpfID = intent.getStringExtra("cpfID");
                URL url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/locador/" + cpfID);
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

                    Intent respintent= new Intent("RESPOSTA_VEICULOLOCADOR");
                    respintent.putExtra("resposta_locador",resp);
                    try {
                        String placaID = intent.getStringExtra("placaID");
                        url = new URL("https://argo.td.utfpr.edu.br/locadora-war/ws/veiculo/" + placaID);
                        con = (HttpsURLConnection) url.openConnection();
                        resp = "";
                        linha = "";
                        if(con.getResponseCode() == 200){
                            buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            do{
                                linha = buf.readLine();
                                if(linha != null){
                                    resp += linha;
                                }
                            }while (linha != null);

                            respintent.putExtra("resposta_veiculo",resp);
                            sendBroadcast(respintent);

                        }else{
                            respintent.putExtra("resposta_veiculo","conexaofalhou");
                            sendBroadcast(respintent);
                        }
                    }catch (Exception ex){

                    }
                }else{
                    Intent respintent= new Intent("RESPOSTA_VEICULOLOCADOR");
                    respintent.putExtra("resposta_locador","conexaofalhou");
                    sendBroadcast(respintent);
                }
            }catch (Exception ex){

            }
        }
    }


}