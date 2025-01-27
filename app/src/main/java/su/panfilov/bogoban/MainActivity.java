package su.panfilov.bogoban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import su.panfilov.bogoban.components.BottomNavigationViewHelper;
import su.panfilov.bogoban.models.SetItem;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    private Fragment frag;

    private int insetTop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Bogoban_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.removeShiftMode(mBottomNav);//disable BottomNavigationView shift mode

        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        if (frag != null) {
            FragmentTransaction ftremove = getSupportFragmentManager().beginTransaction();
            ftremove.remove(frag);
            ftremove.commit();
        }

        frag = null;
        int id = item.getItemId();

        if (id == R.id.mainTab) {
            frag = BogobanFragment.newInstance();
        } else if (id == R.id.setsTab) {
            frag = SetsFragment.newInstance();
        } else if (id == R.id.helpTab) {
            frag = HelpFragment.newInstance();
        }

        // Опционально: обработка неучтенных случаев
        if (frag == null) {
            throw new IllegalStateException("Unknown menu item ID: " + id);
        }
        // update selected item
        mSelectedItem = item.getItemId();

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.rootLayout, frag, frag.getTag());
            ft.commit();
        }
    }

    public void showBogoban(SetItem setItem) {
        mBottomNav.getMenu().getItem(0).setChecked(true);
        selectFragment(mBottomNav.getMenu().getItem(0));
        ((BogobanFragment) frag).setStonesSet(setItem);
    }

    public int getStatusBarHeight() {
        if (statusBarHeight() > 0) {
            return statusBarHeight();
        }

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int statusBarHeight()
    {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentViewTop - statusBarHeight;
    }
}