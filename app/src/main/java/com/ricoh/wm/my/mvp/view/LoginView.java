package com.ricoh.wm.my.mvp.view;

import android.content.Context;

import com.ricoh.wm.my.model.User;

/**
 * @author wangmiao
 * Created by 2017063001 on 2018/3/10.
 *
 *
 * mvp  模式中 view层的接口
 */
public interface LoginView {
    /**得到用户填写的用户名信息
     *
     * @return
     */
    String getUsername();
    /**得到用户填写的密码信息
     *
     * @return
     */
    String getPassword();

    /**得到上下文
     *
     * @return
     */
    Context context();

    /**显示和隐藏登录ProgressBar
     *
     */
    void showLoading();
    /**显示和隐藏登录ProgressBar
     *
     */
    void hideLoading();

    /**登录成功或失败后，返回信息的方法
     *
     * @param user
     */
    void showSuccessMsg(User user);
    /**登录成功或失败后，返回信息的方法
     *
     * @param s
     */
    void showFailedMsg(String s);

    /**清楚数据
     *
     */
    void clearEditText();
}
