package kislyakov.a07_1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import kislyakov.a07_1.R;
import kislyakov.a07_1.models.Divorce;
import kislyakov.a07_1.models.Object;

/**
 * Created by anujgupta on 26/12/17.
 */

public class BridgesAdapter extends RecyclerView.Adapter<BridgesAdapter.BridgesHolder> {

    List<Object> bridgeList;
    Context context;
    final OnItemClickListener listener;

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

    @Override
    public void onBindViewHolder(BridgesHolder holder, int position) {

        holder.bridgeTitle.setText(bridgeList.get(position).getName());
        List<Divorce> divorces = bridgeList.get(position).getDivorces();
        String divorcesStr = "";
        int state = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date start = new Date(System.currentTimeMillis());
        Date stop = new Date(System.currentTimeMillis());
        Date now = Calendar.getInstance().getTime();


        for (int i = 0; i < divorces.size(); i++)
        {
            try {
                start = sdf.parse(divorces.get(i).getStart());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                stop = sdf.parse(divorces.get(i).getEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            if(stop.getTime() > ((now.getTime()+10800000)%86400000) && start.getTime() - ((now.getTime()+10800000)%86400000) < 3600000)
            {
                state -= 1; // желтый мост
            }

            if(stop.getTime() > ((now.getTime()+10800000)%86400000) && ((now.getTime()+10800000)%86400000) > start.getTime())
            {
                state -= 2; // красный мост
            }

            //to convert Date to String, use format method of SimpleDateFormat class.
            divorcesStr += dateFormat.format(start);
            divorcesStr += " - ";
            divorcesStr += dateFormat.format(stop);
            divorcesStr += "   ";
        }

        holder.bridgeTime.setText(divorcesStr);

        holder.kolokolBridge.setBackgroundResource(R.drawable.ic_kolocol_on);
        if(state > 0){
            holder.ivBridgeStatus.setBackgroundResource(R.drawable.ic_brige_normal);
        }
        else if(state == 0){
            holder.ivBridgeStatus.setBackgroundResource(R.drawable.ic_brige_soon);
        }
        else {
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
