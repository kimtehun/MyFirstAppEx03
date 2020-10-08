package com.ex.myfirstappex03.bookListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ex.myfirstappex03.R;

import java.util.ArrayList;

public class BookMarkAdapter extends BaseAdapter {

    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_bookmark, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textView1 = convertView.findViewById(R.id.title);
        TextView textView2 = convertView.findViewById(R.id.path);
        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        textView1.setText(listViewItem.getRowText1());
        textView2.setText(listViewItem.getRowText2());

        return convertView;

    }

    public void addItem(String text1, String text2){
        ListViewItem item = new ListViewItem();
        item.setRowText1(text1);
        item.setRowText2(text2);

        listViewItemList.add(item);

    }

    public void clearItem(){
        listViewItemList.clear();
    }



}
