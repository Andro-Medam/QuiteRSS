package com.example.quiterss.mainPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiterss.MySQLiteOpenHelper;
import com.example.quiterss.R;
import com.example.quiterss.readPage.ReadActivity;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Folder_item;

import java.util.ArrayList;
import java.util.List;

public class ExpandedAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private List<String> group;
    private List<List<String>> child;
    private Context mContext;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    public ExpandedAdapter(Context context, List<String> group, List<List<String>> child){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.group = group;
        this.child = child;
        mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext);
    }

    public ExpandedAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        child = new ArrayList<List<String>>();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(mContext);
        group = mySQLiteOpenHelper.QueryAllFolder();

        for(int i = 0; i < group.size(); i++){
            child.add(mySQLiteOpenHelper.QueryItemByFolder(group.get(i)));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.elv_item_header, null);
        ImageView iv = view.findViewById(R.id.folder_expanded);
        TextView tv = view.findViewById(R.id.folder_name);
        tv.setText(group.get(groupPosition));
        if (isExpanded) {
            iv.setImageResource(R.mipmap.arrow_up_bold);
        }
        else {
            iv.setImageResource(R.mipmap.arrow_down);
        }
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenuGroup(view, groupPosition);
                return false;
            }
        });
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.elv_item_child, null);
        TextView tv = view.findViewById(R.id.item_name);
        try {
            tv.setText(child.get(groupPosition).get(childPosition));
        }catch (Exception e){
            Log.d("expanded", "ArrayOutOfBound");
        }
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenuItem(view, groupPosition, childPosition);
                return false;
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReadActivity.class);
                intent.putExtra("title", child.get(groupPosition).get(childPosition));
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void showPopupMenuGroup(View view, int groupPosition){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.group_longclick_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                switch (menuItem.getItemId()){
                    case R.id.delete_folder:
                        View delete_dialog = mInflater.inflate(R.layout.delete_folder_dialog, null);
                        CheckBox checkBox = delete_dialog.findViewById(R.id.delete_items_cb);
                        ArrayList<CompoundButton> selected = new ArrayList<>();
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b)
                                    selected.add(compoundButton);
                                else
                                    selected.remove(compoundButton);
                            }
                        });
                        builder.setView(delete_dialog)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mySQLiteOpenHelper.DeleteFolderByName(group.get(groupPosition));
                                        if (selected.size() > 0){
                                            mySQLiteOpenHelper.DeleteItemByChannel(group.get(groupPosition));
                                        }
                                        mySQLiteOpenHelper.DeleteFIByFolderName(group.get(groupPosition));
                                        child.remove(groupPosition);
                                        group.remove(groupPosition);
                                    }
                                })
                                .show();
                        return true;
                    case R.id.rename_folder:
                        View rename_dialog = mInflater.inflate(R.layout.rename_folder_dialog, null);
                        builder.setView(rename_dialog)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText editText = rename_dialog.findViewById(R.id.rename_folder_et);
                                        if (editText.getText().toString().equals(""))
                                            Toast.makeText(mContext, "文件夹名称不能为空", Toast.LENGTH_SHORT).show();
                                        else {
                                            if (mySQLiteOpenHelper.UpdateFolderByName(group.get(groupPosition), editText.getText().toString()) != -1){
                                                group.set(groupPosition, editText.getText().toString());
                                                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(mContext, "文件夹名称修改错误，请重新检查！", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showPopupMenuItem(View view, int groupPosition, int childPosition){
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
                    folder_item.setItemName(child.get(groupPosition).get(childPosition));
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
