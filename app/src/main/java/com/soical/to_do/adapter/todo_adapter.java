package com.soical.to_do.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soical.to_do.Notes_screen;
import com.soical.to_do.R;
import com.soical.to_do.databackend.data;
import com.soical.to_do.databackend.dbhelper;

import java.util.ArrayList;
import java.util.Collection;

public class  todo_adapter extends RecyclerView.Adapter<todo_adapter.ViewHolder>  implements Filterable {


    private ArrayList dis;
    private ArrayList time_;
    private ArrayList title;
    private ArrayList uniqueKEY;
    public Context context;
    private int size ;

    private OnClickListener mlistener;
    private OnLongClickListener nlistener;

    Filter filter = new Filter() {

        ArrayList filteredDis , filteredTitle , filteredTime , filteredId ;

        void initilize(){
            filteredDis = new ArrayList();
            filteredId = new ArrayList();
            filteredTime = new ArrayList();
            filteredTitle = new ArrayList();
        }
        void Cleared(){


            filteredTitle.clear();
            filteredTime.clear();
            filteredDis.clear();
            filteredId.clear();

        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // var need to get store the filtered output
            initilize();
            Cleared();

           System.out.println("###"+constraint.toString()+"###");

            if(constraint.toString().isEmpty() || constraint.toString().equals("") || constraint.toString().equals(null)){

                new dbhelper(context).booting_process();
                notifyDataSetChanged();
                Log.i("Constraint is empty","1 condition is called");

            }else{
                Log.i("Constraint is not empty","2 condition is called");
                System.out.println(data.todo_dis.size());
                new dbhelper(context).booting_process();
                for(int i = 0 ; i < data.todo_dis.size() ; i++) {

                    System.out.println(data.todo_dis.get(i).toString());
                    if (data.todo_dis.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())
                                                                    ||
                            data.todo_title.get(i).toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredDis.add(data.todo_dis.get(i));
                        filteredTitle.add(data.todo_title.get(i));
                        filteredTime.add(data.todo_time.get(i));
                        filteredId.add(data.todo_id.get(i));
                    }
                }
                System.out.println(filteredDis.toString());
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredDis;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
                    dataCleared();
                    dis.addAll( (Collection) results.values);
                    title.addAll(filteredTitle);
                    time_.addAll(filteredTime);
                    uniqueKEY.addAll(filteredId);
                    notifyDataSetChanged();
                ImageView img = Notes_screen.img;
                if(filteredTitle.isEmpty()){

                    img.setVisibility(View.VISIBLE);

                }   else{

                    img.setVisibility(View.INVISIBLE);
                }

                new Notes_screen().updating_recycler();

            }
            catch (Exception e){
                System.out.println(e.getCause());


            }



        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public interface OnClickListener {

        public void onItemClick(int postion);

    }

    public interface OnLongClickListener {
        public void onItemLongClick(int position);

    }

    public void setOnClickListener(OnClickListener listener) {
        mlistener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        nlistener = listener;
    }

    // this is a ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {


        // discription
        TextView tv;
        TextView time;
        CheckBox checkBox;
        // title
        TextView tv_title_;

        public ViewHolder(View view, OnClickListener listener, OnLongClickListener longClickListener) {
            super(view);
            tv_title_ = view.findViewById(R.id.tv_title);
            tv = view.findViewById(R.id.dis_tv);
            time = view.findViewById(R.id.time_);
            view.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public todo_adapter(Context context) {

        this.context = context;
        dis = new ArrayList();
        dis = data.todo_dis;

        title = new ArrayList();
        title = data.todo_title;

        time_ = new ArrayList();
        time_ = data.todo_time;

        uniqueKEY = new ArrayList();
        uniqueKEY = data.todo_id;

        int size = data.todo_dis.size();


    }

    // generic
    // lamada


    @NonNull
    @Override
    public todo_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_notes, parent, false);

        todo_adapter.ViewHolder viewHolder = new todo_adapter.ViewHolder(view, mlistener, nlistener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull todo_adapter.ViewHolder holder, int position) {
        try {
            holder.tv.setText(dis.get(position).toString());
            holder.tv_title_.setText(title.get(position).toString());
            holder.time.setText(time_.get(position).toString());
        }
        catch (Exception e){
            System.out.println(e.getCause());
        }
    }

    public void dataCleared() {
        dis.clear();
        title.clear();
        time_.clear();
        uniqueKEY.clear();

    }

    public void update_specific(int pos) {
        notifyItemChanged(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

}
