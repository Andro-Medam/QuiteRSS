package com.example.quiterss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandedAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mInflater;
    private String[] group;
    private String[][] child;

    public ExpandedAdapter(Context context, String[] group, String[][] child){
        mInflater = LayoutInflater.from(context);
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child[groupPosition][childPosition];
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
        tv.setText(group[groupPosition]);
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
        tv.setText(child[groupPosition][childPosition]);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
