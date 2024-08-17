package com.example.segundaavaliacao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class activity_adicionar_locacao extends AppCompatActivity {
    ArrayAdapter<Veiculo> adapter;

    ArrayAdapter<Locador> adapterlocador;
    Spinner veiculoSelecionado,locadorSelecionado;
    TextView valorModelo,marcaModelo, nomeLocador, cpfLocador;

    Gson gson;

    Button addlocacao;

    EditText dataInicio,dataFim;
    Locacao loc;

    String locacaoId;
    public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return dateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }


    class OuvinteVeiculoLocador extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String respjson = intent.getStringExtra("resposta_locador");
            listLocadores(respjson);
            respjson = intent.getStringExtra("resposta_veiculo");
            listVeiculos( respjson);
        }
    }


    public void listVeiculos(String respjson){
        Gson gson = new Gson();
        Veiculo[] veiculos = gson.fromJson(respjson, Veiculo[].class);
        Spinner dropdown = findViewById(R.id.spinnerVeiculo);


        if(veiculos != null) {
            adapter = new ArrayAdapter<>(activity_adicionar_locacao.this, android.R.layout.simple_spinner_dropdown_item, veiculos);
            dropdown.setAdapter(adapter);
            if (loc != null) {
                int selectedPosition = -1;


                for (int i = 0; i < veiculos.length; i++) {
                    if (veiculos[i].getPlaca().equals(loc.getPlaca())) {
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
        veiculoSelecionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Veiculo itemSelecionado = (Veiculo) parentView.getSelectedItem();


                valorModelo.setText("Valor da Diaria: " + Double.toString(itemSelecionado.getModelo().getValorDiaria()));
                marcaModelo.setText("Marca: " + itemSelecionado.getModelo().getMarca().getNome());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
    public void listLocadores(String respjson){
        Gson gson = new Gson();
        Locador[] locadores = gson.fromJson(respjson, Locador[].class);
        Spinner dropdown = findViewById(R.id.spinnerLocadores);


        if(locadores != null) {
            adapterlocador = new ArrayAdapter<>(activity_adicionar_locacao.this, android.R.layout.simple_spinner_dropdown_item, locadores);
            dropdown.setAdapter(adapterlocador);
            if (loc != null) {
                int selectedPosition = -1;


                for (int i = 0; i < locadores.length; i++) {
                    if (locadores[i].getCpf().equals(loc.getCpf())) {
                        selectedPosition = i;
                        break;
                    }
                }


                if (selectedPosition != -1) {
                    dropdown.setSelection(selectedPosition);
                }
            }
            adapterlocador.notifyDataSetChanged();
        }
        locadorSelecionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                Locador itemSelecionado = (Locador) parentView.getSelectedItem();


                nomeLocador.setText("Nome Locador: " + itemSelecionado.getNome());
                cpfLocador.setText("Cpf Locador: " + itemSelecionado.getCpf());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    class OuvinteLocacaoAdd extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response= intent.getStringExtra("resposta_postlocacao");

            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


        }
    }
    class OuvinteLocacaoUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response= intent.getStringExtra("resposta_put");

            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_locacao);
        veiculoSelecionado = (Spinner) findViewById(R.id.spinnerVeiculo);
        valorModelo = (TextView) findViewById(R.id.valormodelo);
        marcaModelo = (TextView) findViewById(R.id.marcaModelo);
        Spinner dropdown = findViewById(R.id.spinnerVeiculo);
        locadorSelecionado = (Spinner) findViewById(R.id.spinnerLocadores);
        nomeLocador = (TextView) findViewById(R.id.nomelocador);
        cpfLocador = (TextView) findViewById(R.id.cpflocador);
        dataInicio = (EditText)  findViewById(R.id.datainicio);
        dataFim = (EditText) findViewById(R.id.datafim);

        dataInicio.addTextChangedListener(Mask.insert("##/##/####", dataInicio));
        dataFim.addTextChangedListener(Mask.insert("##/##/####",dataFim));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateAdapter());
        gson = gsonBuilder.create();
        addlocacao = (Button) findViewById(R.id.logarveiculo);
        loc = (Locacao) getIntent().getSerializableExtra("locacaoEdicao");

        if(loc != null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioFormatada = sdf.format(loc.getInicio());
            String dataFimFormatada = sdf.format(loc.getFim());
            cpfLocador.setText(loc.getCpf());
            dataInicio.setText(dataInicioFormatada);
            dataFim.setText(dataFimFormatada);
            locacaoId = Integer.toString(loc.getId());
            addlocacao.setText("Atualizar Locacao");
        }


        registerReceiver(new activity_adicionar_locacao.OuvinteVeiculoLocador(), new IntentFilter("RESPOSTA_VEICULOLOCADOR"));
        Intent itloc = new Intent(this, ServiceVeiculoLocadorGET.class);
        startService(itloc);
    }

    public void adicionarLocacao(View v){
        try {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);

            Locador locador = (Locador) locadorSelecionado.getSelectedItem();
            Veiculo veiculo = (Veiculo) veiculoSelecionado.getSelectedItem();


            String dataInicioStr = dataInicio.getText().toString();
            String dataFimStr = dataFim.getText().toString();

            long diferencaEmDias;
            Double valorTotaldiaria = 0.0;
            Date dataInicioformat =  new Date();
            Date dataFimformat = new Date();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dataInicioformat = sdf.parse(dataInicioStr);
                dataFimformat = sdf.parse(dataFimStr);

                long diferencaEmMillis = dataFimformat.getTime() - dataInicioformat.getTime();
                diferencaEmDias = diferencaEmMillis / (24 * 60 * 60 * 1000);

                valorTotaldiaria = (diferencaEmDias * veiculo.getModelo().getValorDiaria());
            } catch (Exception e) {
                e.printStackTrace();
            }
            bld.setMessage("Confirmar locacao \n" +
                    "-----------------------------\n" +
                    "Locador Informacoes: " + "\n" +
                    "Nome: " + locador.getNome() + "\n"
                    + "Email: " + locador.getEmail() + "\n"
                    + "Telefone: " + locador.getTelefone() + "\n"
                    + "Cpf: " + locador.getCpf() + "\n"
                    + "Cep: " + locador.getCep() + "\n"
                    + "-----------------------------\n"
                    + "Veiculo Informacoes: " + "\n"
                    + "Modelo: " + veiculo.getModelo().getDescricao() + "\n"
                    + "Marca: " + veiculo.getModelo().getMarca().getNome() + "\n"
                    + "Ano: " + veiculo.getAno() + "\n"
                    + "Placa: " + veiculo.getPlaca() + "\n"
                    + "Valor Diária: " + veiculo.getModelo().getValorDiaria() + "\n"
                    + "-----------------------------\n"
                    + "Informacoes locacaco: \n"
                    + "Data de ínicio: " + dataInicio.getText().toString() + "\n"
                    + "Data Fim: " + dataFim.getText().toString() + "\n"
                    + "Valor Total da Diária: " + valorTotaldiaria
                    );

            Date finalDataInicioformat = dataInicioformat;
            Date finalDataFimformat = dataFimformat;
            Double valorTotalFinal = valorTotaldiaria;
            bld.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  Locacao locacao = new Locacao();
                  locacao.setCpf(locador.getCpf());
                  locacao.setPlaca(veiculo.getPlaca());
                  locacao.setInicio(finalDataInicioformat);
                  locacao.setFim(finalDataFimformat);
                  locacao.setValor(valorTotalFinal);



                  String jsonlocacao = gson.toJson(locacao);
                    if (loc != null) {
                        locacao.setId(Integer.parseInt(locacaoId));
                        jsonlocacao = gson.toJson(locacao);
                        Intent itloc = new Intent(activity_adicionar_locacao.this,ServicoLocacaoPUT.class);
                        itloc.putExtra("locacaoID",locacaoId);
                        itloc.putExtra("put_jsonlocacao", jsonlocacao);
                        registerReceiver(new activity_adicionar_locacao.OuvinteLocacaoUpdate(), new IntentFilter("RESPOSTA_PUT"));
                        startService(itloc);
                        finish();
                    }else {
                        Intent itloc = new Intent(activity_adicionar_locacao.this, ServicoLocacaoPOST.class);
                        itloc.putExtra("post_jsonlocacao", jsonlocacao);
                        registerReceiver(new activity_adicionar_locacao.OuvinteLocacaoAdd(), new IntentFilter("RESPOSTA_POSTLOCACACO"));
                        startService(itloc);
                        finish();
                    }
                }
            });
            bld.setNegativeButton("Voltar", null);
            bld.show();


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}