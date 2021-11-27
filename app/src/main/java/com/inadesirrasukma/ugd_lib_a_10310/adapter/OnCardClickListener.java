package com.inadesirrasukma.ugd_lib_a_10310.adapter;

import com.inadesirrasukma.ugd_lib_a_10310.model.Game;

public interface OnCardClickListener {
    void onClick(Game game);

    void onChartClick(Game game);

    void onDescriptionClick(String desc);
}
