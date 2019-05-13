package kislyakov.a10_2.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kislyakov.a10_2.R;
import kislyakov.a10_2.classes.AlertReceiver;
import kislyakov.a10_2.models.Divorce;
import kislyakov.a10_2.models.Object;

import static kislyakov.a10_2.models.Divorce.DivorceConverter;
import static kislyakov.a10_2.models.Divorce.DivorceState;


public class BridgesAdapter extends RecyclerView.Adapter<BridgesAdapter.BridgesHolder> {

    List<Object> bridgeList;
    Context context;
    final OnItemClickListener listener;
    boolean alarmUp = false;


    public interface OnItemClickListener {
        void onItemClick(int positionItem);
    }

    public BridgesAdapter(List<Object> bridgeList, final Context context, OnItemClickListener listener) {
        this.bridgeList = bridgeList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public BridgesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_bridge, parent, false);
        BridgesHolder mh = new BridgesHolder(v);
        return mh;
    }

    public void updateAdapter() {

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BridgesHolder holder, int position) {

        holder.bridgeTitle.setText(bridgeList.get(position).getName());
        List<Divorce> divorces = bridgeList.get(position).getDivorces();

        String divorcesStr = DivorceConverter(divorces);
        int openState = DivorceState(divorces);

        holder.bridgeTime.setText(divorcesStr);

        alarmUp = (PendingIntent.getBroadcast(context, position,
                new Intent(this.context, AlertReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            Log.d("myTag", "Alarm is already active");
            holder.kolokolBridge.setBackgroundResource(R.drawable.ic_kolocol_on);
        } else {
            holder.kolokolBridge.setBackgroundResource(R.drawable.ic_kolocol_off);
        }


        if (openState > 0) {
            holder.ivBridgeStatus.setBackgroundResource(R.drawable.ic_brige_normal);
        } else if (openState == 0) {
            holder.ivBridgeStatus.setBackgroundResource(R.drawable.ic_brige_soon);
        } else {
            holder.ivBridgeStatus.setBackgroundResource(R.drawable.ic_brige_late);
        }
        holder.bind(position, listener);
        //Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + movieList.get(position).getPosterPath()).into(holder.ivMovie);
    }

    @Override
    public int getItemCount() {
        return bridgeList.size();
    }

    public class BridgesHolder extends RecyclerView.ViewHolder {

        TextView bridgeTitle, bridgeTime;
        Button kolokolBridge;
        ImageView ivBridgeStatus;

        public BridgesHolder(View v) {
            super(v);
            bridgeTitle = (TextView) v.findViewById(R.id.bridgeTitle);
            bridgeTime = (TextView) v.findViewById(R.id.bridgeTime);
            kolokolBridge = (Button) v.findViewById(R.id.kolokolButton);
            ivBridgeStatus = (ImageView) v.findViewById(R.id.bridgeStatusImage);

        }

        public void bind(final int positionItem, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(positionItem);
                }
            });
        }

    }


}
