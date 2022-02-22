package com.example.quiterss;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
                showPopupMenu(view);
                return false;
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_longclick_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.show_detail:
                        return true;
                    case R.id.add_to_folder:
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

}
