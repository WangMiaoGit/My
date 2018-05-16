package com.ricoh.wm.my.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ricoh.wm.my.R;

public class NewViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my2);

       /* this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        //返回按钮监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//menu item点击事件监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.test_menu1:

                        break;
                }
                return false;
            }
        });*/
    }

    public static void actionStart(Context context, String userName) {
        Intent intent = new Intent(context, NewViewActivity.class);
        intent.putExtra("userName", userName);
        context.startActivity(intent);

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);

        return super.onCreateOptionsMenu(menu);
    }*/

}
