package com.ricoh.wm.my.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ricoh.wm.my.greendao.DaoMaster;
import com.ricoh.wm.my.greendao.DaoSession;
import com.ricoh.wm.my.greendao.User_testDao;
import com.ricoh.wm.my.model.User_test;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @ author ORM对象关系映射  将java对象映射到SQLite数据库中
 * Created by 2017063001 on 2018/5/21.
 *
 *   GreenDao  SQLite数据库管理类
 */
public class DBManager {
    private final static String dbName = "USER";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param user 需要插入的对象
     */
    public void insertUser(User_test user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        userDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertUserList(List<User_test> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        userDao.insertInTx(users);
    }


    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteUser(User_test user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        userDao.delete(user);
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateUser(User_test user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        userDao.update(user);
    }

    /**
     * 查询用户列表
     */
    public List<User_test> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        QueryBuilder<User_test> qb = userDao.queryBuilder();
        List<User_test> list = qb.list();

//        return userDao.loadByRowId(1);//根据ID查询
//        return userDao.queryRaw("where AGE>?","10");//查询年龄大于10的用户

        //查询年龄大于10的用户
//        QueryBuilder<User> builder = userDao.queryBuilder();
//        return  builder.where(UserDao.Properties.Age.gt(10)).build().list();

//        return userDao.loadAll();// 查询所有记录

        return list;
    }

    /**
     * 查询用户列表
     */
    public List<User_test> queryUserList(int age) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User_testDao userDao = daoSession.getUser_testDao();
        QueryBuilder<User_test> qb = userDao.queryBuilder();
        qb.where(User_testDao.Properties.Age.gt(age)).orderAsc(User_testDao.Properties.Age);
        List<User_test> list = qb.list();
        return list;
    }
}
