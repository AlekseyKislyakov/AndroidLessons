package kislyakov.a08_1;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kislyakov.a08_1.DataModels.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    List<Note> notesList;
    Context context;
    final OnItemClickListener itemClickListener;
    final OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(int positionItem);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public NotesAdapter(List<Note> notesList, final Context context, OnItemClickListener shortListener, OnItemLongClickListener longClickListener) {
        this.notesList = notesList;
        this.context = context;
        this.itemClickListener = shortListener;
        this.longClickListener = longClickListener;
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_note, parent, false);
        NotesHolder nh = new NotesHolder(v);
        return nh;
    }

    public void updateAdapter() {

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NotesHolder holder, int position) {

        holder.noteTitle.setText(notesList.get(position).getNoteHeader());
        String result;
        if (notesList.get(position).getNoteHeader() == null || notesList.get(position).getNoteHeader().length() == 0) {
            result = notesList.get(position).getNoteText();
        } else {
            result = "<b><big>" + notesList.get(position).getNoteHeader()
                    + "</b></big> <br />" + notesList.get(position).getNoteText();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.noteTitle.setText(Html.fromHtml(result, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.noteTitle.setText(Html.fromHtml(result));
        }
        if(notesList.get(position).getNoteColor()!=null){
            holder.itemView.setBackgroundColor(Color.parseColor(notesList.get(position).getNoteColor()));
        }
        holder.bind(position, itemClickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        if (notesList == null) {
            return 0;
        }
        return notesList.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {

        TextView noteTitle;

        public NotesHolder(View v) {
            super(v);

            noteTitle = v.findViewById(R.id.note_title);

        }

        public void bind(final int positionItem, final OnItemClickListener itemClickListener, OnItemLongClickListener longClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(positionItem);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClicked(positionItem);
                    return true;
                }
            });
        }

    }

}

