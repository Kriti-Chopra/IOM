package kriti.somevalue.com.iom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ShopkeeperPagerAdapter extends FragmentPagerAdapter {

    public ShopkeeperPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0: return new ScanQRCode();
            case 1: return new ShopkeeperHistory();
            case 2: return new ShopkeeperProfile();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "Scan QR Code";
            case 1: return "My History";
            case 2: return "My Profile";
            default:return null;
        }
    }
}
