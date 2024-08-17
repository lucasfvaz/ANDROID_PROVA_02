package com.example.segundaavaliacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static int TELA_ADICIONAR_VEICULO = 123;
    final  static  int TELA_LISTAR_VEICULO = 123;

    final  static  int TELA_ADICIONAR_LOCADOR = 123;

    final  static  int TELA_LISTAR_LOCADOR = 123;

    final  static  int TELA_ADICIONAR_LOCACAO = 123;

    final  static  int TELA_LISTAR_LOCACAO = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!verificaConexaoComInternet()) {
            Toast.makeText(this, "Não é possível usar o aplicativo offline. Verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    public boolean verificaConexaoComInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    public void adicionarveiculo(View v) {
        Intent it = new Intent(this, activity_adicionar_veiculo.class);
        startActivityForResult(it, TELA_ADICIONAR_VEICULO);

    }

    public void listarveiculos(View v){
        Intent it = new Intent(this, activity_veiculo_list.class);
        startActivityForResult(it, TELA_LISTAR_VEICULO);

    }

    public void adicionarlocador(View v) {
        Intent it = new Intent(this, activity_adicionar_locador.class);
        startActivityForResult(it, TELA_ADICIONAR_LOCADOR);

    }

    public void listarlocadores(View v){
        Intent it = new Intent(this, activity_locador_list.class);
        startActivityForResult(it, TELA_LISTAR_LOCADOR);

    }


    public void adicionarlocacao(View v) {
        Intent it = new Intent(this, activity_adicionar_locacao.class);
        startActivityForResult(it, TELA_ADICIONAR_LOCACAO);

    }

    public void listarlocacoes(View v){
        Intent it = new Intent(this, activity_locacao_list.class);
        startActivityForResult(it, TELA_LISTAR_LOCACAO);

    }


}