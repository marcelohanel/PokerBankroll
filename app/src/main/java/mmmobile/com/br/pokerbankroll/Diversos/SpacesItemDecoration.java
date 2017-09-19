package mmmobile.com.br.pokerbankroll.Diversos;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public SpacesItemDecoration() {
        this.space = Funcoes.SPACE_BETWEEN_ITEMS;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;

        // Add top margin only for the first or second item to avoid double space between items
        // Add top margin only for the first or second item to avoid double space between items
        //   if ((parent.getChildCount() > 0 && parent.getChildPosition(view) == 0)
        //           || (parent.getChildCount() > 1 && parent.getChildPosition(view) == 1))
        //       outRect.top = space;
    }
}
