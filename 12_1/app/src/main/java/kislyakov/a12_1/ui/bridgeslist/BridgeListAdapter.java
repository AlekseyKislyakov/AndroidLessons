package kislyakov.a12_1.ui.bridgeslist;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kislyakov.a12_1.R;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.bridgesdetail.BridgeDetailActivity;
import kislyakov.a12_1.util.DivorceUtil;

public class BridgeListAdapter extends RecyclerView.Adapter<BridgeListAdapter.BridgeViewHolder> {

    List<Bridge> mBridgeList;
    View.OnClickListener mOnItemClickListener;
    private static final String TAG = "myTag";

    @Inject
    public BridgeListAdapter() {
        mBridgeList = new ArrayList<>();
        mOnItemClickListener = v -> {
            RecyclerView.ViewHolder bridgeVH = (RecyclerView.ViewHolder) v.getTag();
            Bridge thisItem = mBridgeList.get(bridgeVH.getAdapterPosition());
            Log.d(TAG, "onClick: " + thisItem.getName());
            Intent intent = new Intent(v.getContext(), BridgeDetailActivity.class);
            intent.putExtra("BridgeID", thisItem.getId());
            v.getContext().startActivity(intent);
        };
    }

    public void setBridgeList(List<Bridge> bridgeList) {
        mBridgeList = bridgeList;
    }


    @NonNull
    @Override
    public BridgeListAdapter.BridgeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_bridge, viewGroup, false);
        return new BridgeViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull BridgeViewHolder bridgeViewHolder, int i) {
        Bridge bridge = mBridgeList.get(i);
        bridgeViewHolder.bridgeNameTextView.setText(bridge.getName());
        bridgeViewHolder.bridgeDivorceTextView.setText(DivorceUtil.divorceToString(bridge.getDivorces()));


        int openState = DivorceUtil.divorceState(bridge.getDivorces());

        if(openState == DivorceUtil.STATE_OPEN){
            bridgeViewHolder.bridgeStatusImageView.setBackgroundResource(R.drawable.ic_brige_normal);
        }
        else if(openState == DivorceUtil.STATE_NEAR){
            bridgeViewHolder.bridgeStatusImageView.setBackgroundResource(R.drawable.ic_brige_soon);
        }
        else {
            bridgeViewHolder.bridgeStatusImageView.setBackgroundResource(R.drawable.ic_brige_late);
        }
    }


    @Override
    public int getItemCount() {
        return mBridgeList.size();
    }

    class BridgeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bridgeStatusImage)
        ImageView bridgeStatusImageView;
        @BindView(R.id.bridgeName)
        TextView bridgeNameTextView;
        @BindView(R.id.bridgeDivorces)
        TextView bridgeDivorceTextView;

        public BridgeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }


    }
}
