package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.smartpoint.adapter.MyAdapter;
import com.smartpoint.entity.CardBean;
import com.smartpoint.entity.CityInfo;
import com.smartpoint.marquee.R;

import java.util.ArrayList;
import java.util.List;

public class PickerViewActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "PickerViewActivity";
    private OptionsPickerView pvCustomOptions;
    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private Button btnProvince,btnCity;
    private LinearLayout rootView;
    private int type = -1;//0代表选择省1代表选择市
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PickerViewActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickerview);
        initView();
        getCardData();
        initBottomView();
    }
    private void initView(){
        rootView = findViewById(R.id.rootView);
        findViewById(R.id.btn).setOnClickListener(this);
        btnProvince = findViewById(R.id.btnProvince);
        btnCity = findViewById(R.id.btnCity);
        btnProvince.setOnClickListener(this);
        btnCity.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                initCustomOptionPicker();
                break;
            case R.id.btnProvince:
                type = 0;
                adapter.getList().clear();
                adapter.addData(getProvince());
                adapter.notifyDataSetChanged();
                bottomSheetDialog.show();
                //popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                break;
            case R.id.btnCity:
                type = 1;
                adapter.getList().clear();
                adapter.addData(getCity());
                adapter.notifyDataSetChanged();
                bottomSheetDialog.show();
                //popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                break;

        }
    }
    ListView listView;
    MyAdapter adapter;
    TextView textViewTitle;
    BottomSheetDialog bottomSheetDialog;//底部弹出的dialog
    //初始化bottomView
    private void initBottomView(){
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.bottom_sheet_dialog, null);
        bottomSheetDialog.setContentView(view);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        listView = view.findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type==0){
                    String resProvince = adapter.getItem(position);
                    Toast.makeText(PickerViewActivity.this,"选择省份为"+resProvince,Toast.LENGTH_SHORT).show();
                    textViewTitle.setText("选择市");
                    adapter.getList().clear();
                    adapter.addData(getCity());
                    adapter.notifyDataSetChanged();
                }else {
                    String resCity = adapter.getItem(position);
                    Toast.makeText(PickerViewActivity.this,"选择城市为"+resCity,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private List<String> getCity(){
        List<String> list1 = new ArrayList<>();
        list1.add("成都");list1.add("西安");list1.add("广州");
        list1.add("测试1");list1.add("测试2");list1.add("测试3");
        return list1;
    }
    private List<String> getProvince(){
        List<String> list1 = new ArrayList<>();
        list1.add("四川");list1.add("湖南");list1.add("江苏");
        list1.add("测试1");list1.add("测试2");list1.add("测试3");
        return list1;
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                Log.e(TAG,"选中--->"+list1.get(options1)+"  "+list2.get(options1).get(option2));
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .isDialog(true)
                .build();

        pvCustomOptions.setPicker(list1,list2);//添加数据
        pvCustomOptions.show();
    }
    List<String> list1 = new ArrayList<>();
    List<List<String>> list2 = new ArrayList<>();
    private void getCardData() {
        CityInfo cityInfo = new Gson().fromJson(test,CityInfo.class);
        for (int i = 0;i<cityInfo.getProvinces().size();i++){
            String province = cityInfo.getProvinces().get(i).getProvinceName();
            list1.add(province);
            List<CityInfo.ProvincesBean.CitysBean> list_city = cityInfo.getProvinces().get(i).getCitys();
            List<String> list_city2 = new ArrayList<>();
            for (CityInfo.ProvincesBean.CitysBean citysBean:list_city){
                list_city2.add(citysBean.getCitysName());
            }
            list2.add(list_city2);
        }
//        for (int i = 0; i < 5; i++) {
//            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
//        }
//
//        for (int i = 0; i < cardItem.size(); i++) {
//            if (cardItem.get(i).getCardNo().length() > 6) {
//                String str_item = cardItem.get(i).getCardNo().substring(0, 6) + "...";
//                cardItem.get(i).setCardNo(str_item);
//            }
//        }
    }

    private String test = "{\n" +
            "    \"provinces\": [\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"石家庄市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邯郸市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"唐山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"保定市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"秦皇岛市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邢台市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"张家口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"承德市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"沧州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"廊坊市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"衡水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"辛集市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"晋州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新乐市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"遵化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"迁安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"霸州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"三河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"定州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"涿州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安国市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高碑店市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泊头市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"任丘市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"黄骅市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"河间市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"冀州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"深州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南宫市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"沙河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"武安市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"河北省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"太原市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大同市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"朔州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阳泉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"长治市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"晋城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"忻州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吕梁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"晋中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临汾市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"运城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"古交市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"潞城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"原平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"孝义市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汾阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"介休市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"侯马市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"霍州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"永济市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"河津市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"山西省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"呼和浩特市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"包头市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乌海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"赤峰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"呼伦贝尔市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"通辽市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乌兰察布市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鄂尔多斯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"巴彦淖尔市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"满洲里市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"扎兰屯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"牙克石市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"根河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"额尔古纳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乌兰浩特市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿尔山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"霍林郭勒市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"锡林浩特市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"二连浩特市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丰镇市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"内蒙古自治区\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"沈阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大连市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"朝阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阜新市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铁岭市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"抚顺市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"本溪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"辽阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鞍山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丹东市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"营口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"盘锦市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"锦州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"葫芦岛市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新民市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"瓦房店市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"庄河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北票市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"凌源市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"调兵山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"开原市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"灯塔市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"凤城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东港市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大石桥市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"盖州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"凌海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北镇市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兴城市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"辽宁省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"长春市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吉林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"白城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"松原市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"四平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"辽源市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"通化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"白山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"德惠市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"榆树市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"磐石市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"蛟河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桦甸市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"舒兰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"洮南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"双辽市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"公主岭市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"梅河口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"集安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"延吉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"图们市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"敦化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"珲春市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"龙井市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"和龙市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"扶余市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"吉林省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"哈尔滨市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"齐齐哈尔市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"黑河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大庆市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"伊春市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鹤岗市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"佳木斯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"双鸭山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"七台河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鸡西市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"牡丹江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"绥化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"尚志市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"五常市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"讷河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"五大连池市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铁力市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"同江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"富锦市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"虎林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"密山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"绥芬河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宁安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安达市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"肇东市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海伦市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"穆棱市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"抚远市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"黑龙江省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"南京市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"徐州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"连云港市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宿迁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"淮安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"盐城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"扬州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泰州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南通市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"镇江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"常州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"无锡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"苏州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"常熟市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"张家港市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"太仓市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昆山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"江阴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"溧阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"扬中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"句容市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丹阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"如皋市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"启东市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高邮市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"仪征市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兴化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泰兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"靖江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东台市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邳州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新沂市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"江苏省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"杭州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宁波市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"湖州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"嘉兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"舟山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"绍兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"衢州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"金华市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"台州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"温州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丽水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"建德市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"慈溪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"余姚市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"平湖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桐乡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"诸暨市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"嵊州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"江山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兰溪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"永康市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"义乌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"温岭市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"瑞安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乐清市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"龙泉市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"浙江省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"合肥市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"芜湖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"蚌埠市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"淮南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"马鞍山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"淮北市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铜陵市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安庆市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"黄山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"滁州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阜阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宿州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"六安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"亳州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"池州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宣城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"巢湖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桐城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"天长市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"明光市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"界首市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宁国市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"安徽省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"厦门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"福州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"三明市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"莆田市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泉州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"漳州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"龙岩市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宁德市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"福清市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"长乐市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邵武市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"武夷山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"建瓯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"永安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"石狮市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"晋江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"龙海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"漳平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"福安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"福鼎市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"福建省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"南昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"九江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"景德镇市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鹰潭市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新余市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"萍乡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"赣州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"上饶市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"抚州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜春市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吉安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"瑞昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"共青城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乐平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"瑞金市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"德兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丰城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"樟树市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"井冈山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"贵溪市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"江西省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"济南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"青岛市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"聊城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"德州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东营市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"淄博市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"潍坊市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"烟台市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"威海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"日照市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临沂市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"枣庄市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"济宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泰安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"莱芜市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"滨州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"菏泽市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"胶州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"即墨市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"平度市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"莱西市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临清市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乐陵市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"禹城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安丘市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昌邑市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高密市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"青州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"诸城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"寿光市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"栖霞市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"龙口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"莱阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"莱州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"蓬莱市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"招远市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"荣成市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乳山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"滕州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"曲阜市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邹城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新泰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"肥城市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"山东省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"郑州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"开封市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"洛阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"平顶山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鹤壁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新乡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"焦作市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"濮阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"许昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"漯河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"三门峡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"商丘市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"周口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"驻马店市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"信阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"荥阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新郑市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"登封市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新密市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"偃师市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"孟州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"沁阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"卫辉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"辉县市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"林州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"禹州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"长葛市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"舞钢市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"义马市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"灵宝市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"项城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"巩义市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邓州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"永城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汝州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"济源市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"河南省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"武汉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"十堰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"襄阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"荆门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"孝感市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"黄冈市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鄂州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"黄石市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"咸宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"荆州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"随州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丹江口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"老河口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"枣阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"钟祥市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汉川市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"应城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安陆市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"广水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"麻城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"武穴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大冶市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"赤壁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"石首市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"洪湖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"松滋市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜都市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"枝江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"当阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"恩施市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"利川市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"仙桃市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"天门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"潜江市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"湖北省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"长沙市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"衡阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"张家界市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"常德市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"益阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"岳阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"株洲市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"湘潭市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"郴州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"永州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邵阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"怀化市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"娄底市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"耒阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"常宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"浏阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"津市市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"沅江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汨罗市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临湘市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"醴陵市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"湘乡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"韶山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"资兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"武冈市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"洪江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"冷水江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"涟源市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吉首市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"湖南省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"广州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"深圳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"清远市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"韶关市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"河源市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"梅州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"潮州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汕头市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"揭阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汕尾市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"惠州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东莞市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"珠海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"中山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"江门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"佛山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"肇庆市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"云浮市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阳江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"茂名市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"湛江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"英德市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"连州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乐昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南雄市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兴宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"普宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"陆丰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"恩平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"台山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"开平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"鹤山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"四会市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"罗定市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阳春市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"化州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"信宜市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吴川市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"廉江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"雷州市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"广东省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"南宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桂林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"柳州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"梧州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"贵港市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"玉林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"钦州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"防城港市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"崇左市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"百色市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"河池市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"来宾市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"贺州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"岑溪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桂平市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北流市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东兴市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"凭祥市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"合山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"靖西市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"广西壮族自治区\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"海口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"三亚市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"三沙市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"儋州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"文昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"琼海市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"万宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"东方市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"五指山市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"海南省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"成都市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"广元市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"绵阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"德阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"南充市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"广安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"遂宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"内江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乐山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"自贡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"泸州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宜宾市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"攀枝花市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"巴中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"达州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"资阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"眉山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"雅安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"崇州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"邛崃市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"都江堰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"彭州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"江油市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"什邡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"广汉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"绵竹市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阆中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"华蓥市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"峨眉山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"万源市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"简阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"西昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"康定市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"马尔康市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"四川省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"贵阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"六盘水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"遵义市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安顺市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"毕节市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铜仁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"清镇市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"赤水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"仁怀市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"凯里市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"都匀市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兴义市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"福泉市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"贵州省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"昆明市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"曲靖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"玉溪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"丽江市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昭通市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"普洱市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临沧市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"保山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宣威市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"芒市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"瑞丽市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"大理市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"楚雄市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"个旧市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"开远市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"蒙自市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"弥勒市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"景洪市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"文山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"香格里拉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"腾冲市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"云南省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"西安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"延安市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铜川市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"渭南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"咸阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"宝鸡市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"汉中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"榆林市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"商洛市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"安康市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"韩城\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"华阴\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"兴平\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"陕西省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"兰州市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"嘉峪关市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"金昌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"白银市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"天水市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"酒泉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"张掖市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"武威市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"庆阳市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"平凉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"定西市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"陇南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"玉门市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"敦煌市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"临夏市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"合作市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"甘肃省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"西宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"海东市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"格尔木市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"德令哈市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"玉树市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"青海省\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"拉萨市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"日喀则市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昌都市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"林芝市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"山南市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"西藏自治区\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"银川市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"石嘴山市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吴忠市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"中卫市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"固原市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"灵武市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"青铜峡市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"宁夏回族自治区\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"台北市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新北市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"桃园市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"台中市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"台南市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"高雄市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"基隆市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"新竹市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"嘉义市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"台湾\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"北京市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"北京市\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"天津市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"天津市\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"上海市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"上海市\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"重庆市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"重庆市\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"香港特别行政区\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"香港\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"澳门特别行政区\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"澳门\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"citys\": [\n" +
            "                {\n" +
            "                    \"citysName\": \"乌鲁木齐市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"克拉玛依市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"吐鲁番市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"哈密市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"喀什市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿克苏市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"和田市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿图什市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿拉山口市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"博乐市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昌吉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阜康市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"库尔勒市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"伊宁市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"奎屯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"塔城市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"乌苏市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿勒泰市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"霍尔果斯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"石河子市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"阿拉尔市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"图木舒克市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"五家渠市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"北屯市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"铁门关市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"双河市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"可克达拉市\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"citysName\": \"昆玉市\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"provinceName\": \"新疆维吾尔自治区\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";


}
