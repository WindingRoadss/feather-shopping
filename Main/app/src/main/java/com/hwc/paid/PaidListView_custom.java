package com.hwc.paid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hwc.main.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PaidListView_custom extends BaseAdapter {
    private PaidListView_custom mCustomAdapter = null;
    // Activity에서 가져온 객체정보를 저장할 변수
    public PaidListView_getset lv_gst;
    public Context mContext;
    public PaidActivity pa;
    public static final String HWC = "HWC";
    public static int price_sum = 0;
    // ListView 내부 View들을 가르킬 변수들


    // 리스트 아이템 데이터를 저장할 배열
    public ArrayList<PaidListView_getset> mData;
    public Bitmap btp_test;
    public boolean flag = false;
    public int get_position;


    public static ArrayList<Boolean> data_checked = new ArrayList<>(PaidActivity.rowLength);


    public PaidListView_custom(Context context) {
        super();
        mContext = context;
        mData = new ArrayList<>();
        for (int i = 0; i < PaidActivity.rowLength; i++) {
            data_checked.add(i, false);
        }
    }

    @Override
    /**
     * @return 아이템의 총 개수를 반환
     */
    public int getCount() {
        return mData.size();
    }

    @Override
    /**
     * @return 선택된 아이템을 반환
     */
    public PaidListView_getset getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    /**
     * getView
     *
     * @param position - 현재 몇 번째로 아이템이 추가되고 있는지 정보를 갖고 있다.
     * @param convertView - 현재 사용되고 있는 어떤 레이아웃을 가지고 있는지 정보를 갖고 있다.
     * @param parent - 현재 뷰의 부모를 지칭하지만 특별히 사용되지는 않는다.
     * @return 리스트 아이템이 저장된 convertView
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 리스트 아이템이 새로 추가될 경우에는 v가 null값이다.
        // view는 어느 정도 생성된 뒤에는 재사용이 일어나기 때문에 효율을 위해서 해준다.
        if (convertView == null) {
            // inflater를 이용하여 사용할 레이아웃을 가져옵니다.
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.paid_custom_list, null);
        }
        // 레이아웃이 메모리에 올라왔기 때문에 이를 이용하여 포함된 뷰들을 참조할 수 있습니다.
        final TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        final TextView txt_size = (TextView) convertView.findViewById(R.id.txt_size);
        final TextView txt_color = (TextView) convertView.findViewById(R.id.txt_color);
        final TextView txt_brand = (TextView) convertView.findViewById(R.id.txt_brand);
        final TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
        final ImageView img_test = (ImageView) convertView.findViewById(R.id.img_test);
        final CheckBox chk_cfrm = (CheckBox) convertView.findViewById(R.id.chk_cfrm);
        final TextView txt_brdel = (TextView) convertView.findViewById(R.id.txt_brdel);
        final TextView txt_prcnt = (TextView) convertView.findViewById(R.id.txt_prcnt);

        //convertView = new NumberView( getContext(), getItem( position));


        // 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
        lv_gst = getItem(position);

        // Tag를 이용하여 데이터와 뷰를 묶습니다.
        chk_cfrm.setTag(lv_gst);

        // 데이터의 실존 여부를 판별합니다.
        if (lv_gst != null) {
            // 데이터가 있다면 갖고 있는 정보를 뷰에 알맞게 배치시킵니다.
            txt_name.setText(lv_gst.getName());
            txt_size.setText(lv_gst.getSize());
            txt_color.setText(lv_gst.getColor());
            txt_brand.setText(lv_gst.getBrand());
            txt_price.setText(lv_gst.getPrice());
            txt_brdel.setText(lv_gst.getBrdel());
            txt_prcnt.setText(lv_gst.getPrcnt());

            imageThread it = new imageThread();
            it.start();
            try {
                //  메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
                //  대기해야 하므로 작업스레드의 join() 메소드를 호출해서
                //  메인 스레드가 작업 스레드가 종료될 까지 기다리도록 합니다.
                it.join();
                //  이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
                //  UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.
                img_test.setImageBitmap(btp_test);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            chk_cfrm.setChecked(((ListView) parent).isItemChecked(position));
            chk_cfrm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                    if (checked) {
                        flag = true;
                        data_checked.set(position, true);
                    } else {
                        flag = false;
                        data_checked.set(position, false);
                    }
                }
            });

        }
        // 완성된 아이템 뷰를 반환합니다.
        return convertView;
    }

    // 데이터를 추가하는 것을 위해서 만들어 준다.
    public void add(PaidListView_getset user) {
        mData.add(user);
    }


    class imageThread extends Thread {
        @Override
        public void run() {
            try {
                /* 이 곳에 반드시 data_image의 주소가 들어가야 한다. */
                URL url = new URL(lv_gst.getImage()); // URL 주소를 이용해서 URL 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                btp_test = BitmapFactory.decodeStream(is);
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }
}