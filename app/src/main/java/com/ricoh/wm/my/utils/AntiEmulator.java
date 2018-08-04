//package com.ricoh.wm.my.utils;
//
//import android.content.Context;
//import android.os.Build;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//import java.io.File;
//
///**
// * 检测是否是模拟器
// * Created by 2017063001 on 2018/6/8.
// */
//public class AntiEmulator {
//
//    private static String[] known_pipes={
//            "/dev/socket/qemud",
//            "/dev/qemu_pipe"
//    };
//
//    private static String[] known_qemu_drivers={
//            "goldfish"
//    };
//
//    private static String [] known_files={
//            "/system/lib/libc_malloc_debug_qemu.so",
//            "/sys/qemu_trace",
//            "/system/bin/qemu-props"
//    };
//
//    private static String [] known_device_ids={
//      "000000000000000"     //默认的ID
//    };
//    private static String [] known_imsi_ids={
//      "310260000000000"     //默认的imsi ID
//    };
//
//    //模拟器上面特有的几个文件
//    public static Boolean CheckEmulatorFiles(){
//        for (int i = 0;i<known_files.length;i++){
//            String file_name = known_files[i];
//            File qemu_file = new File(file_name);
//            if (qemu_file.exists()){
//                Log.v("Result:","Find Emulator Files!");
//                return  true;
//            }
//        }
//        Log.v("Result:","Not Find Emulator Files!");
//        return  false;
//    }
//    //检测设备的IDS 是不是"00000000000000"
//    public static Boolean CheckDeviceIDS(Context context){
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        String device_ids = telephonyManager.getDeviceId();
//
//        for (String know_deviceid :known_device_ids){
//            if (know_deviceid.equalsIgnoreCase(device_ids)){
//                Log.v("Result:","Find ids:000000000000000!");
//                return true;
//            }
//        }
//        Log.v("Result:","Not Find ids:000000000000000!");
//        return false;
//    }
//
//    //检测设备的IDS 是不是"310260000000000"
//    public static Boolean CheckImsiIDS(Context context){
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        String imsi_ids = telephonyManager.getSubscriberId();
//
//        for (String know_imsi :known_imsi_ids){
//            if (know_imsi.equalsIgnoreCase(imsi_ids)){
//                Log.v("Result:","Find imsi ids:310260000000000!");
//                return true;
//            }
//        }
//        Log.v("Result:","Not imsi Find ids:310260000000000!");
//        return false;
//    }
//
//    //检测手机硬件信息
//    public static Boolean CheckEmulatorBuild(Context context){
//       String BOARD = Build.BOARD;
//       String BOOTLOADER = Build.BOOTLOADER;
//       String BRAND = Build.BRAND;
//       String DEVICE = Build.DEVICE;
//       String HARDWARE = Build.HARDWARE;
//       String MODEL = Build.MODEL;
//       String PRODUCT = Build.PRODUCT;
//
//
//        if (BOARD == "unknown" || BOOTLOADER == "unknown"
//                ||BRAND == "generic"){
//            Log.v("Result:","Find imsi ids:310260000000000!");
//            return true;
//        }
//
//        Log.v("Result:","Not imsi Find ids:310260000000000!");
//        return false;
//    }
//}
