package com.termproject.familyprotector;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class TestForDeviceAdmin extends AppCompatActivity implements View.OnClickListener{

    private PolicyManager policyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_for_device_admin);
        Log.v("main activity", "launched main activity");

        policyManager = new PolicyManager(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.activate_admin:
                if (!policyManager.isAdminActive()) {
                    Intent activateDeviceAdmin = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    activateDeviceAdmin.putExtra(
                            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            policyManager.getAdminComponent());
                    activateDeviceAdmin
                            .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    "After activating admin, you will be able to block application uninstallation.");
                    startActivityForResult(activateDeviceAdmin,
                            PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
                }
                break;
            case R.id.deactivate_admin:
                if (policyManager.isAdminActive())
                    policyManager.disableAdmin();
                break;
        }
    }
}
