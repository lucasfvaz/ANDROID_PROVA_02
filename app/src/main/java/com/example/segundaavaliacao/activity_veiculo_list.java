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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.LinkedList;

public class activity_veiculo_list extends AppCompatActivity {
    final static int TELA_VEICULO_EDIT = 123;
    final static int TELA_VEICULO_ADD = 123;
    ListView listaveiculos;

    LinkedList<Veiculo> veiculosList;
    Veiculo[] veiculos;

    VeiculoAdapter adapter;

    int posSelecionada = -1;
    static boolean click = true;

    static int positionClick = 0;
    class VeiculoAdapter extends ArrayAdapter<Veiculo>{
        public VeiculoAdapter(){
            super(activity_veiculo_list.this,0,veiculosList);
        }

        @Override
        public View getView(int pos, View recycled, ViewGroup grupo ){
            if (recycled == null){
                recycled = getLayoutInflater().inflate(
                        R.layout.listview_veiculos,null);
            }
            Veiculo veiculo = veiculosList.get(pos);

            ((TextView) recycled.findViewById(R.id.txt_nomeveiculo)).setText(veiculo.getModelo().getDescricao());
            ((TextView) recycled.findViewById(R.id.txt_anoveiculo)).setText(Integer.toString(veiculo.getAno()));
            ((TextView) recycled.findViewById(R.id.txt_marcaveiculo)).setText(veiculo.getModelo().getMarca().getNome());
            ((TextView) recycled.findViewById(R.id.txt_placaveiculo)).setText(veiculo.getPlaca());
            return recycled;
        }
    }
    class OuvinteVeiculos extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String respjson = intent.getStringExtra("resposta_veiculo");
            Gson gson = new Gson();

            veiculos = gson.fromJson(respjson, Veiculo[].class);
            veiculosList = new LinkedList<>();
            for (int i = 0; i < veiculos.length ; i++) {
                Veiculo veiculoadd = new Veiculo(veiculos[i].getAno(),veiculos[i].getCor(),veiculos[i].getIdModelo(),veiculos[i].getModelo(),veiculos[i].getPlaca());
                veiculosList.add(veiculoadd);
                veiculoadd = new Veiculo();
            }

            adapter =  new VeiculoAdapter();
            listaveiculos = (ListView) findViewById(R.id.lista_veiculos);
            listaveiculos.setAdapter(adapter);
            listaveiculos.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listaveiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    class OuvinteVeiculoDelete extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resp = intent.getStringExtra("resposta_delete");

            if (resp.equals("Excluído com sucesso")) {
                // Remove o veículo da lista
                if (positionClick >= 0 && positionClick < veiculosList.size()) {
                    veiculosList.remove(positionClick);
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
        setContentView(R.layout.activity_veiculo_list);

        registerReceiver(new activity_veiculo_list.OuvinteVeiculos(), new IntentFilter("RESPOSTA_VEICULO"));
        Intent it = new Intent(this, ServicoVeiculoGET.class);
        startService(it);

    }

    public void adicionarveiculo(View v) {
        Intent it = new Intent(this, activity_adicionar_veiculo.class);
        startActivityForResult(it, TELA_VEICULO_ADD);

    }

    public void editarVeiculo(View v){
        final int pos = listaveiculos.getCheckedItemPosition();
        if(pos == -1){
            Toast.makeText(this, "Selecione um veiculo para editar", Toast.LENGTH_SHORT).show();
        }else{
            posSelecionada = pos;
            Intent it = new Intent(activity_veiculo_list.this, activity_adicionar_veiculo.class);
            it.putExtra("veiculoEdicao", veiculosList.get(pos));
            startActivityForResult(it, TELA_VEICULO_EDIT);
        }
    }


    public void removerVeiculo(View vi2) {
        final int pos = listaveiculos.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione um veiculo para remover", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setTitle("Confirmacao");
            bld.setMessage("Deseja realmente remover este contato de Veiculo ?");
            bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(activity_veiculo_list.this, ServicoVeiculoDELETE.class);
                    it.putExtra("placaID",veiculosList.get(pos).getPlaca());
                    registerReceiver(new activity_veiculo_list.OuvinteVeiculoDelete(), new IntentFilter("RESPOSTA_DELETE"));
                    startService(it);

                }
            });
            bld.setNegativeButton("Nao", null);
            bld.show();
        }
    }

    public void consultarVeiculo(View v3){
        final int pos = listaveiculos.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione um Veiculo para consultar", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View tela = getLayoutInflater().inflate(R.layout.dialig_info, null);

            TextView edPlacaView = tela.findViewById(R.id.txt_infoplaca);
            TextView edValorView = tela.findViewById(R.id.txt_infovalor);
            TextView edMarcaView = tela.findViewById(R.id.txt_infomarca);
            TextView edModeloView = tela.findViewById(R.id.txt_infomodelo);
            TextView edAnoView = tela.findViewById(R.id.txt_infoano);
            TextView edCategoriaView = tela.findViewById(R.id.txt_infocategoria);

            edPlacaView.setText(veiculosList.get(pos).getPlaca());

            edValorView.setText(Double.toString(veiculosList.get(pos).getModelo().getValorDiaria()));

            edMarcaView.setText(veiculosList.get(pos).getModelo().getMarca().getNome());

            edModeloView.setText(veiculosList.get(pos).getModelo().getDescricao());

            edAnoView.setText(Integer.toString(veiculosList.get(pos).getAno()));

            edCategoriaView.setText(veiculosList.get(pos).getModelo().getCategoria());

            builder.setView(tela);
            builder.create().show();


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new activity_veiculo_list.OuvinteVeiculos(), new IntentFilter("RESPOSTA_VEICULO"));
        Intent it = new Intent(this, ServicoVeiculoGET.class);
        startService(it);
    }

}