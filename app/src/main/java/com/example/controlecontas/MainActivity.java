package com.example.controlecontas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    class CategoriaAdapter extends ArrayAdapter<Categoria> {

        public CategoriaAdapter() {
            super(MainActivity.this, android.R.layout.simple_list_item_1, cad );
        }

        @Override
        public View getView(int posicao, View reciclada, ViewGroup grupo ) {
            if (reciclada == null) {
                reciclada = getLayoutInflater().inflate(
                        R.layout.item_lista, null );
            }
            Categoria categoria = cad.get( posicao );
            ((TextView) reciclada.findViewById(R.id.item_nome))
                    .setText(categoria.getDescricao());
            ((TextView) reciclada.findViewById(R.id.item_quantidade))
                    .setText(String.valueOf(categoria.getTotalContas()));
            ((TextView) reciclada.findViewById(R.id.item_valor))
                    .setText(String.valueOf(categoria.getValorTotal()));
            if (posicao == selecionado) {
                reciclada.setBackgroundColor(Color.LTGRAY);
            } else {
                reciclada.setBackgroundColor(Color.WHITE);
            }
            return reciclada;
        }

    }

    EditText novaCategoria;

    ListView listaCategorias;
    LinkedList<Categoria> cad;

    CategoriaAdapter adapter;

    int selecionado = -1;
    Boolean editando = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        novaCategoria = (EditText) findViewById(R.id.novacategoria);
        listaCategorias = (ListView) findViewById(R.id.listaCategorias);

        cad = new LinkedList<>();
        if (savedInstanceState != null) {
            cad = (LinkedList<Categoria>) savedInstanceState.getSerializable("LISTA_CATEGORIAS");
            selecionado = savedInstanceState.getInt("SELECIONADO", -1);
        }
        adapter = new CategoriaAdapter();
        listaCategorias.setAdapter( adapter );
        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                selecionado = pos;
                adapter.notifyDataSetChanged();
            }
        });

        listaCategorias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Categoria a = cad.get(pos);
                String descricao = a.getDescricao();
                try {
                        Intent it = new Intent(Intent.ACTION_DIAL);
                        it.setData( Uri.parse("nome:"+ descricao) );
                        startActivity(it);
                    } catch(Exception ex) {
                    }
                return false;
            }
        });
    }

    public void confirmar(View v){
        if(editando){
            Categoria a = cad.get( selecionado );
            a.setDescricao(novaCategoria.getText().toString());
            cad.set(selecionado, a);
//            Categoria novo = new Categoria(novaCategoria.getText().toString());
//            cad.add(novo);
            novaCategoria.setText("");
            adapter.notifyDataSetChanged();
            editando = false;
        }else{
            Categoria novo = new Categoria(novaCategoria.getText().toString());
            cad.add(novo);
            novaCategoria.setText("");
            adapter.notifyDataSetChanged();
            editando = false;
        }

    }

    public void visualizarConta(View v) {
        if (selecionado != -1) {
            Categoria a = cad.get( selecionado );
            Intent it = new Intent(this, VisualizarActivity.class);
            it.putExtra("objeto", a);
            startActivityForResult(it, 123);
        } else {
            Toast.makeText(this, "Selecione uma categoria para visualizar", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requisicao, int resposta, Intent dados){
        super.onActivityResult(requisicao, resposta, dados);
        if(requisicao == 123 && resposta == RESULT_OK){
            Categoria c = (Categoria) dados.getSerializableExtra("objetoModificado");
            int teste = c.getTotalContas();
            cad.set(selecionado, c);
            adapter.notifyDataSetChanged();
        }
    }

    public void remover(View v) {
        if (selecionado == -1) {
            Toast.makeText(this, "Selecione a categoria a remover", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setTitle("Confirmação");
            bld.setMessage("Deseja realmente remover esta categoria?");
            bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cad.remove(selecionado);
                    adapter.notifyDataSetChanged();
                }
            });
            bld.setNegativeButton("Não", null);
            bld.show();
        }
    }

    public void editar(View v) {
        if (listaCategorias != null) {
            Categoria c =  cad.get( selecionado );
            if (c == null) {
                Toast.makeText(this,"Selecione a categoria a editar", Toast.LENGTH_SHORT).show();
            } else {
               novaCategoria.setText(c.getDescricao());
               editando = true;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle dados) {
        super.onSaveInstanceState(dados);
        dados.putSerializable("LISTA_CATEGORIAS", cad);
        dados.putInt("SELECIONADO", selecionado);
    }
}