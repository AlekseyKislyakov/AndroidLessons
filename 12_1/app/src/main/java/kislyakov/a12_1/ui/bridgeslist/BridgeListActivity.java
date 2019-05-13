package kislyakov.a12_1.ui.bridgeslist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ViewFlipper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kislyakov.a12_1.R;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.base.BaseActivity;

public class BridgeListActivity extends BaseActivity implements BridgeListMvpView {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    private static final String TAG = "myTag";
    @Inject BridgeListPresenter mMainPresenter;
    @Inject BridgeListAdapter mBridgesAdapter;

    @BindView(R.id.viewFlipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.rvBridgesList)
    RecyclerView mRecyclerView;

    @BindView(R.id.buttonRetry)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_list);
        activityComponent().inject(this);

        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mBridgesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mButton.setOnClickListener(l -> mMainPresenter.loadBridges());

        mMainPresenter.attachView(this);
        mMainPresenter.loadBridges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public void showLoadingError() {
        mViewFlipper.setDisplayedChild(VIEW_ERROR);
    }

    public void showBridges(List<Bridge> bridges){
        mBridgesAdapter.setBridgeList(bridges);
        mBridgesAdapter.notifyDataSetChanged();
        mViewFlipper.setDisplayedChild(VIEW_DATA);
    }

    @Override
    public void showSingleBridge(Bridge bridges) {

    }

    @Override
    public void showProgressView() {
        mViewFlipper.setDisplayedChild(VIEW_LOADING);
    }
}
