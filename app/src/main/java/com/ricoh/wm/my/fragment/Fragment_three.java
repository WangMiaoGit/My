package com.ricoh.wm.my.fragment;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.utils.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_three extends Fragment {


    @Bind(R.id.bt_shock)
    Button btShock;
    @Bind(R.id.bt_db)
    Button btDb;
    @Bind(R.id.add_db)
    Button add_db;
    @Bind(R.id.contacts_view)
    ListView contactsView;

    //震动器对象
    private Vibrator vibrator;
    //是否在震动
    boolean shock = true;

    //数据库管理类
    private MyDbHelper myDbHelper;


    //存放联系人信息
    private List<String> list_contacts = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public Fragment_three() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);

        //得到系统的震动器对象
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        //得到dbhelper类
        myDbHelper = new MyDbHelper(getContext(), "BookerStore.db", null, 1);


        //给listView存放数据
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_contacts);
        contactsView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);

        } else {
            readContacts();
        }
        return view;
    }

    /**
     * 读取手机联系人
     */
    private void readContacts() {
        Cursor cursor = null;

        try {
            /**
             * 访问内容提供器中的共享数据，需要ContentResolver类，通过Context的getContentResolver()获得
             */
            //查询联系人数据
            cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            if (cursor != null) {

                list_contacts.clear();
                while (cursor.moveToNext()) {
                    //获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //listView的数据源
                    list_contacts.add(displayName + "\n" + number);
                }
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 检查权限的
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(getContext(), "你拒绝了联系人权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bt_shock, R.id.bt_db,R.id.add_db})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_shock:
//                vibrator.vibrate(2000);
                if (shock) {
                    vibrator.vibrate(new long[]{0, 300, 50, 100, 50, 100}, 0);

                    shock = false;
                } else {
                    vibrator.cancel();
                    shock = true;
                }
                break;
            case R.id.bt_db:
                createDb();
                break;
            case R.id.add_db:
                add_data();
//                update_data();
                break;
        }

    }


    /**
     * 使用sql语句的方式
     *  添加：db.execSQL("insert into Book (name,author,pages,price) values(?,?,?,?)",
     *                      new String[]{"a","wm","444","12.28"});
     *  更新：db.execSQL("update Book set price = ? where name = ?",
     *                      new String[]{"10.99","wm"});
     *  删除：db.execSQL("delete from Book where pages > ?",
     *                      new String[]{"500"});
     *  查询:db.rawQuery("select * from Book",null);
     */
    //增加数据
    private void add_data() {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        //不用手工编写，直接调用方法传参
        ContentValues contentValues = new ContentValues();
        //设置要插入的字段值
/*        contentValues.put("name","The Da Vinci Code");
        contentValues.put("author","Dan Brown");
        contentValues.put("pages",454);
        contentValues.put("price",16.96);
        //插入数据
        db.insert("Book",null,contentValues);

        contentValues.clear();*/


        //设置要插入的字段值
/*        contentValues.put("name","AAAAAAAAAA");
        contentValues.put("author","Dan Brown");
        contentValues.put("pages",520);
        contentValues.put("price",22.22);
        //插入数据
        db.insert("Book",null,contentValues);*/



        contentValues.put("category_name","mybook");
        contentValues.put("category_code",1);
        db.insert("Category",null,contentValues);

        Toast.makeText(getContext(), "插入数据成功", Toast.LENGTH_SHORT).show();




    }
    //修改数据
    private void update_data(){
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        //不用手工编写，直接调用方法传参
        ContentValues contentValues = new ContentValues();
        //设置要修改的字段值
        contentValues.put("price",88.88);
        //插入数据
        db.update("Book",contentValues,"name=?",new String[]{"The Da Vinci Code"});

        Toast.makeText(getContext(), "修改数据成功", Toast.LENGTH_SHORT).show();
    }
    //删除数据
    private void delete_data(){
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        db.delete("Book","pages > ?",new  String[]{"500"});

        Toast.makeText(getContext(), "删除数据成功", Toast.LENGTH_SHORT).show();
    }


    //查询数据
    private void query_data(){
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        //查询所有
        /**
         * table            from table_name            表名
         * columns          select column1,column2     查询的列名
         * selection        where column = value       指定where的约束条件
         * selectionArgs    -                          为where中的占位符提供具体的值
         * groupBy          group by column            group by的列
         * having           having column = value      对group by后的结果进一步约束
         * orderBy          order by column1，column2  指定查询结果的排序方式
         *
         */
        Cursor cursor= db.query("Book",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
            }while (cursor.moveToNext());
        }
        Toast.makeText(getContext(), "删除数据成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建数据库
     */
    private void createDb() {
        SQLiteDatabase writableDatabase = myDbHelper.getWritableDatabase();
    }

}
