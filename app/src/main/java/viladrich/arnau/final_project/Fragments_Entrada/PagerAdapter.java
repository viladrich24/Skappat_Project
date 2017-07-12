package viladrich.arnau.final_project.Fragments_Entrada;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Log In", "Sign Up" };
    private Fragment tab = null;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return tabTitles.length;
    }

    public Fragment getItem(int position) {

        switch(position) {
            case 0: tab = new LogInFragment(); break;
            case 1: tab = new SignUpFragment(); break;
        }
        return tab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }




}
