package kislyakov.a04_2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder> {

    private int itemsCount;

    public NumbersAdapter(int numberOfItems) {
        itemsCount = numberOfItems;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_tile;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup);

        NumberViewHolder viewHolder = new NumberViewHolder(view);
        viewHolder.descListTextView.setText("LOL");
        viewHolder.headerListTextView.setText("LIL");
        viewHolder.imageListItemView.setImageResource(R.drawable.ic_homephone);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder numberViewHolder, int i) {
        numberViewHolder.bind(R.drawable.ic_homephone, "LOL","LIL");
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

        void bind(int imageResourse, String header, String description) {
            imageListItemView.setImageResource(imageResourse);
            headerListTextView.setText(header);
            descListTextView.setText(description);
        }
    }
}
