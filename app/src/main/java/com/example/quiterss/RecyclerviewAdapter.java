package com.example.quiterss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private String toSearch;
    private String[] searchItem;

    public RecyclerviewAdapter(Context context, String s){
        this.mContext = context;
        toSearch = s;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.tv.setText(searchItem[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "you clicked search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext);
        searchItem = mySQLiteOpenHelper.QueryItemByName(toSearch);
        return searchItem.length;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView tv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_name);
        }
    }
}
