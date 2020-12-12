package com.example.native_android_firebase_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Editar extends AppCompatActivity {

    String documentId;
    FirebaseFirestore firestore;
    EditText titulo;
    EditText contenido;
    Button editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        titulo = (EditText) findViewById(R.id.editViewTextTitulo);
        contenido = (EditText) findViewById(R.id.editViewTextContenido);
        editar = (Button) findViewById(R.id.btnEditDatos);
        firestore = FirebaseFirestore.getInstance();

        titulo.setText(getIntent().getStringExtra("titulo"));
        contenido.setText(getIntent().getStringExtra("contenido"));
        documentId = getIntent().getStringExtra("documentId");

        editar.setOnClickListener(v -> actualizarDocumento());
    }

    private void actualizarDocumento() {

        String nuevoTitulo = titulo.getText().toString();
        String nuevoContenido = contenido.getText().toString();

        Map<String, Object> document = new HashMap<>();

        document.put("titulo", nuevoTitulo);
        document.put("contenido", nuevoContenido);
        document.put("fecha", FieldValue.serverTimestamp());

        firestore.collection("articulos").document(documentId).update(document).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Editar.this,"¡El documento se editó de manera correcta!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Editar.this,"¡La edición del documento falló!", Toast.LENGTH_LONG).show();
            }
        });
    }
}