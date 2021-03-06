package com.qw.sample;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

public class ApiGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_guide);
    }

    public void checkSinglePermission(View view) {
        //you can also use checkPermissions() for a series of permissions
        Permission checkResult = SoulPermission.getInstance().checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION);
        Toast.makeText(this, checkResult.toString(), Toast.LENGTH_LONG).show();
    }

    public void requestSinglePermission(View view) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                "\n is ok , you can do your operations", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                " \n is refused you can not do next things", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void requestPermissions(View view) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                //if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        Toast.makeText(ApiGuideActivity.this, allPermissions.length + "permissions is ok" +
                                " \n  you can do your operations", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        Toast.makeText(ApiGuideActivity.this, refusedPermissions[0].toString() +
                                " \n is refused you can not do next things", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void requestSinglePermissionWithRationale(View view) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS,
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                "\n is ok , you can do your operations", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        // see CheckPermissionWithRationaleAdapter
                        if (permission.shouldRationale()) {
                            Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                    " \n you should show a explain for user then retry ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                    " \n is refused you can not do next things", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void goApplicationSettings(View view) {
        SoulPermission.getInstance().goPermissionSettings();
    }

    public void checkNotification(View view) {
            boolean checkResult = SoulPermission.getInstance().checkSpecialPermission(Special.NOTIFICATION);
            if (checkResult) {
                Toast.makeText(view.getContext(), "Notification is enable", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Notification is disable \n you may invoke goPermissionSettings and enable notification")
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SoulPermission.getInstance().goPermissionSettings();
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
    }

    public void checkSystemAlert(View view) {
        boolean checkResult = SoulPermission.getInstance().checkSpecialPermission(Special.SYSTEM_ALERT);
        if (checkResult) {
            Toast.makeText(view.getContext(), "SystemAlert is enable", Toast.LENGTH_LONG).show();
        } else {
            new AlertDialog.Builder(view.getContext())
                    .setMessage("SystemAlert is disable ")
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                startActivity(intent);
                            }
//                            SoulPermission.getInstance().goPermissionSettings();
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
    }

    public void getTopActivity(View view) {
        Activity activity = SoulPermission.getInstance().getTopActivity();
        if (null != activity) {
            Toast.makeText(activity, activity.getClass().getSimpleName() + " " + activity.hashCode(), Toast.LENGTH_LONG).show();
        }
    }

}
