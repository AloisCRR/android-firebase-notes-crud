package com.example.lab05_cloud_firestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListaDocumentos extends AppCompatActivity {

    RecyclerView recyclerViewArticulos;
    DocumentosAdapter adapter;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        recyclerViewArticulos = findViewById(R.id.recyclerArticulos);
        recyclerViewArticulos.setLayoutManager(new LinearLayoutManager(this));
        firestore = FirebaseFirestore.getInstance();

        Query query = firestore.collection("articulos");

        FirestoreRecyclerOptions<Documento> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Documento>().setQuery(query, Documento.class).build();

        adapter = new DocumentosAdapter(firestoreRecyclerOptions, this);
        adapter.notifyDataSetChanged();
        recyclerViewArticulos.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}