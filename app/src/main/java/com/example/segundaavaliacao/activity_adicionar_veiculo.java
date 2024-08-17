package com.example.segundaavaliacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class activity_adicionar_veiculo extends AppCompatActivity {
     Spinner modeloSelecionado;
     TextView valorModelo,marcaModelo;
     EditText anoVeiculo,corVeiculo, placaVeiculo;

    Veiculo v;

    String placaIdUpdate;
    ArrayAdapter<Modelo> adapter;

    Button addveiculo;
    class OuvinteModelo extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
             String respjson = intent.getStringExtra("resposta_modelo");
            Gson gson = new Gson();
            Modelo[] modelos = gson.fromJson(respjson, Modelo[].class);
            Spinner dropdown = findViewById(R.id.spinnerCategoriasmodelo);


            if(modelos != null) {
                adapter = new ArrayAdapter<>(activity_adicionar_veiculo.this, android.R.layout.simple_spinner_dropdown_item, modelos);
                dropdown.setAdapter(adapter);
                if (v != null) {
                    int selectedPosition = -1;


                    for (int i = 0; i < modelos.length; i++) {
                        if (modelos[i].getId().equals(v.getIdModelo())) {
                            selectedPosition = i;
                            break;
                        }
                    }


                    if (selectedPosition != -1) {
                        dropdown.setSelection(selectedPosition);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            modeloSelecionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    Modelo itemSelecionado = (Modelo) parentView.getSelectedItem();


                    valorModelo.setText("Valor da Diaria: " + Double.toString(itemSelecionado.getValorDiaria()));
                    marcaModelo.setText("Marca: " + itemSelecionado.getMarca().getNome());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });
        }
    }
    class OuvinteVeiculoAdd extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response= intent.getStringExtra("resposta_post");

            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


        }
    }
    class OuvinteVeiculoUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resp = intent.getStringExtra("resposta_put");

            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_veiculo);
        modeloSelecionado = (Spinner) findViewById(R.id.spinnerCategoriasmodelo);
        valorModelo = (TextView) findViewById(R.id.valormodelo);
        marcaModelo = (TextView) findViewById(R.id.marcaModelo);
        Spinner dropdown = findViewById(R.id.spinnerCategoriasmodelo);
        anoVeiculo = (EditText) findViewById(R.id.anoveiculo);
        corVeiculo = (EditText) findViewById(R.id.corveiculo);
        placaVeiculo = (EditText) findViewById(R.id.placaveiculo);
        addveiculo = (Button) findViewById(R.id.buttonaddveiculo);
        v = (Veiculo) getIntent().getSerializableExtra("veiculoEdicao");


        if(v != null){
            anoVeiculo.setText(Integer.toString(v.getAno()));
            corVeiculo.setText(v.getCor());
            placaVeiculo.setText(v.getPlaca());
            placaVeiculo.setEnabled(false);
            placaIdUpdate = v.getPlaca();
            addveiculo.setText("Atualizar Veiculo");
        }
        registerReceiver(new OuvinteModelo(), new IntentFilter("RESPOSTA_MODELO"));
        Intent it = new Intent(this, ServicoModeloGET.class);
        startService(it);
    }

    public void adicionarVeiculo(View vi){
        try {
            Modelo modelo = (Modelo) modeloSelecionado.getSelectedItem();
            Integer ano = Integer.parseInt(anoVeiculo.getText().toString());
            String cor = corVeiculo.getText().toString();
            String placa = placaVeiculo.getText().toString();
            Integer idModelo = modelo.getId();

            Veiculo veiculo = new Veiculo(ano, cor, idModelo, placa);
            Gson gson = new Gson();

            String json = gson.toJson(veiculo);

            if (v != null) {
                Intent it = new Intent(this, ServicoVeiculoPUT.class);
                it.putExtra("placaID",placaIdUpdate);
                it.putExtra("put_jsonveiculo", json);
                registerReceiver(new OuvinteVeiculoUpdate(), new IntentFilter("RESPOSTA_PUT"));
                startService(it);
                finish();
            } else {
                Intent it = new Intent(this, ServicoVeiculoPOST.class);
                it.putExtra("post_jsonveiculo", json);
                registerReceiver(new OuvinteVeiculoAdd(), new IntentFilter("RESPOSTA_POST"));
                startService(it);
                finish();
            }
        }catch (Exception EX){
              EX.printStackTrace();
        }
    }


}