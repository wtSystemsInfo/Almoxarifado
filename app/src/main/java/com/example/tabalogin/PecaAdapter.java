package com.example.tabalogin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.example.tabalogin.PecaAdapter.OnLongClickListener ;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabalogin.Model.Peca;

public class PecaAdapter extends RecyclerView.Adapter<PecaAdapter.PecaViewHolder> {
    private List<Peca> listaPecaOS;
    private OnLongClickListener  listener;


    public interface OnLongClickListener  {
        void onLongClick(Peca pecaOS);
    }

    public void setOnItemClickListener(OnLongClickListener  listener) {
        this.listener = listener;
    }

    public PecaAdapter(List<Peca> listaPecaOS) {
        this.listaPecaOS = listaPecaOS;
    }

    @NonNull
    @Override
    public PecaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_os, parent, false);
        return new PecaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PecaViewHolder holder, int position) {
        Peca pecaOS = listaPecaOS.get(position);

        // Atualize os componentes do item da peça com os dados da peça atual

        holder.textViewDesc.setText(pecaOS.getPecadesc());
        holder.textViewCodigo.setText(String.valueOf(pecaOS.getCodpeca()));
        holder.textViewFunc.setText(pecaOS.getPecafunc());
        holder.textViewQtde.setText(String.valueOf(pecaOS.getPecaqtde()));


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(pecaOS);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPecaOS.size();
    }

    public static class PecaViewHolder extends RecyclerView.ViewHolder {
        // Defina os componentes do item da peça aqui
        public TextView textViewDesc;
        public TextView textViewCodigo;
        public TextView textViewFunc;
        public TextView textViewQtde;

        public PecaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicialize os componentes do item da peça aqui
            textViewDesc = itemView.findViewById(R.id.txtDesc);
            textViewCodigo = itemView.findViewById(R.id.txtCodItem);
            textViewFunc = itemView.findViewById(R.id.txtFunc);
            textViewQtde = itemView.findViewById(R.id.txtQtde);
        }
    }
}

