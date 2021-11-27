package com.inadesirrasukma.ugd_lib_a_10310.adapter;

import com.inadesirrasukma.ugd_lib_a_10310.model.PurchasedGame;

public interface OnCartClickListener {
    void onTambah(PurchasedGame purchasedGame, int position);
    void onKurang(PurchasedGame purchasedGame, int position);
}
