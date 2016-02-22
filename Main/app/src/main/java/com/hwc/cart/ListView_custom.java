package com.hwc.cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

/**
 * Created by hyunwoo794 on 2016-01-18.
 */

public class ListView_custom extends BaseAdapter {
    private ListView_custom mCustomAdapter = null;
    // Activity에서 가져온 객체정보를 저장할 변수
    public ListView_getset lv_gst;
    public Context mContext;
    public CartActivity ca;
    public static final String HWC = "HWC_num";


    // ListView 내부 View들을 가르킬 변수들
    public TextView txt_name;
    public TextView txt_size;
    public TextView txt_color;
    public TextView txt_brand;
    public TextView txt_price;
    //public TextView txt_eachsize[] = new TextView[ca.cart.length()];
    public TextView txt_eachsize[] = new TextView[CartActivity.rowLength];
    public ImageView img_test;
    public CheckBox chk_add;
    public String url_image;
    // 리스트 아이템 데이터를 저장할 배열
    public ArrayList<ListView_getset> mData;
    public Bitmap btp_test;
    public Button bt_add;
    public Button bt_minus;

    // +, -
    public int count[] = new int[CartActivity.rowLength];
    public int temp_sum[] = new int[CartActivity.rowLength];
    public static int price_sum = 0;
    public boolean[] flag = new boolean[CartActivity.rowLength];

    //public static ArrayList<Boolean> data_checked = new ArrayList<>(CartActivity.cart.length());
    public static ArrayList<Boolean> data_checked = new ArrayList<>(CartActivity.rowLength);


    public ListView_custom(Context context) {
        super();
        mContext = context;
        mData = new ArrayList<>();
        /*for(int i=0;i<CartActivity.cart.length();i++) {
            data_checked.add(i,false);
        }*/
        for(int i=0;i<CartActivity.rowLength;i++) {
            data_checked.add(i,false);
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
    public ListView_getset getItem(int position) {
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
        View v = convertView;
        // 리스트 아이템이 새로 추가될 경우에는 v가 null값이다.
        // view는 어느 정도 생성된 뒤에는 재사용이 일어나기 때문에 효율을 위해서 해준다.
        if (v == null) {
            // inflater를 이용하여 사용할 레이아웃을 가져옵니다.
            v = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.custom_list, null);

            // 레이아웃이 메모리에 올라왔기 때문에 이를 이용하여 포함된 뷰들을 참조할 수 있습니다.
            txt_name = (TextView) v.findViewById(R.id.txt_name);
            txt_size = (TextView) v.findViewById(R.id.txt_size);
            txt_color = (TextView) v.findViewById(R.id.txt_color);
            txt_brand = (TextView) v.findViewById(R.id.txt_brand);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            txt_eachsize[position] = (TextView) v.findViewById(R.id.txt_eachsize);
            //url_image = (TextView) v.findViewById(R.id.url_image);
            //btnSend = (Button) v.findViewById(R.id.bt_detail);
            bt_add = (Button) v.findViewById(R.id.bt_add);
            bt_minus = (Button) v.findViewById(R.id.bt_minus);
            img_test = (ImageView) v.findViewById(R.id.img_test);
            chk_add = (CheckBox) v.findViewById(R.id.chk_add);

        }

        // 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
        lv_gst = getItem(position);

        // Tag를 이용하여 데이터와 뷰를 묶습니다.
        chk_add.setTag(lv_gst);
        bt_add.setTag(lv_gst);
        txt_eachsize[position].setTag(lv_gst);
        bt_minus.setTag(lv_gst);

        // 데이터의 실존 여부를 판별합니다.
        if (lv_gst != null) {
            // 데이터가 있다면 갖고 있는 정보를 뷰에 알맞게 배치시킵니다.
            txt_name.setText(lv_gst.getName());
            txt_size.setText(lv_gst.getSize());
            txt_color.setText(lv_gst.getColor());
            txt_brand.setText(lv_gst.getBrand());
            txt_price.setText(lv_gst.getPrice());
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

            for (int i = 0; i < CartActivity.rowLength; i++) {
                count[i] = 0;
            }
            for (int i = 0; i < CartActivity.rowLength; i++) {
                temp_sum[i] = 0;
            }
            for (int i = 0; i < CartActivity.rowLength; i++) {
                flag[i] = false;
            }

            chk_add.setChecked(((ListView) parent).isItemChecked(position));
            chk_add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                    if (checked) {
                        flag[position] = true;
                        data_checked.set(position, true);
                        Log.d(HWC, "[" + position + "번째 포지션의 체크 누름");
                        count[position]++;
                        temp_sum[position] +=  ca.data_intprice.get(position);
                        price_sum +=  ca.data_intprice.get(position);
                        CartActivity.setTextPrice(price_sum);
                        txt_eachsize[position].setText(Integer.toString(count[position]));
                        Log.d(HWC, position + "번째 포지션의 price_sum : " + price_sum);
                        Log.d(HWC, position + "번째 포지션의 count : " + count[position]);
                        Log.d(HWC, position + "번째 포지션의 data_intprice : " + ca.data_intprice.get(position));
                        Log.d(HWC, position + "번째 포지션의 temp_sum : " + temp_sum[position] + "]");
                    } else {
                        flag[position] = false;
                        data_checked.set(position, false);
                        Log.d(HWC, "[" + position + "번째 포지션의 체크 해제");
                        count[position] = 0;
                        price_sum -= temp_sum[position];
                        temp_sum[position] = 0;
                        CartActivity.setTextPrice(price_sum);
                        txt_eachsize[position].setText(Integer.toString(count[position]));
                        Log.d(HWC, position + "번째 포지션의 price_sum : " + price_sum);
                        Log.d(HWC, position + "번째 포지션의 count : " + count[position]);
                        Log.d(HWC, position + "번째 포지션의 data_intprice : " + ca.data_intprice.get(position));
                        Log.d(HWC, position + "번째 포지션의 temp_sum : " + temp_sum[position] + "]");
                    }
                }
            });


            bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag[position] == true) {
                        Log.d(HWC, "[" + position + "번째 포지션의 + 버튼 누름");
                        count[position]++;
                        temp_sum[position] += ca.data_intprice.get(position);
                        price_sum +=  ca.data_intprice.get(position);
                        CartActivity.setTextPrice(price_sum);
                        txt_eachsize[position].setText(Integer.toString(count[position]));
                        Log.d(HWC, position + "번째 포지션의 price_sum : " + price_sum);
                        Log.d(HWC, position + "번째 포지션의 count : " + count[position]);
                        Log.d(HWC, position + "번째 포지션의 data_intprice : " + ca.data_intprice.get(position));
                        Log.d(HWC, position + "번째 포지션의 temp_sum : " + temp_sum[position] + "]");
                    }
                }
            });

            bt_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag[position] == true) {
                        if (temp_sum[position] > 0) {
                            Log.d(HWC, "[" + position + "번째 포지션의 - 체크 누름");
                            count[position]--;
                            temp_sum[position] -= ca.data_intprice.get(position);
                            price_sum -= ca.data_intprice.get(position);
                            CartActivity.setTextPrice(price_sum);
                            txt_eachsize[position].setText(Integer.toString(count[position]));
                            Log.d(HWC, position + "번째 포지션의 price_sum : " + price_sum);
                            Log.d(HWC, position + "번째 포지션의 count : " + count[position]);
                            Log.d(HWC, position + "번째 포지션의 data_intprice : " + ca.data_intprice.get(position));
                            Log.d(HWC, position + "번째 포지션의 temp_sum : " + temp_sum[position] + "]");
                        }
                    }
                }
            });
        }

        // 완성된 아이템 뷰를 반환합니다.
        return v;
    }

    // 데이터를 추가하는 것을 위해서 만들어 준다.
    public void add(ListView_getset user) {
        mData.add(user);
    }

    /*@Override
    public void onClick(View v) {
        // Tag를 이용하여 Data를 가져옵니다.
        //ListView_getset clickItem = (ListView_getset) v.getTag();
        switch (v.getId()) {
            case R.id.bt_add:
                //Log.d(HWC, "+");

                break;
            case R.id.bt_minus:
                //Log.d(HWC, "-");
                break;
        }
    }*/

    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            flag = true;
            //Log.d(HWC, "" + ca.data_intprice);
            for(int i = 0; i <ca.rowLength; i++) {

                    price_sum += ca.data_intprice.get(i);
                    CartActivity.setTextPrice(price_sum);

            }
            //Log.d(HWC, "" + price_sum);
        public void run() {
        } else {
            flag = false;
            Log.d(HWC, "false");
        }
    }*/

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