package com.example.segundaavaliacao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class activity_locacao_list extends AppCompatActivity {

    LinkedList<Locacao> locacaoList;


    ListView listalocacoes;


    LocacaoAdapter adapter;

    Locacao[] locacoes;

    static boolean click = true;

    static int positionClick = 0;

    int posSelecionada = -1;

    final static int TELA_LOCACAO_EDIT = 123;
    final static int TELA_ADICIONAR_LOCACAO = 123;

    Gson gson;
    class LocacaoAdapter extends ArrayAdapter<Locacao> {
        public LocacaoAdapter(){
            super(activity_locacao_list.this,0,locacaoList);
        }

        @Override
        public View getView(int pos, View recycled, ViewGroup grupo ){
            if (recycled == null){
                recycled = getLayoutInflater().inflate(
                        R.layout.listview_locacao,null);
            }
            Locacao locacao = locacaoList.get(pos);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioFormatada = sdf.format(locacao.getInicio());
            String dataFimFormatada = sdf.format(locacao.getFim());
            ((TextView) recycled.findViewById(R.id.txt_cpflocacao)).setText(locacao.getCpf());
            ((TextView) recycled.findViewById(R.id.txt_locacaodatainicio)).setText(dataInicioFormatada);
            ((TextView) recycled.findViewById(R.id.txt_locacaodatafim)).setText(dataFimFormatada);
            ((TextView) recycled.findViewById(R.id.txt_locacaoplaca)).setText(locacao.getPlaca());
            ((TextView) recycled.findViewById(R.id.txt_locacaovalor)).setText(Double.toString(locacao.getValor()));
            return recycled;
        }
    }



    class DateAdapter implements JsonDeserializer<Date> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return dateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }
    class OuvinteLocacoes extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String respjson = intent.getStringExtra("resposta_locacao");

            locacoes = gson.fromJson(respjson, Locacao[].class);
            locacaoList = new LinkedList<>();

            for (int i = 0; i < locacoes.length ; i++) {
                Locacao locacaoadd = new Locacao(locacoes[i].getCpf(),locacoes[i].getInicio(),locacoes[i].getFim(),locacoes[i].getId(),locacoes[i].getPlaca(),locacoes[i].getValor());
                locacaoList.add(locacaoadd);
                locacaoadd = new Locacao();
            }

            adapter =  new activity_locacao_list.LocacaoAdapter();
            listalocacoes = (ListView) findViewById(R.id.lista_locacao);
            listalocacoes.setAdapter(adapter);
            listalocacoes.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listalocacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Aqui faco a selecao do item clicado para alterar
                    if(adapterView.isClickable() == true && click ) {
                        adapterView.getChildAt(i).setBackgroundColor(Color.GRAY);
                        click = false;
                        positionClick = i;

                    }else{
                        if(i == positionClick ) {
                            adapterView.getChildAt(i).setBackgroundColor(Color.WHITE);
                            click = true;
                        }else{
                            adapterView.getChildAt(positionClick).setBackgroundColor(Color.WHITE);
                            adapterView.getChildAt(i).setBackgroundColor(Color.GRAY);
                            click = false;
                            positionClick = i;
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
            });


        }
    }
    class OuvinteLocacaoDelete extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resp = intent.getStringExtra("resposta_delete");

            if (resp.equals("Excluído com sucesso")) {
                // Remove o veículo da lista
                if (positionClick >= 0 && positionClick < locacaoList.size()) {
                    locacaoList.remove(positionClick);
                }

                // Notifica o adaptador sobre as alterações
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locacao_list);
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateAdapter())
                .create();
        registerReceiver(new activity_locacao_list.OuvinteLocacoes(), new IntentFilter("RESPOSTA_LOCACAO"));
        Intent it = new Intent(this, ServicoLocacaoGET.class);
        startService(it);
    }

    public void adicionarlocacao(View v2) {
        Intent it = new Intent(this, activity_adicionar_locacao.class);
        startActivityForResult(it, TELA_ADICIONAR_LOCACAO);

    }
    public void editarLocacao(View v){
        final int pos = listalocacoes.getCheckedItemPosition();
        if(pos == -1){
            Toast.makeText(this, "Selecione uma locacao para editar", Toast.LENGTH_SHORT).show();
        }else{
            posSelecionada = pos;
            Intent it = new Intent(activity_locacao_list.this, activity_adicionar_locacao.class);
            it.putExtra("locacaoEdicao", locacaoList.get(pos));
            startActivityForResult(it, TELA_LOCACAO_EDIT);
        }
    }

    public void removerLocacao(View vi2) {
        final int pos = listalocacoes.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione uma locacao para remover", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setTitle("Confirmacao");
            bld.setMessage("Deseja realmente remover está Locacao?");
            bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(activity_locacao_list.this, ServicoLocacaoDELETE.class);
                    it.putExtra("locacaoID",Integer.toString(locacaoList.get(pos).getId()));
                    registerReceiver(new activity_locacao_list.OuvinteLocacaoDelete(), new IntentFilter("RESPOSTA_DELETE"));
                    startService(it);

                }
            });
            bld.setNegativeButton("Nao", null);
            bld.show();
        }
    }

    public void consultarLocacao(View v3){
        final int pos = listalocacoes.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione uma Locacao para listar", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View tela = getLayoutInflater().inflate(R.layout.dialog_locacao, null);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioFormatada = sdf.format(locacaoList.get(pos).getInicio());
            String dataFimFormatada = sdf.format(locacaoList.get(pos).getFim());
            TextView edCpfView = tela.findViewById(R.id.txt_locacaocpf);
            TextView edDataInicioView = tela.findViewById(R.id.txt_locacaoinicio);
            TextView edDataFimView = tela.findViewById(R.id.txt_locacaofim);
            TextView edPlacaView = tela.findViewById(R.id.txt_locacaoplaca);
            TextView edValorView = tela.findViewById(R.id.txt_locacaovalor);

            edCpfView.setText(locacaoList.get(pos).getCpf());
            edDataInicioView.setText(dataInicioFormatada);
            edDataFimView.setText(dataFimFormatada);
            edPlacaView.setText(locacaoList.get(pos).getPlaca());
            edValorView.setText(Double.toString(locacaoList.get(pos).getValor()));



            builder.setView(tela);
            builder.create().show();


        }


    }

    protected void onResume() {
        super.onResume();
        registerReceiver(new activity_locacao_list.OuvinteLocacoes(), new IntentFilter("RESPOSTA_LOCACAO"));
        Intent it = new Intent(this, ServicoLocacaoGET.class);
        startService(it);
    }
}