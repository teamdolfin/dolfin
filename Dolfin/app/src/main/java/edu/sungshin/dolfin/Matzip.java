package edu.sungshin.dolfin;

import android.os.Parcel;
import android.os.Parcelable;

public class Matzip implements Parcelable {
    public String name;
    String call_num;
    int menu_kind;
    String menu1;
    String menu2;
    String menu3;
    String homepage;
    String enroll_date;
    boolean isCheckable;


    public Matzip(String name, String call_num, int menu_kind, String menu1, String menu2, String menu3, String homepage, String enroll_date) {
        this.name = name;
        this.call_num = call_num;
        this.menu_kind = menu_kind;
        this.homepage = homepage;
        this.enroll_date = enroll_date;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu3 = menu3;
        boolean isCheckable = false;


    }
    public String getName() {
        String name = this.name;
        return name;
    }

    protected Matzip(Parcel in) {
        name = in.readString();
        call_num = in.readString();
        menu_kind = in.readInt();
        menu1 = in.readString();
        menu2 = in.readString();
        menu3 = in.readString();
        homepage = in.readString();
        enroll_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(call_num);
        dest.writeInt(menu_kind);
        dest.writeString(menu1);
        dest.writeString(menu2);
        dest.writeString(menu3);
        dest.writeString(homepage);
        dest.writeString(enroll_date);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Matzip> CREATOR = new Creator<Matzip>() {
        @Override
        public Matzip createFromParcel(Parcel source) {
            return new Matzip(source);
        }

        @Override
        public Matzip[] newArray(int size) {
            return new Matzip[size];
        }


    };
}