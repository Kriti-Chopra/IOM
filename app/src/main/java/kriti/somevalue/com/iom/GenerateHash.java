package kriti.somevalue.com.iom;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateHash extends Fragment {

    private ImageView imgView;
    private Bitmap bitmap;
    private Button btnGenerateQR;
    private EditText edtVendorId;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    String EditTextValue;

    String REF_PREF="prefs";


    public GenerateHash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_generate_hash, container, false);
        imgView=view.findViewById(R.id.imgQR);
        btnGenerateQR=view.findViewById(R.id.btnGenerateQR);
        edtVendorId=view.findViewById(R.id.edtVendorId);

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTextValue = edtVendorId.getText().toString();

                SharedPreferences.Editor editor=getContext().getSharedPreferences(REF_PREF, Context.MODE_PRIVATE).edit();
                editor.putString("VendorId",EditTextValue);
                editor.apply();

                Long tslong=System.currentTimeMillis()/1000;
                String ts=tslong.toString();

                try {
                    bitmap = TextToImageEncode(EditTextValue.concat(ts));

                    imgView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException{
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorPrimaryDark):getResources().getColor(R.color.colorAccent);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;

    }

}
