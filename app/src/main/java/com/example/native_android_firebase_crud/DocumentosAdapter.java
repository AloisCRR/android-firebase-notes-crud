package com.example.native_android_firebase_crud;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DocumentosAdapter extends FirestoreRecyclerAdapter<Documento, DocumentosAdapter.ViewHolders> {

    Activity activity;
    FirebaseFirestore firestore;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DocumentosAdapter(@NonNull FirestoreRecyclerOptions<Documento> options, Activity activity) {
        super(options);
        this.activity = activity;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolders holder, int position, @NonNull Documento model) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

        String id = documentSnapshot.getId();

        String titulo = model.getTitulo();
        String contenido = model.getContenido();

        holder.titulo.setText(titulo);
        holder.contenido.setText(contenido);
        holder.fecha.setText(formatData(model.getFecha()));

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Editar.class);
                intent.putExtra("documentId", id);
                intent.putExtra("titulo", titulo);
                intent.putExtra("contenido", contenido);
                activity.startActivity(intent);
            }
        });
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarDocumento(id);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_articulos, parent, false);
        return new ViewHolders(view);
    }

    public static class ViewHolders extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView contenido;
        TextView fecha;
        Button editar;
        Button eliminar;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textViewTitulo);
            contenido = itemView.findViewById(R.id.textViewContenido);
            fecha = itemView.findViewById(R.id.textViewFecha);
            editar = itemView.findViewById(R.id.btnEditarArticulo);
            eliminar = itemView.findViewById(R.id.btnEliminarArticulo);
        }
    }

    private String formatData(Date date) {
        String pattern = "dd/MM/yyyy K:mm a";
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        return dateFormat.format(date);
    }

    private void eliminarDocumento(String documentId) {
        firestore.collection("articulos").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity,"¡El documento se eliminó de manera correcta!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity,"¡Error al eliminar el documento!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
