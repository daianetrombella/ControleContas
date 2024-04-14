package com.example.controlecontas;


        import static java.lang.String.valueOf;

        import android.app.DatePickerDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.LinkedList;

public class VisualizarActivity  extends AppCompatActivity {

    class ContaAdapter extends ArrayAdapter<Conta> {

        public ContaAdapter() {
            super(VisualizarActivity.this, android.R.layout.simple_list_item_1, cad );
        }

        @Override
        public View getView(int posicao, View reciclada, ViewGroup grupo ) {

            if (reciclada == null) {
                reciclada = getLayoutInflater().inflate(
                        R.layout.item_lista_conta, null );
            }
            Conta conta = cad.get( posicao );
            if(data == null){
                data = new Date();
            }else {
                data = conta.getVencimento();
            }

            String valorDouble = Double.toString(conta.getValor());
            ((TextView) reciclada.findViewById(R.id.item_nome))
                    .setText(conta.getDescricao());
            ((TextView) reciclada.findViewById(R.id.item_valor))
                    .setText(valorDouble);
            ((TextView) reciclada.findViewById(R.id.item_vencimento))
                    .setText(sdf.format(data));
            if (posicao == selecionado) {
                reciclada.setBackgroundColor(Color.LTGRAY);
            } else {
                reciclada.setBackgroundColor(Color.WHITE);
            }
            return reciclada;
        }

    }
    ListView listaContas;
    LinkedList<Conta> cad;

    EditText nomeDespesa;

    EditText valorDespesaEdit;
    EditText vencimentoDespesaEdit;
    ContaAdapter adapter;
    Categoria objeto;
    String nomeCategoria;
    int selecionado = -1;
    Boolean editando = false;
    Date data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        listaContas = (ListView) findViewById(R.id.listaContas);
        nomeDespesa = (EditText) findViewById(R.id.nomeDespesa);
        valorDespesaEdit = (EditText) findViewById(R.id.valorDespesa);
        vencimentoDespesaEdit = (EditText) findViewById(R.id.txt_data);

        cad = new LinkedList<>();
        if (savedInstanceState != null) {
            cad = (LinkedList<Conta>) savedInstanceState.getSerializable("LISTA_CONTAS");
            selecionado = savedInstanceState.getInt("SELECIONADO", -1);
        }
        adapter = new ContaAdapter();
        listaContas.setAdapter( adapter );
        listaContas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                selecionado = pos;
                adapter.notifyDataSetChanged();
            }
        });

        listaContas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Conta a = cad.get(pos);
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

        TextView descricaoText = (TextView) findViewById(R.id.descricaoText);
        Intent origem = getIntent();
        objeto = (Categoria) getIntent().getSerializableExtra("objeto");
        LinkedList<Conta> contas = objeto.getContas();
        nomeCategoria = objeto.getDescricao();
        descricaoText.setText(nomeCategoria);

        for (Conta c : contas) {
            cad.add(c);
        }

    }


    public void registrar(View v) throws ParseException, ParseException {
        Double valorDespesa = Double.parseDouble(valorDespesaEdit.getText().toString());
//        String vencimentoDespesaString = vencimentoDespesaEdit.getText().toString();
        String descricao = nomeDespesa.getText().toString();

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        sdf.setLenient(false);
//        Date data = sdf.parse(vencimentoDespesaString);


        if(editando){
            Conta a = cad.get( selecionado );
            a.setDescricao(descricao);
            a.setValor(valorDespesa);
            a.setVencimento(data);

            cad.set(selecionado, a);
            LinkedList<Conta> contasTemp = objeto.getContas();
            contasTemp.set(selecionado, a);
            objeto.modificarTodasContas(contasTemp);
            adapter.notifyDataSetChanged();
            nomeDespesa.setText("");
            valorDespesaEdit.setText("");
            vencimentoDespesaEdit.setText("");
            adapter.notifyDataSetChanged();
            editando = false;

        }else {

            Conta novo = new Conta(descricao, data, valorDespesa, nomeCategoria);
            cad.add(novo);
            objeto.setContas(novo);
            nomeDespesa.setText("");
            valorDespesaEdit.setText("");
            vencimentoDespesaEdit.setText("");
            adapter.notifyDataSetChanged();
        }
    }

    public void voltar(View view){
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra("objetoModificado", objeto); // objetoModificado é o objeto modificado que você deseja passar de volta
        setResult(RESULT_OK, retornoIntent);
        finish();
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public void vencimento(View v) {
        DatePickerDialog dpd = new DatePickerDialog(this);
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                data = new Date(ano - 1900, mes, dia);

                TextView dt = findViewById(R.id.txt_data);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                dt.setText(sdf.format(data));
            }
        });
        dpd.show();
    }
    public void remover(View v) {
        if (selecionado == -1) {
            Toast.makeText(this, "Selecione uma conta a remover", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setTitle("Confirmação");
            bld.setMessage("Deseja realmente remover esta conta?");
            bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cad.remove(selecionado);
                    LinkedList<Conta> contasTemp = objeto.getContas();
                    contasTemp.remove(selecionado);
                    objeto.modificarTodasContas(contasTemp);
                    adapter.notifyDataSetChanged();
                }
            });
            bld.setNegativeButton("Não", null);
            bld.show();
        }
    }

    public void editar(View v) {
        if (listaContas != null) {
            Conta c =  cad.get( selecionado );
            if (c == null) {
                Toast.makeText(this,"Selecione a conta a editar", Toast.LENGTH_SHORT).show();
            } else {
                nomeDespesa.setText(c.getDescricao());
                valorDespesaEdit.setText(valueOf(c.getValor()));
                vencimentoDespesaEdit.setText(sdf.format(c.getVencimento()));
                editando = true;
            }
        }
    }



    @Override
    public void onSaveInstanceState(Bundle dados) {
        super.onSaveInstanceState(dados);
        dados.putSerializable("LISTA_CONTAS", cad);
    }
}
