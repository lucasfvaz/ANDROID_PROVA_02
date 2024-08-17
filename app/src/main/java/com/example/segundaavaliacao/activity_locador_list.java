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

import java.util.LinkedList;

public class activity_locador_list extends AppCompatActivity {

    final static int TELA_LOCADOR_EDIT = 123;
    final static int  TELA_ADICIONAR_LOCADOR = 123;
    ListView listalocadores;
    LinkedList<Locador> locadorList;

    Locador [] locadores;

    LocadorAdapter adapter;


    int posSelecionada = -1;
    static boolean click = true;

    static int positionClick = 0;
    class LocadorAdapter extends ArrayAdapter<Locador> {
        public LocadorAdapter(){
            super(activity_locador_list.this,0,locadorList);
        }

        @Override
        public View getView(int pos, View recycled, ViewGroup grupo ){
            if (recycled == null){
                recycled = getLayoutInflater().inflate(
                        R.layout.listview_locador,null);
            }
            Locador locador = locadorList.get(pos);

            ((TextView) recycled.findViewById(R.id.txt_nomelocador)).setText(locador.getNome());
            ((TextView) recycled.findViewById(R.id.txt_cpflocador)).setText(locador.getCpf());
            ((TextView) recycled.findViewById(R.id.txt_emaillocador)).setText(locador.getEmail());
            ((TextView) recycled.findViewById(R.id.txt_telefonelocador)).setText(locador.getTelefone());

            return recycled;
        }
    }

    class OuvinteLocadores extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String respjson = intent.getStringExtra("resposta_locador");
            Gson gson = new Gson();

            locadores = gson.fromJson(respjson, Locador[].class);
            locadorList = new LinkedList<>();
            for (int i = 0; i < locadores.length; i++) {
                Locador locadoradd = new Locador(
                        locadores[i].getCep(),
                        locadores[i].getNome(),
                        locadores[i].getCpf(),
                        locadores[i].getComplemento(),
                        locadores[i].getEmail(),
                        locadores[i].getLocalidade(),
                        locadores[i].getLogradouro(),
                        locadores[i].getNumero(),
                        locadores[i].getNumero(),
                        locadores[i].getTelefone()
                );
                locadorList.add(locadoradd);
                locadoradd = new Locador();
            }

            adapter = new LocadorAdapter();
            listalocadores = (ListView) findViewById(R.id.lista_locadores);
            listalocadores.setAdapter(adapter);
            listalocadores.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listalocadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    class OuvinteLocadorDelete extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resp = intent.getStringExtra("resposta_locadordelete");

            if (resp.equals("Excluído com sucesso")) {
                // Remove o veículo da lista
                if (positionClick >= 0 && positionClick < locadorList.size()) {
                    locadorList.remove(positionClick);
                }

                // Notifica o adaptador sobre as alterações
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
        }
    }

    public void adicionarLocador(View v3) {
        Intent it = new Intent(this, activity_adicionar_locador.class);
        startActivityForResult(it, TELA_ADICIONAR_LOCADOR);

    }
    public void editarLocador(View v){
        final int pos = listalocadores.getCheckedItemPosition();
        if(pos == -1){
            Toast.makeText(this, "Selecione um veiculo para editar", Toast.LENGTH_SHORT).show();
        }else{
            posSelecionada = pos;
            Intent it = new Intent(activity_locador_list.this, activity_adicionar_locador.class);
            it.putExtra("locadorEdicao", locadorList.get(pos));
            startActivityForResult(it, TELA_LOCADOR_EDIT);
        }
    }

    public void removerLocador(View vi2) {
        final int pos = listalocadores.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione um locador para remover", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setTitle("Confirmacao");
            bld.setMessage("Deseja realmente remover este Locador de Veiculo ?");
            bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(activity_locador_list.this, ServicoLocadorDELETE.class);
                    it.putExtra("cpfID",locadorList.get(pos).getCpf());
                    registerReceiver(new activity_locador_list.OuvinteLocadorDelete(), new IntentFilter("RESPOSTA_LOCADORDELETE"));
                    startService(it);

                }
            });
            bld.setNegativeButton("Nao", null);
            bld.show();
        }
    }

    public void consultarLocador(View v3){
        final int pos = listalocadores.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecione um Locador para consultar", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View tela = getLayoutInflater().inflate(R.layout.dialig_locador, null);

            TextView edNomeView = tela.findViewById(R.id.txt_locadornome);
            TextView edEmailView = tela.findViewById(R.id.txt_locadoremail);
            TextView edCpfView = tela.findViewById(R.id.txt_locadorcpf);
            TextView edTelefoneView = tela.findViewById(R.id.txt_locadortelefone);
            TextView edCepView = tela.findViewById(R.id.txt_locadorcep);
            TextView edLocalidadeView = tela.findViewById(R.id.txt_locadorlocalidade);
            TextView edLogradouroView = tela.findViewById(R.id.txt_locadorlogradouro);
            TextView edUfView = tela.findViewById(R.id.txt_locadoruf);
            TextView edComplementoView = tela.findViewById(R.id.txt_locadorcomplemento);
            TextView edNumeroView = tela.findViewById(R.id.txt_locadornumero);


            edNomeView.setText(locadorList.get(pos).getNome());
            edEmailView.setText(locadorList.get(pos).getEmail());
            edCpfView.setText(locadorList.get(pos).getCpf());
            edTelefoneView.setText(locadorList.get(pos).getTelefone());
            edCepView.setText(locadorList.get(pos).getCep());
            edLocalidadeView.setText(locadorList.get(pos).getLocalidade());
            edLogradouroView.setText(locadorList.get(pos).getLogradouro());
            edUfView.setText(locadorList.get(pos).getUf());
            edComplementoView.setText(locadorList.get(pos).getComplemento());
            edNumeroView.setText(locadorList.get(pos).getNumero());


            builder.setView(tela);
            builder.create().show();


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locador_list);


        registerReceiver(new activity_locador_list.OuvinteLocadores(), new IntentFilter("RESPOSTA_LOCADOR"));
        Intent it = new Intent(this, ServicoLocadorGET.class);
        startService(it);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(new activity_locador_list.OuvinteLocadores(), new IntentFilter("RESPOSTA_LOCADOR"));
        Intent it = new Intent(this, ServicoLocadorGET.class);
        startService(it);
    }
}