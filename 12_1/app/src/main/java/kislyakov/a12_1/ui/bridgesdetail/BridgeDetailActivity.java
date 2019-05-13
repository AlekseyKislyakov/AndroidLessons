package kislyakov.a12_1.ui.bridgesdetail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kislyakov.a12_1.R;
import kislyakov.a12_1.data.models.Bridge;
import kislyakov.a12_1.ui.base.BaseActivity;
import kislyakov.a12_1.util.DivorceUtil;

public class BridgeDetailActivity extends BaseActivity implements BridgeDetailMvpView {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    private static final String TAG = "myTag";

    @Inject
    BridgeDetailPresenter mBridgeDetailPresenter;

    @BindView(R.id.viewFlipperDetail)
    ViewFlipper mViewFlipperDetail;
    @BindView(R.id.buttonRetryDetail)
    Button mButtonDetail;

    @BindView(R.id.toolbarDetail)
    Toolbar mToolbar;

    @BindView(R.id.nameDetailTextView)
    TextView mNameTextview;
    @BindView(R.id.divorceTextView)
    TextView mDivorceTextView;
    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @BindView(R.id.detailBridgePictureImageView)
    ImageView mDetailBridgeImageView;
    @BindView(R.id.statusSmallImageView)
    ImageView mStatusSmallImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_detail);
        activityComponent().inject(this);

        mBridgeDetailPresenter.attachView(this);

        Intent parentIntent = getIntent();
        setIntent(parentIntent);

        ButterKnife.bind(this);


        int bridgeId = parentIntent.getIntExtra("BridgeID", 0);
        mBridgeDetailPresenter.loadSingleBridge(bridgeId);

        mButtonDetail.setOnClickListener(l -> mBridgeDetailPresenter.loadSingleBridge(bridgeId));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBridgeDetailPresenter.detachView();
    }

    @Override
    public void showLoadingError() {
        mViewFlipperDetail.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public void showBridges(List<Bridge> bridges) {

    }

    @Override
    public void showSingleBridge(Bridge bridge) {

        mNameTextview.setText(bridge.getName());
        mDivorceTextView.setText(DivorceUtil.divorceToString(bridge.getDivorces()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDescriptionTextView.setText(Html.fromHtml(bridge.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mDescriptionTextView.setText(Html.fromHtml(bridge.getDescription()));
        }

        int openState = DivorceUtil.divorceState(bridge.getDivorces());

        if(openState == DivorceUtil.STATE_OPEN){
            mStatusSmallImageView.setBackgroundResource(R.drawable.ic_brige_normal);
            Glide.with(this)
                    .load("http://gdemost.handh.ru/" + bridge.getPhotoOpen())
                    .into(mDetailBridgeImageView);
        }
        else if(openState == DivorceUtil.STATE_NEAR){
            mStatusSmallImageView.setBackgroundResource(R.drawable.ic_brige_soon);
            Glide.with(this)
                    .load("http://gdemost.handh.ru/" + bridge.getPhotoOpen())
                    .into(mDetailBridgeImageView);
        }
        else {
            mStatusSmallImageView.setBackgroundResource(R.drawable.ic_brige_late);
            Glide.with(this)
                    .load("http://gdemost.handh.ru/" + bridge.getPhotoClose())
                    .into(mDetailBridgeImageView);
        }
        mViewFlipperDetail.setDisplayedChild(VIEW_DATA);
    }

    @Override
    public void showProgressView() {
        mViewFlipperDetail.setDisplayedChild(VIEW_LOADING);
    }
}
