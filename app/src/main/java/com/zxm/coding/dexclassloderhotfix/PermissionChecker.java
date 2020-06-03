package com.zxm.coding.dexclassloderhotfix;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangXinmin on 2018/12/17.
 * Copyright (c) 2018 . All rights reserved.
 */
public final class PermissionChecker {
    private PermissionChecker() {
        throw new UnsupportedOperationException("U con't do this!");
    }

    /**
     * Request permissions.
     * 申请一组权限。
     *
     * @param activity    The activity.
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissions(@NonNull Activity activity, @NonNull String[] permissions, int requestCode) {
        if (permissions != null) {
            //先获取未被允许的权限
            String[] deniedPermissions = checkDeniedPermissions(activity, permissions);
            if (deniedPermissions != null && deniedPermissions.length > 0) {
                ActivityCompat.requestPermissions(activity, deniedPermissions, requestCode);
            }
        }
    }

    /**
     * check permissions
     * 检查一组权限是否存在未被允许的权限
     *
     * @param context
     * @param permissions
     * @return if the permission has not been granted to the given package , return false.
     */
    public static boolean checkSeriesPermissions(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!checkPersmission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get denied permissions
     * 获取拒绝的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static String[] checkDeniedPermissions(@NonNull Context context,
                                                  @NonNull String[] permissions) {
        final List<String> deniedList = new ArrayList<>();
        if (permissions != null) {
            for (String permission : permissions) {
                if (!checkPersmission(context, permission)) {
                    deniedList.add(permission);
                }
            }
        }

        if (deniedList.isEmpty()) {
            return null;
        } else {
            return deniedList.toArray(new String[0]);
        }
    }

    /**
     * Check single permission.
     * 检查单一权限.
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPersmission(@NonNull Context context, @NonNull String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


}
