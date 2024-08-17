package com.example.segundaavaliacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class activity_adicionar_locador extends AppCompatActivity {
    EditText nome,email,cpf,telefone,cep,localidade,logradouro,uf,complemento,numero;

   Locador loc;


    String cpfIdUpdate;
   Button addLocador;

    class OuvinteLocadorCep extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response= intent.getStringExtra("resposta_cep");

            if(response.equals("conexaofalhou")) {
                Toast.makeText(getApplicationContext(), "Cep inválido", Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                CepLocador cepLocador = gson.fromJson(response, CepLocador.class);
                localidade = (EditText) findViewById(R.id.localidadelocador);
                logradouro = (EditText) findViewById(R.id.logradourolocador);
                uf = (EditText) findViewById(R.id.uflocador);
                complemento = (EditText) findViewById(R.id.complementolocador);

                localidade.setText(cepLocador.getLocalidade());
                logradouro.setText(cepLocador.getLogradouro());
                uf.setText(cepLocador.getUf());
                complemento.setText(cepLocador.getComplemento());
            }

        }
    }

    class OuvinteLocadorAdd extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response= intent.getStringExtra("resposta_postlocador");

            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


        }
    }

    class OuvinteLocadorUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resp = intent.getStringExtra("resposta_locadorput");

            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_locador);
        nome = (EditText) findViewById(R.id.nomelocador);
        email = (EditText) findViewById(R.id.emaillocador);
        cpf = (EditText) findViewById(R.id.cpflocador);
        telefone = (EditText) findViewById(R.id.telefonelocador);
        cep = (EditText) findViewById(R.id.ceplocador);
        localidade = (EditText) findViewById(R.id.localidadelocador);
        logradouro = (EditText) findViewById(R.id.logradourolocador);
        uf = (EditText) findViewById(R.id.uflocador);
        complemento = (EditText) findViewById(R.id.complementolocador);
        numero = (EditText) findViewById(R.id.numerolocador);
        addLocador = (Button) findViewById(R.id.buttonaddlocador);
        telefone.addTextChangedListener(Mask.insert("(##)#####-####", telefone));
        cpf.addTextChangedListener(Mask.insert("###.###.###-##", cpf));
        cep.addTextChangedListener(Mask.insert("#####-###",cep));
        localidade.setEnabled(false);
        logradouro.setEnabled(false);
        uf.setEnabled(false);
        complemento.setEnabled(false);
        loc = (Locador) getIntent().getSerializableExtra("locadorEdicao");
        if(loc != null){
            nome.setText(loc.getNome());
            email.setText(loc.getEmail());
            cpf.setText(loc.getCpf());
            telefone.setText(loc.getTelefone());
            cep.setText(loc.getCep());
            localidade.setText(loc.getLocalidade());
            logradouro.setText(loc.getLogradouro());
            uf.setText(loc.getUf());
            complemento.setText(loc.getComplemento());
            numero.setText(loc.getNumero());
            cpfIdUpdate =  loc.getCpf();
            addLocador.setText("Atualizar Locador");

        }
        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Antes da mudança de texto (não é necessário implementar)
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Durante a mudança de texto (não é necessário implementar)
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Após a mudança de texto (chame o serviço de busca de CEP aqui)
                String cep = editable.toString();
                cep = cep.replace("-","");
                if(cep.length() == 8) {
                    localidade.setEnabled(true);
                    logradouro.setEnabled(true);
                    uf.setEnabled(true);
                    complemento.setEnabled(true);

                        Intent it = new Intent(activity_adicionar_locador.this, ServiceLocadorCEPGET.class);
                        it.putExtra("cepLocador", cep);
                        registerReceiver(new activity_adicionar_locador.OuvinteLocadorCep(), new IntentFilter("RESPOSTA_CEP"));
                        startService(it);

                }
            }
        });

    }


    public void adicionarlocador(View v){
        String nomelocador = nome.getText().toString();
        String emaillocador = email.getText().toString();
        String cpflocador = cpf.getText().toString();
        String telefonelocador = telefone.getText().toString();
        String ceplocador = cep.getText().toString();
        String localidadelocador = localidade.getText().toString();
        String logradourolocador = logradouro.getText().toString();
        String uflocador = uf.getText().toString();
        String complementolocador = complemento.getText().toString();
        String numerolocador = numero.getText().toString();

        Locador locador = new Locador(ceplocador,nomelocador,cpflocador,complementolocador,emaillocador,localidadelocador,logradourolocador,numerolocador,uflocador,telefonelocador);

        Gson gson = new Gson();

        String jsonlocador = gson.toJson(locador);

        if(loc != null){
            Intent it = new Intent(this, ServiceLocadorPUT.class);
            it.putExtra("cpfID",cpfIdUpdate);
            it.putExtra("put_jsonlocador", jsonlocador);
            registerReceiver(new activity_adicionar_locador.OuvinteLocadorUpdate(), new IntentFilter("RESPOSTA_LOCADORPUT"));
            startService(it);
            finish();
        }else {

            Intent it = new Intent(this, ServicoLocadorPOST.class);
            it.putExtra("post_jsonlocador", jsonlocador);
            registerReceiver(new activity_adicionar_locador.OuvinteLocadorAdd(), new IntentFilter("RESPOSTA_POSTLOCADOR"));
            startService(it);
            finish();
        }
    }
}