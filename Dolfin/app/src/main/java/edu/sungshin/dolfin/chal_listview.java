package edu.sungshin.dolfin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class chal_listview extends BaseAdapter {
    interface Listener {
        void onItemClickedAT(int position);
    }

    private Listener listener;

    public void setListener(Listener listener){
        this.listener = listener;
    }

    private ArrayList<ListViewItem> listViewItemList ;

    // chal_listview Adapter의 생성자
    public chal_listview(ArrayList<ListViewItem> itemList) {
        if (itemList == null) {
            listViewItemList = new ArrayList<ListViewItem>() ;
        } else {
            listViewItemList = itemList ;
        }
    }
    public int getCount() {
        return listViewItemList.size() ;
    }
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }
    public long getItemId(int position) {
        return position ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "chal_listview" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_chal_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView chalName = (TextView) convertView.findViewById(R.id.chal_name) ;
        TextView chalMemNum = (TextView) convertView.findViewById(R.id.chal_memNum) ;
        TextView chalMemMax = (TextView) convertView.findViewById(R.id.chal_memMax) ;
        TextView chalStrDate = (TextView) convertView.findViewById(R.id.chal_startDate) ;
        TextView chalEndDate = (TextView) convertView.findViewById(R.id.chal_endDate) ;
        TextView chalWeekCnt = (TextView) convertView.findViewById(R.id.chal_weekCnt) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        chalName.setText(listViewItem.getName());
        chalMemNum.setText(Integer.toString(listViewItem.getmNum())+" /");
        chalMemMax.setText(Integer.toString(listViewItem.getmMax()));
        chalStrDate.setText(listViewItem.getStr()+" ~");
        chalEndDate.setText(listViewItem.getEnd());
        chalWeekCnt.setText(Integer.toString(listViewItem.getCnt()));
        return convertView;
    }

    public ArrayList<ListViewItem> getItemList() {
        return listViewItemList ;
    }

    public ArrayList<String> getNameList() {
        ArrayList<ListViewItem> temp = getItemList();
        ArrayList<String> nameList = new ArrayList<>();
        for (int i=0; i<temp.size(); i++) {
            nameList.add(temp.get(i).getName());
        }
        return nameList;
    }

    public void addItem(String chal_name, int member_num, int member_max, String start_date, String end_date, int count_week) {
        ListViewItem item = new ListViewItem();
        item.setName(chal_name);
        item.setmNum(Math.toIntExact(member_num));
        item.setmMax(Math.toIntExact(member_max));
        item.setStr(start_date);
        item.setEnd(end_date);
        item.setCnt(Math.toIntExact(count_week));
        //listViewItemList.add(item);
    }

    public static class ListViewItem {
        private String name;
        private int mNum;
        private int mMax;
        private String str;
        private String end;
        private int cnt;

        public ListViewItem() {
        }

        public void setName(String name) {
            this.name =  name;
        }
        public void setmNum(int mNum) {
            this.mNum =  mNum;
        }
        public void setmMax(int mMax) {
            this.mMax =  mMax;
        }
        public void setStr(String str) {
            this.str =  str;
        }
        public void setEnd(String end) {
            this.end =  end;
        }
        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public String getName() {
            return name;
        }
        public int getmNum() {
            return mNum;
        }
        public int getmMax() {
            return mMax;
        }
        public String getStr() {
            return str;
        }
        public String getEnd() {
            return end;
        }
        public int getCnt() {
            return cnt;
        }

        public void addItem(String chal_name, Long member_num, Long member_max, String start_date, String end_date, Long count_week) {
            ListViewItem item = new ListViewItem();
            item.setName(chal_name);
            item.setmNum(Math.toIntExact(member_num));
            item.setmMax(Math.toIntExact(member_max));
            item.setStr(start_date);
            item.setEnd(end_date);
            item.setCnt(Math.toIntExact(count_week));
            //listViewItemList.add(item);
        }
    }
}