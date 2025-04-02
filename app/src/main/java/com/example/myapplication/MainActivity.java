package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextAutor;
    private Spinner spinnerCategoria;
    private RadioGroup radioGroupLeitura;
    private RadioButton radioButtonLido;
    private Button buttonSalvar;
    private ListView listViewLivros;

    private LivroDBHelper dbHelper;
    private List<Livro> listaLivros;
    private ArrayAdapter<Livro> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAutor = findViewById(R.id.editTextAutor);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        radioGroupLeitura = findViewById(R.id.radioGroupLeitura);
        radioButtonLido = findViewById(R.id.radioButtonLido);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        listViewLivros = findViewById(R.id.listViewLivros);

        dbHelper = new LivroDBHelper(this);
        listaLivros = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaLivros);
        listViewLivros.setAdapter(adapter);

        atualizarListaDeLivros();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_livros,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinnerAdapter);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarLivro();
            }
        });

        listViewLivros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Livro livroSelecionado = listaLivros.get(position);
                editarLivro(livroSelecionado);
            }
        });

        listViewLivros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Livro livroSelecionado = listaLivros.get(position);
                excluirLivro(livroSelecionado);
                return true;
            }
        });
    }

    private void atualizarListaDeLivros() {
        listaLivros.clear();
        listaLivros.addAll(dbHelper.getAllLivros());
        adapter.notifyDataSetChanged();
    }

    private void salvarLivro() {
        String titulo = editTextTitulo.getText().toString().trim();
        String autor = editTextAutor.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        boolean lido = radioButtonLido.isChecked();

        if (titulo.isEmpty() || autor.isEmpty()) {
            Toast.makeText(this, R.string.erro_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        Livro novoLivro = new Livro(titulo, autor, categoria, lido);
        long id = dbHelper.adicionarLivro(novoLivro);

        if (id > 0) {
            Toast.makeText(this, R.string.toast_livro_salvo, Toast.LENGTH_SHORT).show();
            editTextTitulo.getText().clear();
            editTextAutor.getText().clear();
            radioGroupLeitura.check(R.id.radioButtonNaoLido);
            atualizarListaDeLivros();
        } else {
            Toast.makeText(this, "Erro ao salvar o livro.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarLivro(Livro livro) {
        Intent intent = new Intent(this, EditarLivroActivity.class);
        intent.putExtra("livro_id", livro.getId());
        startActivity(intent);
    }

    private void excluirLivro(final Livro livro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_excluir_titulo)
                .setMessage(R.string.dialog_excluir_mensagem)
                .setPositiveButton(R.string.dialog_excluir_sim, (dialog, which) -> {
                    dbHelper.deletarLivro(livro.getId());
                    atualizarListaDeLivros();
                    Toast.makeText(MainActivity.this, R.string.toast_livro_excluido, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.dialog_excluir_nao, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListaDeLivros();
    }
}