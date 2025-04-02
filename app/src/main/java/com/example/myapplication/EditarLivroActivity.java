package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class EditarLivroActivity extends AppCompatActivity {

    private EditText editTextTituloEditar;
    private EditText editTextAutorEditar;
    private Spinner spinnerCategoriaEditar;
    private RadioGroup radioGroupLeituraEditar;
    private RadioButton radioButtonLidoEditar;
    private Button buttonAtualizar;
    private Button buttonCancelar;

    private LivroDBHelper dbHelper;
    private int livroId;
    private Livro livroAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_livro);

        editTextTituloEditar = findViewById(R.id.editTextTituloEditar);
        editTextAutorEditar = findViewById(R.id.editTextAutorEditar);
        spinnerCategoriaEditar = findViewById(R.id.spinnerCategoriaEditar);
        radioGroupLeituraEditar = findViewById(R.id.radioGroupLeituraEditar);
        radioButtonLidoEditar = findViewById(R.id.radioButtonLidoEditar);
        buttonAtualizar = findViewById(R.id.buttonAtualizar);
        buttonCancelar = findViewById(R.id.buttonCancelar);

        dbHelper = new LivroDBHelper(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_livros,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaEditar.setAdapter(spinnerAdapter);

        livroId = getIntent().getIntExtra("livro_id", -1);

        if (livroId != -1) {
            carregarDadosDoLivro(livroId);
        } else {
            Toast.makeText(this, "Erro ao carregar os detalhes do livro.", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarLivro();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void carregarDadosDoLivro(int id) {
        livroAtual = dbHelper.getLivro(id);
        editTextTituloEditar.setText(livroAtual.getTitulo());
        editTextAutorEditar.setText(livroAtual.getAutor());

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerCategoriaEditar.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(livroAtual.getCategoria());
            spinnerCategoriaEditar.setSelection(position);
        }

        if (livroAtual.isLido()) {
            radioButtonLidoEditar.setChecked(true);
        } else {
            radioGroupLeituraEditar.check(R.id.radioButtonNaoLidoEditar);
        }
    }

    private void atualizarLivro() {
        String titulo = editTextTituloEditar.getText().toString().trim();
        String autor = editTextAutorEditar.getText().toString().trim();
        String categoria = spinnerCategoriaEditar.getSelectedItem().toString();
        boolean lido = radioButtonLidoEditar.isChecked();

        if (titulo.isEmpty() || autor.isEmpty()) {
            Toast.makeText(this, R.string.erro_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        livroAtual.setTitulo(titulo);
        livroAtual.setAutor(autor);
        livroAtual.setCategoria(categoria);
        livroAtual.setLido(lido);

        int rowsAffected = dbHelper.atualizarLivro(livroAtual);

        if (rowsAffected > 0) {
            Toast.makeText(this, R.string.toast_livro_atualizado, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar o livro.", Toast.LENGTH_SHORT).show();
        }
    }
}
