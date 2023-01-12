package edu.sungshin.dolfin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class feed_listview extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList ;

    // feed_listview Adapter의 생성자
    public feed_listview(ArrayList<ListViewItem> itemList) {
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


        // "feed_listview" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_feed_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView myAcheive = (TextView) convertView.findViewById(R.id.my_achieve) ;
        TextView feedTitle = (TextView) convertView.findViewById(R.id.feed_title) ;
        TextView feedName = (TextView) convertView.findViewById(R.id.feed_name) ;
        TextView feedDate = (TextView) convertView.findViewById(R.id.feed_date) ;
        //Uri feedImage = (Uri) convertView.findViewById(R.id.feed_image);
        ImageView feedImage = (ImageView) convertView.findViewById(R.id.feed_image);
        //ImageView feedImage = (ImageView) convertView.findViewById(R.id.imageView_feed);
        //VideoView feedvideo = (VideoView) convertView.findViewById(R.id.videoView_feed);
        TextView feedContents = (TextView) convertView.findViewById(R.id.feed_contents) ;
        TextView feedDelete = (TextView) convertView.findViewById(R.id.feed_delete) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        feed_listview.ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        myAcheive.setText(listViewItem.getmyachieve()+" /");
        feedTitle.setText(listViewItem.getfeedtitle());
        feedName.setText(listViewItem.getfeedname());
        feedDate.setText(listViewItem.getfeeddate());
        feedImage.setImageURI(listViewItem.getimage());
        //feedvideo.setVideoURI(listViewItem.getvideo());
        feedContents.setText(listViewItem.getfeedcontents());
        feedDelete.setText(listViewItem.getfeeddelete());

        // 피드 삭제 버튼 클릭 이벤트
        feedDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("글을 삭제하시겠습니까?");
                builder.setCancelable(true);
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }




    //siississ

    public ArrayList<feed_listview.ListViewItem> getItemList() {
        return listViewItemList ;
    }

    public ArrayList<String> getNameList() {
        ArrayList<ListViewItem> temp = getItemList();
        ArrayList<String> feedtitleList = new ArrayList<>();
        for (int i=0; i<temp.size(); i++) {
            feedtitleList.add(temp.get(i).getfeedtitle());
        }
        return feedtitleList;
    }

    public void addItem(String myachieve, String feedtitle, String feedname, String feeddate, Uri image, String feedcontents, String feeddelete,Uri v) {
        ListViewItem item = new ListViewItem();
        item.setmyachieve(myachieve);
        item.setfeedtitle(feedtitle);
        item.setfeedname(feedname);
        item.setfeeddate(feeddate);
        item.setimage(image);
        //item.setVideoView3(v);
        item.setfeedcontents(feedcontents);
        item.setfeeddelete(feeddelete);
        //listViewItemList.add(item);
    }

    public static class ListViewItem {
        private String myachieve;
        private String feedtitle;
        private String feedname;
        private String feeddate;
        private Uri image;
        private String feedcontents;
        private String feeddelete;
        //private Uri videoView3;

        public ListViewItem() {
        }

        public void setmyachieve(String myachieve) { this.myachieve =  myachieve; }
        public void setfeedtitle(String feedtitle) { this.feedtitle = feedtitle; }
        public void setfeedname(String feedname) { this.feedname = feedname; }
        public void setfeeddate(String feeddate) { this.feeddate = feeddate; }
        public void setimage(Uri image){ this.image = image;}
        //public void setVideoView3(Uri videoView3){ this.videoView3 = videoView3;}
        public void setfeedcontents(String feedcontents) { this.feedcontents = feedcontents; }
        public void setfeeddelete(String feeddelete) { this.feeddelete = feeddelete; }

        public String getmyachieve() { return myachieve; }
        public String getfeedtitle() { return feedtitle; }
        public String getfeedname() { return feedname; }
        public String getfeeddate() { return feeddate; }
        public Uri getimage() {return image; }
        //public Uri getvideo(){return videoView3;}
        public String getfeedcontents() { return feedcontents; }
        public String getfeeddelete() { return feeddelete; }



        public void addItem(String myachieve, String feedtitle, String feedname, String feeddate, Uri image, String feedcontents, String feeddelete,Uri v) {
            ListViewItem item = new ListViewItem();
            item.setmyachieve(myachieve);
            item.setfeedtitle(feedtitle);
            item.setfeedname(feedname);
            item.setfeeddate(feeddate);
            item.setimage(image);
            //item.setVideoView3(v);
            item.setfeedcontents(feedcontents);
            item.setfeeddelete(feeddelete);
            //listViewItemList.add(item);
        }
    }
}