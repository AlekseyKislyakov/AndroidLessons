package kislyakov.a07_1;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kislyakov.a07_1.models.Divorce;

public class DetailObject implements Parcelable {

    final static String LOG_TAG = "myLogs";

    private String bigPictureLinkOpen= new String();
    private String bigPictureLinkClosed = new String(); // link to the opened/closed bridge depending on state
    private String bridgeName = new String(); // name on the bridge
    private List<Divorce> bridgeDivorce  = new ArrayList<>(); // string containing divorce periods// 2 = open; 1 = near; 0 = close.
    private String description= new String();


    public DetailObject(String bigPictureLinkOpen, String bigPictureLinkClosed,
                        String bridgeName, List<Divorce> bridgeDivorce, String description) {
        this.bigPictureLinkOpen = bigPictureLinkOpen;
        this.bigPictureLinkClosed = bigPictureLinkClosed;
        this.bridgeName = bridgeName;
        this.bridgeDivorce = bridgeDivorce;
        this.description = description;
    }

    public int describeContents() {
        return 0;
    }

    // упаковываем объект в Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(LOG_TAG, "writeToParcel");
        parcel.writeString(bigPictureLinkOpen);
        parcel.writeString(bigPictureLinkClosed);
        parcel.writeString(bridgeName);
        parcel.writeList(bridgeDivorce);
        parcel.writeString(description);
    }

    public static final Parcelable.Creator<DetailObject> CREATOR = new Parcelable.Creator<DetailObject>() {
        // распаковываем объект из Parcel
        public DetailObject createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel");
            return new DetailObject(in);
        }

        public DetailObject[] newArray(int size) {
            return new DetailObject[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private DetailObject(Parcel parcel) {
        Log.d(LOG_TAG, "MyObject(Parcel parcel)");
        bigPictureLinkOpen = parcel.readString();
        bigPictureLinkClosed = parcel.readString();
        bridgeName = parcel.readString();
        parcel.readList(bridgeDivorce,Divorce.class.getClassLoader());
        description = parcel.readString();
    }

    public String getPictureOpen(){
        return bigPictureLinkOpen;
    }

    public String getPictureClosed(){
        return bigPictureLinkClosed;
    }

    public String getBridgeName(){
        return bridgeName;
    }

    public List<Divorce> getDivorces(){
        return bridgeDivorce;
    }

    public String getBridgeDescription(){
        return description;
    }

}