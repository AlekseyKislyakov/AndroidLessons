package kislyakov.a04_2;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NumbersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int itemsCount;
    public String[] headers;
    public String[] headersB;
    public String[] descriptions;
    public Drawable[] avatars;
    public Drawable[] avatarsB;
    public Context parent;



    static final int DESCRIPTION_RED = 1;
    static final int ONEINTWO = 2;
    static final int BASECELL = 3;
    static final int DEFAULT = 0;

    public interface OnItemClickListener {
        void onItemClick(String item, View view);
    }

    private final OnItemClickListener listener;


    public NumbersAdapter(int items, OnItemClickListener listener) {
        itemsCount = items;
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();

        switch (viewType) {
            case BASECELL: {
                int layoutIdForListItem = R.layout.base_info_item;


                LayoutInflater inflater = LayoutInflater.from(context);

                View view = inflater.inflate(layoutIdForListItem, null);
                NumberViewHolderBase viewHolder = new NumberViewHolderBase(view);
                Resources resources = context.getResources();
                headersB = resources.getStringArray(R.array.headers_base);

                TypedArray a = resources.obtainTypedArray(R.array.avatars_base);
                avatarsB = new Drawable[a.length()];
                for (int j = 0; j < avatarsB.length; j++) {
                    avatarsB[j] = a.getDrawable(j);
                }
                a.recycle();
                return viewHolder;
            }
            default:{
                int layoutIdForListItem = R.layout.detail_info_item;

                LayoutInflater inflater = LayoutInflater.from(context);

                View view = inflater.inflate(layoutIdForListItem, null);

                NumberViewHolder viewHolder = new NumberViewHolder(view);
                Resources resources = context.getResources();
                headers = resources.getStringArray(R.array.headers);
                descriptions = resources.getStringArray(R.array.descriptions);

                TypedArray a = resources.obtainTypedArray(R.array.avatars);
                avatars = new Drawable[a.length()];
                for (int j = 0; j < avatars.length; j++) {
                    avatars[j] = a.getDrawable(j);
                }
                a.recycle();
                return viewHolder;
            }

        }

        //viewHolder.descListTextView.setText("LOL");
        // viewHolder.headerListTextView.setText("LIL");
        //viewHolder.imageListItemView.setImageResource(R.drawable.ic_homephone);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            int type = NumbersAdapter.this.getItemViewType(i);


            switch (type) {
                case DESCRIPTION_RED:
                    NumberViewHolder viewHolderDetail = (NumberViewHolder)viewHolder;
                    viewHolderDetail.bindRed(avatars[i], headers[i], descriptions[i], listener);
                    break;
                case ONEINTWO:
                    NumberViewHolder viewHolderDetail2 = (NumberViewHolder)viewHolder;
                    viewHolderDetail2.bindOneInTwo(avatars[i], headers[i], descriptions[i], listener);
                    break;
                case BASECELL:
                    NumberViewHolderBase viewHolderBase = (NumberViewHolderBase)viewHolder;
                    viewHolderBase.bind(avatarsB[i-7], headersB[i-7], listener);
                    break;
                default:
                    NumberViewHolder viewHolderDetail3 = (NumberViewHolder)viewHolder;
                    viewHolderDetail3.bind(avatars[i], headers[i], descriptions[i], listener);
                    break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        if (position == 0 || position == 1) return DESCRIPTION_RED;
        if (position == 6) return ONEINTWO;
        if (position >= 7) return BASECELL;
        return DEFAULT;
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        ImageView imageListItemView;
        TextView headerListTextView;
        TextView descListTextView;

        public NumberViewHolder(@NonNull View itemView) {

            super(itemView);
            imageListItemView = itemView.findViewById(R.id.header_pic);
            headerListTextView = itemView.findViewById(R.id.header_tv);
            descListTextView = itemView.findViewById(R.id.header_desc_tv);
        }

        void bind(Drawable imageResourse, final String header, String description, final OnItemClickListener listener) {
            imageListItemView.setImageDrawable(imageResourse);
            headerListTextView.setText(header);
            descListTextView.setText(description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(header, v);
                }
            });
        }

        void bindRed(Drawable imageResourse, final String header, String description, final OnItemClickListener listener) {
            imageListItemView.setImageDrawable(imageResourse);
            headerListTextView.setText(header);
            descListTextView.setTextColor(Color.parseColor("#FF0000"));
            descListTextView.setText(description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(header, v);
                }
            });
        }

        void bindOneInTwo(Drawable imageResourse, final String header, String description, final OnItemClickListener listener) {
            imageListItemView.setImageDrawable(imageResourse);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headerListTextView.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.header_pic);
            headerListTextView.setLayoutParams(params); //causes layout update

            params = (RelativeLayout.LayoutParams) descListTextView.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, R.id.header_pic);
            //params.addRule(RelativeLayout.BELOW, R.id.header_tv);
            descListTextView.setLayoutParams(params);

            headerListTextView.setText(header);
            descListTextView.setText(description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(header, v);
                }
            });
        }
    }

    class NumberViewHolderBase extends RecyclerView.ViewHolder {

        ImageView imageListItemView;
        TextView headerListTextView;

        public NumberViewHolderBase(@NonNull View itemView) {

            super(itemView);

            imageListItemView = itemView.findViewById(R.id.header_pic_base);
            headerListTextView = itemView.findViewById(R.id.header_tv_base);

        }


        void bind(Drawable imageResourse, final String header, final OnItemClickListener listener) {
            imageListItemView.setImageDrawable(imageResourse);
            headerListTextView.setText(header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(header, v);
                }
            });


        }
    }
}
