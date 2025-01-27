package su.panfilov.bogoban;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import su.panfilov.bogoban.components.Utils;
import su.panfilov.bogoban.models.SetItem;
import su.panfilov.bogoban.models.SetItemInfo;
import su.panfilov.bogoban.models.Stone;
import su.panfilov.bogoban.models.StoneColor;
import su.panfilov.bogoban.models.StonePosition;
import su.panfilov.bogoban.models.StoneSize;


public class BogobanFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "BogobanFragment";

    private View rootView;
    private ViewGroup bogobanWrap;
    private ImageView backgroundImageView;
    private List<List<ImageView>> stonesGrid;
    private List<ImageView> stonesBigBasket;
    private List<ImageView> stonesMiddleBasket;
    private List<ImageView> stonesSmallBasket;
    private ImageView draggableStone;
    private StoneColor[] colors = {
            StoneColor.white,
            StoneColor.black,
            StoneColor.red,
            StoneColor.orange,
            StoneColor.yellow,
            StoneColor.green,
            StoneColor.cyan,
            StoneColor.blue,
            StoneColor.purple};
    private ImageButton saveImageButton;
    private ImageButton clearImageButton;
    private ImageButton colorImageButton;

    private Stone stoneDragging;

    private static SetItem stonesSet;

    int statusBarHeight = 0;
    int sepWidth = 0;
    int sepHeight = 0;

    private BogobanFragment() {
    }

    public static BogobanFragment newInstance() {
        BogobanFragment fragment = new BogobanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bogoban, container, false);

        initView();

        return rootView;
    }

    public void setStonesSet(SetItem stonesSet) {
        this.stonesSet = stonesSet;
    }

    private void initView() {
        if (stonesSet == null) {
            stonesSet = new SetItem(new SetItemInfo(getActivity().getString(R.string.save_my_set), "1"), new ArrayList<>(0));
        }

        statusBarHeight = ((MainActivity) getActivity()).getStatusBarHeight();

        View etalonWidthSeparator = rootView.findViewById(R.id.etalonWidthSeparatorView);
        sepWidth = etalonWidthSeparator.getWidth();

        View etalonHeightSeparator = rootView.findViewById(R.id.etalonHeightSeparatorView);
        sepHeight = etalonHeightSeparator.getHeight();

        saveImageButton = rootView.findViewById(R.id.saveImageButton);
        saveImageButton.setOnClickListener(v -> saveSet());

        clearImageButton = rootView.findViewById(R.id.clearImageButton);
        clearImageButton.setOnClickListener(v -> clearStonesGrid());

        colorImageButton = rootView.findViewById(R.id.colorImageButton);
        colorImageButton.setOnClickListener(v -> changeColor());

        backgroundImageView = rootView.findViewById(R.id.coordinatesTableImageView);
        bogobanWrap = rootView.findViewById(R.id.bogobanWrap);
        draggableStone = rootView.findViewById(R.id.draggableBallImageView);
        draggableStone.setVisibility(View.INVISIBLE);

        stonesGrid = new ArrayList<>(0);
        for (int row = 0; row < 9; row++) {
            List<ImageView> rowList = new ArrayList<>(0);
            for (int col = 0; col < 9; col++) {
                ImageView stone = rootView.findViewById(Utils.getResId("cell" + row + col + "ImageView", R.id.class));
                stone.setVisibility(View.INVISIBLE);
                rowList.add(stone);
            }
            stonesGrid.add(rowList);
        }

        stonesBigBasket = new ArrayList<>(0);
        stonesMiddleBasket = new ArrayList<>(0);
        stonesSmallBasket = new ArrayList<>(0);
        for (int num = 1; num <= 9; num++) {
            ImageView stoneBig = rootView.findViewById(Utils.getResId("ballBig" + num + "ImageView", R.id.class));
            stonesBigBasket.add(stoneBig);

            ImageView stoneMiddle = rootView.findViewById(Utils.getResId("ballMiddle" + num + "ImageView", R.id.class));
            stonesMiddleBasket.add(stoneMiddle);

            ImageView stoneSmall = rootView.findViewById(Utils.getResId("ballSmall" + num + "ImageView", R.id.class));
            stonesSmallBasket.add(stoneSmall);
        }

        bogobanWrap.setOnTouchListener(this);

        fillStonesGrid();
    }

    private void saveSet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle(R.string.save_popup_title);

        final EditText input = new EditText(rootView.getContext());
        input.setText(stonesSet.info.title);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            stonesSet.info.title = input.getText().toString();
            stonesSet.saveState(rootView.getContext().getApplicationContext());
            stonesSet = new SetItem(new SetItemInfo(stonesSet.info.title, stonesSet.info.background), stonesSet.stoneList);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void clearStonesGrid() {
        stonesSet.stoneList = new ArrayList<>(0);
        fillStonesGrid();
    }

    private void changeColor() {
        int color = Integer.parseInt(stonesSet.info.background);
        color++;
        if (color > 5) {
            color = 1;
        }
        stonesSet.info.background = Integer.toString(color);
        backgroundImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), Utils.getResId("background" + stonesSet.info.background, R.drawable.class)));
    }

    private void fillStonesGrid() {
        Log.d("DD", "background" + stonesSet.info.background);

        backgroundImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), Utils.getResId("background" + stonesSet.info.background, R.drawable.class)));

        List<StonePosition> usedPositions = new ArrayList<>(0);
        Log.d("DD", stonesSet.stoneList.size() + "");
        for (Stone stone : stonesSet.stoneList) {
            Log.d("DD", stone.getColor() + " " + stone.getSize() + " " + stone.getPosition().getLeft());

            ImageView stoneCell = stonesGrid.get(stone.getPosition().getLeft()).get(stone.getPosition().getTop());
            String sizePart = "";
            switch (stone.getSize()) {
                case small:
                    sizePart = "_small";
                    break;
                case middle:
                    sizePart = "_middle";
                    break;
            }
            stoneCell.setVisibility(View.VISIBLE);
            stoneCell.setImageDrawable(ContextCompat.getDrawable(getContext(), Utils.getResId("ball_" + stone.getColor() + sizePart, R.drawable.class)));
            usedPositions.add(stone.getPosition());
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (usedPositions.contains(new StonePosition(row, col))) {
                    continue;
                }

                ImageView stoneCell = stonesGrid.get(row).get(col);
                stoneCell.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void removeFromGrid(Stone stone) {
        Log.d("SSS", "" + stone.getPosition().getLeft());

        if (stoneDragging.getPosition().getLeft() < 0) {
            return;
        }

        ImageView stoneCell = stonesGrid.get(stone.getPosition().getLeft()).get(stone.getPosition().getTop());
        stoneCell.setVisibility(View.INVISIBLE);
        stonesSet.stoneList.remove(stone);
    }

    private void addToGrid(Stone stone) {
        if (stone.getPosition().getLeft() < 0) {
            return;
        }

        for (Stone stoneExists : stonesSet.stoneList) {
            if (stoneExists.getPosition().equals(stone.getPosition())) {
                return;
            }
        }

        ImageView stoneCell = stonesGrid.get(stone.getPosition().getLeft()).get(stone.getPosition().getTop());
        String sizePart = "";
        switch (stone.getSize()) {
            case small:
                sizePart = "_small";
                break;
            case middle:
                sizePart = "_middle";
                break;
        }
        stoneCell.setVisibility(View.VISIBLE);
        stoneCell.setImageDrawable(ContextCompat.getDrawable(getContext(), Utils.getResId("ball_" + stoneDragging.getColor() + sizePart, R.drawable.class)));
        stonesSet.stoneList.add(stone);
    }

    private Stone detectStoneTap(float x, float y) {
        StonePosition position = detectGridPosition(x, y);

        if (position.getTop() >= 0 && position.getLeft() >= 0 && position.getTop() < 9 && position.getLeft() < 9) {
            for (Stone stoneExists : stonesSet.stoneList) {
                if (stoneExists.getPosition().equals(position)) {
                    return stoneExists;
                }
            }
        }

        for (int num = 0; num < 9; num++) {
            ImageView stone = stonesBigBasket.get(num);

            int[] location = {0, 0};
            stone.getLocationOnScreen(location);
            if (location[0] <= x && location[0] + stone.getWidth() >= x && location[1] <= y && location[1] + stone.getHeight() >= y) {
                return new Stone(StoneSize.large, colors[num], position);
            }

            stone = stonesMiddleBasket.get(num);
            stone.getLocationOnScreen(location);
            if (location[0] <= x && location[0] + stone.getWidth() >= x && location[1] <= y && location[1] + stone.getHeight() >= y) {
                return new Stone(StoneSize.middle, colors[num], position);
            }

            stone = stonesSmallBasket.get(num);
            stone.getLocationOnScreen(location);
            if (location[0] <= x && location[0] + stone.getWidth() >= x && location[1] <= y && location[1] + stone.getHeight() >= y) {
                return new Stone(StoneSize.small, colors[num], position);
            }
        }

        return null;
    }

    private StonePosition detectGridPosition(float x, float y) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                ImageView stoneForCheck = stonesGrid.get(row).get(col);
                int[] location = {0, 0};
                stoneForCheck.getLocationOnScreen(location);
                float size = stoneForCheck.getWidth();
                float halfSize = stoneForCheck.getWidth() / 2.0f;
                float quadSize = halfSize / 2.0f;
                if (x >= location[0] - sepWidth && x <= location[0] + size + sepWidth
                        && y >= location[1] - sepHeight && y <= location[1] + size + sepHeight) {
                    return new StonePosition(row, col);
                }
            }
        }

        return new StonePosition(-1, -1);
    }

    private int[] detectGridPositionLocation(float x, float y) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                ImageView stoneForCheck = stonesGrid.get(row).get(col);
                int[] location = {0, 0};
                stoneForCheck.getLocationOnScreen(location);
                float size = stoneForCheck.getWidth();
                float halfSize = stoneForCheck.getWidth() / 2.0f;
                float quadSize = halfSize / 2.0f;
                if (x >= location[0] - sepWidth && x <= location[0] + size + sepWidth
                        && y >= location[1] + sepHeight - statusBarHeight && y <= location[1] + size + sepHeight - statusBarHeight) {
                    location[0] = Math.round(location[0] + halfSize);
                    location[1] = Math.round(location[1] + halfSize - statusBarHeight);
                    return location;
                }
            }
        }

        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                stoneDragging = detectStoneTap(rawX, rawY);
                if (stoneDragging != null) {
                    draggableStone.setImageDrawable(ContextCompat.getDrawable(getContext(), Utils.getResId("ball_" + stoneDragging.getColor(), R.drawable.class)));
                    draggableStone.setTranslationX(x + draggableStone.getWidth() / 2.0f);
                    draggableStone.setTranslationY(y + draggableStone.getHeight() / 2.0f);
                    draggableStone.setVisibility(View.VISIBLE);

                    removeFromGrid(stoneDragging);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (stoneDragging != null) {
                    int[] gridLocation = detectGridPositionLocation(x, y);
                    if (gridLocation == null) {
                        draggableStone.setTranslationX(x - draggableStone.getWidth() / 2.0f);
                        draggableStone.setTranslationY(y - draggableStone.getHeight() / 2.0f);
                    } else {
                        draggableStone.setTranslationX(gridLocation[0] - draggableStone.getWidth() / 2.0f);
                        draggableStone.setTranslationY(gridLocation[1] - draggableStone.getHeight() / 2.0f);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (stoneDragging != null) {
                    stoneDragging = new Stone(stoneDragging.getSize(), stoneDragging.getColor(),
                            detectGridPosition(rawX, rawY));
                    addToGrid(stoneDragging);
                }
                stoneDragging = null;
                draggableStone.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }
}
