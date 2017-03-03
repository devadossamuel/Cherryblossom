package com.commax.onvif_device_manager.device;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.commax.onvif_device_manager.R;

/**
 * 팝업
 * Created by bagjeong-gyu on 2016. 9. 28..
 */

public class DeviceInfoPopup extends Dialog {


    private final Context mContext;
    private final DeviceInfoConfirmListener mListener;
    private int mPosition;
    private OnvifDevice mSelectedOnvifDevice;


    public DeviceInfoPopup(Context context, DeviceInfoConfirmListener listener) {
        super(context, R.style.CustomDialog);

        mContext = context;
        mListener = listener;
        setOwnerActivity((Activity) context);
        setCancelable(true);
        setContentView(R.layout.popup_device_info);

        addButtonListener();
    }


    /**
     * 버튼에 리스너 추가
     */
    private void addButtonListener() {
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeviceInfoConfirmed(mPosition ,getDeviceInfo());

                dismiss();
            }
        });
    }

    /**
     * 사용자가 입력한 아이디, 비밀번호, 디바이스 별명 가져옴
     * @return
     */
    private OnvifDevice getDeviceInfo() {
        EditText idInput = (EditText) findViewById(R.id.idInput);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        EditText aliasInput = (EditText) findViewById(R.id.aliasInput);
        String id = idInput.getText().toString();
        String password = passwordInput.getText().toString();
        String alias = aliasInput.getText().toString();

        mSelectedOnvifDevice.setId(id);
        mSelectedOnvifDevice.setPassword(password);
        mSelectedOnvifDevice.setAlias(alias);
        mSelectedOnvifDevice.setConfirmed(DeviceInfoConstants.CONFIRMED);


        return mSelectedOnvifDevice;

    }


    /**
     * 팝업창 취소 여부 설정
     *
     * @param flag 팝업 취소 여부
     * @return 팝업창 객체
     */
    public DeviceInfoPopup cancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }


    public void setPosition(int position) {
        mPosition = position;
    }

    public void setSelectedIpDevice(OnvifDevice selectedOnvifDevice) {
        mSelectedOnvifDevice = selectedOnvifDevice;
    }
}
