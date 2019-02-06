package com.example.notepad;

import android.content.*;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> implements Filterable {
    private Context mContext;
    List<Notes> notes;
    List<Notes> fullNotes;

    public NotesAdapter(List<Notes> notes) {
        this.notes = notes;
        fullNotes = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.notes_layout, parent, false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, final int position) {
        final Notes note = notes.get(position);
        holder.setDetails(note.getTitle(), note.getDesc());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, UpdateNoteActivity.class );
                i.putExtra("id",  notes.get(position).getId() +"");
                i.putExtra("title", notes.get(position).getTitle());
                i.putExtra("desc", notes.get(position).getDesc());
                mContext.startActivity(i);

                }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Notes> filterNotes = new ArrayList<>();
            if (constraint.length() == 0 || constraint == null){
                filterNotes.addAll(fullNotes);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Notes notes : fullNotes){
                    if(notes.getDesc().contains(filterPattern)){
                        filterNotes.add(notes);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterNotes;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class NotesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NotesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String title, String desc) {
            TextView titleTxt = mView.findViewById(R.id.notes_title);
            TextView descTxt = mView.findViewById(R.id.notes_desc);
            titleTxt.setText(title);
            if (desc.length() > 100) {
                String subs = desc.substring(0, 100) + "...";
                descTxt.setText(subs);
            } else {
                descTxt.setText(desc);
            }
        }

    }
}
