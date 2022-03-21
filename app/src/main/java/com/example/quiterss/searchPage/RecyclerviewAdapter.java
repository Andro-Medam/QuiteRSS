package com.example.quiterss.searchPage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiterss.MySQLiteOpenHelper;
import com.example.quiterss.R;
import com.example.quiterss.bean.Folder_item;
import com.example.quiterss.readPage.ReadActivity;

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

        holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenuItem(view, position);
                return false;
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

    private void showPopupMenuItem(View view, int Position){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_longclick_menu, popupMenu.getMenu());
        SubMenu subMenu = popupMenu.getMenu().addSubMenu("添加到文件夹");
        List<String> userFolder = mySQLiteOpenHelper.QueryFolderByUserOrSys("1");
        for(int i = 0; i < userFolder.size(); i++){
            subMenu.add(1, i+1, 1,userFolder.get(i));
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getGroupId() == 1){
                    int f = menuItem.getItemId();
                    Log.d("aaaaaaaaaaaaaa", "onMenuItemClick: " + f);
                    Folder_item folder_item = new Folder_item();
                    folder_item.setFolderName(userFolder.get(f-1));
                    folder_item.setItemName(searchItem.get(Position));
                    if (mySQLiteOpenHelper.InsertFolderItem(folder_item) == -1){
                        Toast.makeText(mContext, "该文件已经存在！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });
        popupMenu.show();
    }
}
