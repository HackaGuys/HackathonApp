package com.company.samsalvail.liveinlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<HashMap<String, String>> listDataGroup; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;
    private int lastExpandedGroupPosition = -1;
    private ExpandableListView expandableListView;

    public ExpandableListAdapter(Context context, List<HashMap<String, String>> listDataGroup,
                                 HashMap<String, List<String>> listChildData, ExpandableListView expandableListView) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
        this.expandableListView = expandableListView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(Integer.toString(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ArrayList<String> child = (ArrayList<String>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listing_child, null);
        }

        TextView venue1 = (TextView) convertView.findViewById(R.id.child1);
        TextView venue2 = (TextView) convertView.findViewById(R.id.child2);
        TextView venue3 = (TextView) convertView.findViewById(R.id.child3);
        TextView venue4 = (TextView) convertView.findViewById(R.id.child4);
        TextView venue5 = (TextView) convertView.findViewById(R.id.child5);
        TextView venue6 = (TextView) convertView.findViewById(R.id.child6);
        TextView venue7 = (TextView) convertView.findViewById(R.id.child7);
        TextView venue8 = (TextView) convertView.findViewById(R.id.child8);
        TextView venue9 = (TextView) convertView.findViewById(R.id.child9);
        TextView venue10 = (TextView) convertView.findViewById(R.id.child10);

        if (child != null && child.size() > 0) {
            venue1.setText(child.get(0));
            venue2.setText(child.get(1));
            venue3.setText(child.get(2));
            venue4.setText(child.get(3));
            venue5.setText(child.get(4));
            venue6.setText(child.get(5));
            venue7.setText(child.get(6));
            venue8.setText(child.get(7));
            venue9.setText(child.get(8));
            venue10.setText(child.get(9));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HashMap<String, String> group = (HashMap<String, String>) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listing_group, null);
        }

        TextView address = (TextView) convertView.findViewById(R.id.text_address);
        TextView city = (TextView) convertView.findViewById(R.id.text_city);
        TextView province = (TextView) convertView.findViewById(R.id.text_province);
        TextView price = (TextView) convertView.findViewById(R.id.text_price);

        address.setText(group.get("address"));
        city.setText(group.get("city") + ", ");
        province.setText(group.get("province"));
        price.setText("$" + group.get("price"));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition){
        //collapse the old expanded group, if not the same
        //as new group to expand
        if(groupPosition != lastExpandedGroupPosition){
            expandableListView.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);
        lastExpandedGroupPosition = groupPosition;
    }
}
