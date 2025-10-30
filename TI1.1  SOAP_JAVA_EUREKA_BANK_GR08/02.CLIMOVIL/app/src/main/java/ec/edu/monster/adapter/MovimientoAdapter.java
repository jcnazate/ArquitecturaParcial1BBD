package ec.edu.monster.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ec.edu.monster.R;
import ec.edu.monster.model.MovimientoModel;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.ViewHolder> {

    private List<MovimientoModel> movimientos;

    public MovimientoAdapter(List<MovimientoModel> movimientos) {
        this.movimientos = movimientos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovimientoModel movimiento = movimientos.get(position);

        holder.tvNro.setText(String.valueOf(movimiento.getNumeroMovimiento()));
        holder.tvFecha.setText(movimiento.getFechaMovimiento());
        holder.tvTipo.setText(movimiento.getCodigoTipoMovimiento());
        holder.tvDescripcion.setText(movimiento.getTipoDescripcion());
        holder.tvImporte.setText(String.format("$%.2f", movimiento.getImporteMovimiento()));
        holder.tvCtaRef.setText(movimiento.getCuentaReferencia() != null ? movimiento.getCuentaReferencia() : "N/A");
        holder.tvSaldo.setText(String.format("$%.2f", movimiento.getSaldo()));
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNro, tvFecha, tvTipo, tvDescripcion, tvImporte, tvCtaRef, tvSaldo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNro = itemView.findViewById(R.id.tvNro);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvImporte = itemView.findViewById(R.id.tvImporte);
            tvCtaRef = itemView.findViewById(R.id.tvCtaRef);
            tvSaldo = itemView.findViewById(R.id.tvSaldo);
        }
    }
}