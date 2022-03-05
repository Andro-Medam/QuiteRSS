package com.example.quiterss;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private String toSearch;
    private List<String> searchItem;

    public RecyclerviewAdapter(Context context, String s){
        this.mContext = context;
        toSearch = s;
        mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.tv.setText(searchItem.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "you clicked search", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ReadActivity.class);
                intent.putExtra("title", searchItem.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        searchItem = mySQLiteOpenHelper.QueryItemByName(toSearch);
        return searchItem.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_name);
        }
    }
}
