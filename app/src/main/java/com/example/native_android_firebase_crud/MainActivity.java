package com.example.native_android_firebase_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editTextTitulo;
    EditText editTextContenido;
    Button buttonCrearDatos;
    Button buttonMostrarDatos;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextContenido = findViewById(R.id.editTextContenido);
        buttonCrearDatos = findViewById(R.id.btnCrearDatos);
        buttonMostrarDatos = findViewById(R.id.btnMostrarDatos);

        buttonCrearDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearDatos();
                editTextTitulo.getText().clear();
                editTextContenido.getText().clear();
                editTextTitulo.requestFocus();
            }
        });

        buttonMostrarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListaDocumentos.class));
            }
        });
    }

    private void crearDatos() {

        Map<String, Object> datosFirebase = new HashMap<>();

        String titulo = editTextTitulo.getText().toString();
        String contenido = editTextContenido.getText().toString();

        datosFirebase.put("titulo", titulo);
        datosFirebase.put("contenido", contenido);
        datosFirebase.put("fecha", FieldValue.serverTimestamp());

        firestore.collection("articulos").add(datosFirebase).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this,"¡El documento se creó de manera correcta!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"¡El documento no se pudo crear!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerDatos() {
        firestore.collection("articulos").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String titulo = documentSnapshot.getString("titulo");
                    String contenido = documentSnapshot.getString("contenido");
                    Date fecha = documentSnapshot.getTimestamp("fecha").toDate();
                }
            }
        });
    }
}